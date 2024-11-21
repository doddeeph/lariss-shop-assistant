package id.lariss.web.rest;

import static id.lariss.domain.BoxContentAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.BoxContent;
import id.lariss.repository.BoxContentRepository;
import id.lariss.repository.EntityManager;
import id.lariss.service.dto.BoxContentDTO;
import id.lariss.service.mapper.BoxContentMapper;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link BoxContentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class BoxContentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_EN = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_EN = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/box-contents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BoxContentRepository boxContentRepository;

    @Autowired
    private BoxContentMapper boxContentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private BoxContent boxContent;

    private BoxContent insertedBoxContent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoxContent createEntity() {
        return new BoxContent().name(DEFAULT_NAME).contentEn(DEFAULT_CONTENT_EN).contentId(DEFAULT_CONTENT_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoxContent createUpdatedEntity() {
        return new BoxContent().name(UPDATED_NAME).contentEn(UPDATED_CONTENT_EN).contentId(UPDATED_CONTENT_ID);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(BoxContent.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        boxContent = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBoxContent != null) {
            boxContentRepository.delete(insertedBoxContent).block();
            insertedBoxContent = null;
        }
        deleteEntities(em);
    }

    @Test
    void createBoxContent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BoxContent
        BoxContentDTO boxContentDTO = boxContentMapper.toDto(boxContent);
        var returnedBoxContentDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(boxContentDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(BoxContentDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the BoxContent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBoxContent = boxContentMapper.toEntity(returnedBoxContentDTO);
        assertBoxContentUpdatableFieldsEquals(returnedBoxContent, getPersistedBoxContent(returnedBoxContent));

        insertedBoxContent = returnedBoxContent;
    }

    @Test
    void createBoxContentWithExistingId() throws Exception {
        // Create the BoxContent with an existing ID
        boxContent.setId(1L);
        BoxContentDTO boxContentDTO = boxContentMapper.toDto(boxContent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(boxContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BoxContent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        boxContent.setName(null);

        // Create the BoxContent, which fails.
        BoxContentDTO boxContentDTO = boxContentMapper.toDto(boxContent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(boxContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllBoxContents() {
        // Initialize the database
        insertedBoxContent = boxContentRepository.save(boxContent).block();

        // Get all the boxContentList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(boxContent.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].contentEn")
            .value(hasItem(DEFAULT_CONTENT_EN))
            .jsonPath("$.[*].contentId")
            .value(hasItem(DEFAULT_CONTENT_ID));
    }

    @Test
    void getBoxContent() {
        // Initialize the database
        insertedBoxContent = boxContentRepository.save(boxContent).block();

        // Get the boxContent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, boxContent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(boxContent.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.contentEn")
            .value(is(DEFAULT_CONTENT_EN))
            .jsonPath("$.contentId")
            .value(is(DEFAULT_CONTENT_ID));
    }

    @Test
    void getNonExistingBoxContent() {
        // Get the boxContent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingBoxContent() throws Exception {
        // Initialize the database
        insertedBoxContent = boxContentRepository.save(boxContent).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the boxContent
        BoxContent updatedBoxContent = boxContentRepository.findById(boxContent.getId()).block();
        updatedBoxContent.name(UPDATED_NAME).contentEn(UPDATED_CONTENT_EN).contentId(UPDATED_CONTENT_ID);
        BoxContentDTO boxContentDTO = boxContentMapper.toDto(updatedBoxContent);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, boxContentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(boxContentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BoxContent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBoxContentToMatchAllProperties(updatedBoxContent);
    }

    @Test
    void putNonExistingBoxContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        boxContent.setId(longCount.incrementAndGet());

        // Create the BoxContent
        BoxContentDTO boxContentDTO = boxContentMapper.toDto(boxContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, boxContentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(boxContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BoxContent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBoxContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        boxContent.setId(longCount.incrementAndGet());

        // Create the BoxContent
        BoxContentDTO boxContentDTO = boxContentMapper.toDto(boxContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(boxContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BoxContent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBoxContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        boxContent.setId(longCount.incrementAndGet());

        // Create the BoxContent
        BoxContentDTO boxContentDTO = boxContentMapper.toDto(boxContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(boxContentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BoxContent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBoxContentWithPatch() throws Exception {
        // Initialize the database
        insertedBoxContent = boxContentRepository.save(boxContent).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the boxContent using partial update
        BoxContent partialUpdatedBoxContent = new BoxContent();
        partialUpdatedBoxContent.setId(boxContent.getId());

        partialUpdatedBoxContent.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBoxContent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedBoxContent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BoxContent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBoxContentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBoxContent, boxContent),
            getPersistedBoxContent(boxContent)
        );
    }

    @Test
    void fullUpdateBoxContentWithPatch() throws Exception {
        // Initialize the database
        insertedBoxContent = boxContentRepository.save(boxContent).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the boxContent using partial update
        BoxContent partialUpdatedBoxContent = new BoxContent();
        partialUpdatedBoxContent.setId(boxContent.getId());

        partialUpdatedBoxContent.name(UPDATED_NAME).contentEn(UPDATED_CONTENT_EN).contentId(UPDATED_CONTENT_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBoxContent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedBoxContent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BoxContent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBoxContentUpdatableFieldsEquals(partialUpdatedBoxContent, getPersistedBoxContent(partialUpdatedBoxContent));
    }

    @Test
    void patchNonExistingBoxContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        boxContent.setId(longCount.incrementAndGet());

        // Create the BoxContent
        BoxContentDTO boxContentDTO = boxContentMapper.toDto(boxContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, boxContentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(boxContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BoxContent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBoxContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        boxContent.setId(longCount.incrementAndGet());

        // Create the BoxContent
        BoxContentDTO boxContentDTO = boxContentMapper.toDto(boxContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(boxContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BoxContent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBoxContent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        boxContent.setId(longCount.incrementAndGet());

        // Create the BoxContent
        BoxContentDTO boxContentDTO = boxContentMapper.toDto(boxContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(boxContentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BoxContent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBoxContent() {
        // Initialize the database
        insertedBoxContent = boxContentRepository.save(boxContent).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the boxContent
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, boxContent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return boxContentRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected BoxContent getPersistedBoxContent(BoxContent boxContent) {
        return boxContentRepository.findById(boxContent.getId()).block();
    }

    protected void assertPersistedBoxContentToMatchAllProperties(BoxContent expectedBoxContent) {
        // Test fails because reactive api returns an empty object instead of null
        // assertBoxContentAllPropertiesEquals(expectedBoxContent, getPersistedBoxContent(expectedBoxContent));
        assertBoxContentUpdatableFieldsEquals(expectedBoxContent, getPersistedBoxContent(expectedBoxContent));
    }

    protected void assertPersistedBoxContentToMatchUpdatableProperties(BoxContent expectedBoxContent) {
        // Test fails because reactive api returns an empty object instead of null
        // assertBoxContentAllUpdatablePropertiesEquals(expectedBoxContent, getPersistedBoxContent(expectedBoxContent));
        assertBoxContentUpdatableFieldsEquals(expectedBoxContent, getPersistedBoxContent(expectedBoxContent));
    }
}
