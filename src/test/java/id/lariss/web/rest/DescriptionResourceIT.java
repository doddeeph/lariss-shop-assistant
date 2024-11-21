package id.lariss.web.rest;

import static id.lariss.domain.DescriptionAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Description;
import id.lariss.repository.DescriptionRepository;
import id.lariss.repository.EntityManager;
import id.lariss.service.dto.DescriptionDTO;
import id.lariss.service.mapper.DescriptionMapper;
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
 * Integration tests for the {@link DescriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DescriptionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_EN = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_EN = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/descriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DescriptionRepository descriptionRepository;

    @Autowired
    private DescriptionMapper descriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Description description;

    private Description insertedDescription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Description createEntity() {
        return new Description().name(DEFAULT_NAME).descriptionEn(DEFAULT_DESCRIPTION_EN).descriptionId(DEFAULT_DESCRIPTION_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Description createUpdatedEntity() {
        return new Description().name(UPDATED_NAME).descriptionEn(UPDATED_DESCRIPTION_EN).descriptionId(UPDATED_DESCRIPTION_ID);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Description.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        description = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDescription != null) {
            descriptionRepository.delete(insertedDescription).block();
            insertedDescription = null;
        }
        deleteEntities(em);
    }

    @Test
    void createDescription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);
        var returnedDescriptionDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(descriptionDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(DescriptionDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Description in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDescription = descriptionMapper.toEntity(returnedDescriptionDTO);
        assertDescriptionUpdatableFieldsEquals(returnedDescription, getPersistedDescription(returnedDescription));

        insertedDescription = returnedDescription;
    }

    @Test
    void createDescriptionWithExistingId() throws Exception {
        // Create the Description with an existing ID
        description.setId(1L);
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(descriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        description.setName(null);

        // Create the Description, which fails.
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(descriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllDescriptions() {
        // Initialize the database
        insertedDescription = descriptionRepository.save(description).block();

        // Get all the descriptionList
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
            .value(hasItem(description.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].descriptionEn")
            .value(hasItem(DEFAULT_DESCRIPTION_EN))
            .jsonPath("$.[*].descriptionId")
            .value(hasItem(DEFAULT_DESCRIPTION_ID));
    }

    @Test
    void getDescription() {
        // Initialize the database
        insertedDescription = descriptionRepository.save(description).block();

        // Get the description
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, description.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(description.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.descriptionEn")
            .value(is(DEFAULT_DESCRIPTION_EN))
            .jsonPath("$.descriptionId")
            .value(is(DEFAULT_DESCRIPTION_ID));
    }

    @Test
    void getNonExistingDescription() {
        // Get the description
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDescription() throws Exception {
        // Initialize the database
        insertedDescription = descriptionRepository.save(description).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the description
        Description updatedDescription = descriptionRepository.findById(description.getId()).block();
        updatedDescription.name(UPDATED_NAME).descriptionEn(UPDATED_DESCRIPTION_EN).descriptionId(UPDATED_DESCRIPTION_ID);
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(updatedDescription);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, descriptionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(descriptionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDescriptionToMatchAllProperties(updatedDescription);
    }

    @Test
    void putNonExistingDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        description.setId(longCount.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, descriptionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(descriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        description.setId(longCount.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(descriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        description.setId(longCount.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(descriptionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDescriptionWithPatch() throws Exception {
        // Initialize the database
        insertedDescription = descriptionRepository.save(description).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the description using partial update
        Description partialUpdatedDescription = new Description();
        partialUpdatedDescription.setId(description.getId());

        partialUpdatedDescription.descriptionEn(UPDATED_DESCRIPTION_EN).descriptionId(UPDATED_DESCRIPTION_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDescription.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedDescription))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Description in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDescriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDescription, description),
            getPersistedDescription(description)
        );
    }

    @Test
    void fullUpdateDescriptionWithPatch() throws Exception {
        // Initialize the database
        insertedDescription = descriptionRepository.save(description).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the description using partial update
        Description partialUpdatedDescription = new Description();
        partialUpdatedDescription.setId(description.getId());

        partialUpdatedDescription.name(UPDATED_NAME).descriptionEn(UPDATED_DESCRIPTION_EN).descriptionId(UPDATED_DESCRIPTION_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDescription.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedDescription))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Description in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDescriptionUpdatableFieldsEquals(partialUpdatedDescription, getPersistedDescription(partialUpdatedDescription));
    }

    @Test
    void patchNonExistingDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        description.setId(longCount.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, descriptionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(descriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        description.setId(longCount.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(descriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        description.setId(longCount.incrementAndGet());

        // Create the Description
        DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(descriptionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Description in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDescription() {
        // Initialize the database
        insertedDescription = descriptionRepository.save(description).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the description
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, description.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return descriptionRepository.count().block();
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

    protected Description getPersistedDescription(Description description) {
        return descriptionRepository.findById(description.getId()).block();
    }

    protected void assertPersistedDescriptionToMatchAllProperties(Description expectedDescription) {
        // Test fails because reactive api returns an empty object instead of null
        // assertDescriptionAllPropertiesEquals(expectedDescription, getPersistedDescription(expectedDescription));
        assertDescriptionUpdatableFieldsEquals(expectedDescription, getPersistedDescription(expectedDescription));
    }

    protected void assertPersistedDescriptionToMatchUpdatableProperties(Description expectedDescription) {
        // Test fails because reactive api returns an empty object instead of null
        // assertDescriptionAllUpdatablePropertiesEquals(expectedDescription, getPersistedDescription(expectedDescription));
        assertDescriptionUpdatableFieldsEquals(expectedDescription, getPersistedDescription(expectedDescription));
    }
}
