package id.lariss.web.rest;

import static id.lariss.domain.WarrantyAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Warranty;
import id.lariss.repository.EntityManager;
import id.lariss.repository.WarrantyRepository;
import id.lariss.service.dto.WarrantyDTO;
import id.lariss.service.mapper.WarrantyMapper;
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
 * Integration tests for the {@link WarrantyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class WarrantyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_WARRANTY_EN = "AAAAAAAAAA";
    private static final String UPDATED_WARRANTY_EN = "BBBBBBBBBB";

    private static final String DEFAULT_WARRANTY_ID = "AAAAAAAAAA";
    private static final String UPDATED_WARRANTY_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/warranties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WarrantyRepository warrantyRepository;

    @Autowired
    private WarrantyMapper warrantyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Warranty warranty;

    private Warranty insertedWarranty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Warranty createEntity() {
        return new Warranty().name(DEFAULT_NAME).warrantyEn(DEFAULT_WARRANTY_EN).warrantyId(DEFAULT_WARRANTY_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Warranty createUpdatedEntity() {
        return new Warranty().name(UPDATED_NAME).warrantyEn(UPDATED_WARRANTY_EN).warrantyId(UPDATED_WARRANTY_ID);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Warranty.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        warranty = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedWarranty != null) {
            warrantyRepository.delete(insertedWarranty).block();
            insertedWarranty = null;
        }
        deleteEntities(em);
    }

    @Test
    void createWarranty() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Warranty
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);
        var returnedWarrantyDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(warrantyDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(WarrantyDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Warranty in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWarranty = warrantyMapper.toEntity(returnedWarrantyDTO);
        assertWarrantyUpdatableFieldsEquals(returnedWarranty, getPersistedWarranty(returnedWarranty));

        insertedWarranty = returnedWarranty;
    }

    @Test
    void createWarrantyWithExistingId() throws Exception {
        // Create the Warranty with an existing ID
        warranty.setId(1L);
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(warrantyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        warranty.setName(null);

        // Create the Warranty, which fails.
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(warrantyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllWarranties() {
        // Initialize the database
        insertedWarranty = warrantyRepository.save(warranty).block();

        // Get all the warrantyList
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
            .value(hasItem(warranty.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].warrantyEn")
            .value(hasItem(DEFAULT_WARRANTY_EN))
            .jsonPath("$.[*].warrantyId")
            .value(hasItem(DEFAULT_WARRANTY_ID));
    }

    @Test
    void getWarranty() {
        // Initialize the database
        insertedWarranty = warrantyRepository.save(warranty).block();

        // Get the warranty
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, warranty.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(warranty.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.warrantyEn")
            .value(is(DEFAULT_WARRANTY_EN))
            .jsonPath("$.warrantyId")
            .value(is(DEFAULT_WARRANTY_ID));
    }

    @Test
    void getNonExistingWarranty() {
        // Get the warranty
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingWarranty() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.save(warranty).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the warranty
        Warranty updatedWarranty = warrantyRepository.findById(warranty.getId()).block();
        updatedWarranty.name(UPDATED_NAME).warrantyEn(UPDATED_WARRANTY_EN).warrantyId(UPDATED_WARRANTY_ID);
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(updatedWarranty);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, warrantyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(warrantyDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWarrantyToMatchAllProperties(updatedWarranty);
    }

    @Test
    void putNonExistingWarranty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warranty.setId(longCount.incrementAndGet());

        // Create the Warranty
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, warrantyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(warrantyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchWarranty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warranty.setId(longCount.incrementAndGet());

        // Create the Warranty
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(warrantyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamWarranty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warranty.setId(longCount.incrementAndGet());

        // Create the Warranty
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(warrantyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateWarrantyWithPatch() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.save(warranty).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the warranty using partial update
        Warranty partialUpdatedWarranty = new Warranty();
        partialUpdatedWarranty.setId(warranty.getId());

        partialUpdatedWarranty.name(UPDATED_NAME).warrantyEn(UPDATED_WARRANTY_EN).warrantyId(UPDATED_WARRANTY_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWarranty.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedWarranty))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Warranty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWarrantyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedWarranty, warranty), getPersistedWarranty(warranty));
    }

    @Test
    void fullUpdateWarrantyWithPatch() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.save(warranty).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the warranty using partial update
        Warranty partialUpdatedWarranty = new Warranty();
        partialUpdatedWarranty.setId(warranty.getId());

        partialUpdatedWarranty.name(UPDATED_NAME).warrantyEn(UPDATED_WARRANTY_EN).warrantyId(UPDATED_WARRANTY_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWarranty.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedWarranty))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Warranty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWarrantyUpdatableFieldsEquals(partialUpdatedWarranty, getPersistedWarranty(partialUpdatedWarranty));
    }

    @Test
    void patchNonExistingWarranty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warranty.setId(longCount.incrementAndGet());

        // Create the Warranty
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, warrantyDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(warrantyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchWarranty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warranty.setId(longCount.incrementAndGet());

        // Create the Warranty
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(warrantyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamWarranty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warranty.setId(longCount.incrementAndGet());

        // Create the Warranty
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(warrantyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteWarranty() {
        // Initialize the database
        insertedWarranty = warrantyRepository.save(warranty).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the warranty
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, warranty.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return warrantyRepository.count().block();
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

    protected Warranty getPersistedWarranty(Warranty warranty) {
        return warrantyRepository.findById(warranty.getId()).block();
    }

    protected void assertPersistedWarrantyToMatchAllProperties(Warranty expectedWarranty) {
        // Test fails because reactive api returns an empty object instead of null
        // assertWarrantyAllPropertiesEquals(expectedWarranty, getPersistedWarranty(expectedWarranty));
        assertWarrantyUpdatableFieldsEquals(expectedWarranty, getPersistedWarranty(expectedWarranty));
    }

    protected void assertPersistedWarrantyToMatchUpdatableProperties(Warranty expectedWarranty) {
        // Test fails because reactive api returns an empty object instead of null
        // assertWarrantyAllUpdatablePropertiesEquals(expectedWarranty, getPersistedWarranty(expectedWarranty));
        assertWarrantyUpdatableFieldsEquals(expectedWarranty, getPersistedWarranty(expectedWarranty));
    }
}
