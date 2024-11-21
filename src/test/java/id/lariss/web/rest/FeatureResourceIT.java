package id.lariss.web.rest;

import static id.lariss.domain.FeatureAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Feature;
import id.lariss.repository.EntityManager;
import id.lariss.repository.FeatureRepository;
import id.lariss.service.dto.FeatureDTO;
import id.lariss.service.mapper.FeatureMapper;
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
 * Integration tests for the {@link FeatureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FeatureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FEATURE_EN = "AAAAAAAAAA";
    private static final String UPDATED_FEATURE_EN = "BBBBBBBBBB";

    private static final String DEFAULT_FEATURE_ID = "AAAAAAAAAA";
    private static final String UPDATED_FEATURE_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/features";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private FeatureMapper featureMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Feature feature;

    private Feature insertedFeature;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feature createEntity() {
        return new Feature().name(DEFAULT_NAME).featureEn(DEFAULT_FEATURE_EN).featureId(DEFAULT_FEATURE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feature createUpdatedEntity() {
        return new Feature().name(UPDATED_NAME).featureEn(UPDATED_FEATURE_EN).featureId(UPDATED_FEATURE_ID);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Feature.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        feature = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFeature != null) {
            featureRepository.delete(insertedFeature).block();
            insertedFeature = null;
        }
        deleteEntities(em);
    }

    @Test
    void createFeature() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Feature
        FeatureDTO featureDTO = featureMapper.toDto(feature);
        var returnedFeatureDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(featureDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(FeatureDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Feature in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFeature = featureMapper.toEntity(returnedFeatureDTO);
        assertFeatureUpdatableFieldsEquals(returnedFeature, getPersistedFeature(returnedFeature));

        insertedFeature = returnedFeature;
    }

    @Test
    void createFeatureWithExistingId() throws Exception {
        // Create the Feature with an existing ID
        feature.setId(1L);
        FeatureDTO featureDTO = featureMapper.toDto(feature);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(featureDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feature.setName(null);

        // Create the Feature, which fails.
        FeatureDTO featureDTO = featureMapper.toDto(feature);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(featureDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllFeatures() {
        // Initialize the database
        insertedFeature = featureRepository.save(feature).block();

        // Get all the featureList
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
            .value(hasItem(feature.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].featureEn")
            .value(hasItem(DEFAULT_FEATURE_EN))
            .jsonPath("$.[*].featureId")
            .value(hasItem(DEFAULT_FEATURE_ID));
    }

    @Test
    void getFeature() {
        // Initialize the database
        insertedFeature = featureRepository.save(feature).block();

        // Get the feature
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, feature.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(feature.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.featureEn")
            .value(is(DEFAULT_FEATURE_EN))
            .jsonPath("$.featureId")
            .value(is(DEFAULT_FEATURE_ID));
    }

    @Test
    void getNonExistingFeature() {
        // Get the feature
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFeature() throws Exception {
        // Initialize the database
        insertedFeature = featureRepository.save(feature).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feature
        Feature updatedFeature = featureRepository.findById(feature.getId()).block();
        updatedFeature.name(UPDATED_NAME).featureEn(UPDATED_FEATURE_EN).featureId(UPDATED_FEATURE_ID);
        FeatureDTO featureDTO = featureMapper.toDto(updatedFeature);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, featureDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(featureDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeatureToMatchAllProperties(updatedFeature);
    }

    @Test
    void putNonExistingFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feature.setId(longCount.incrementAndGet());

        // Create the Feature
        FeatureDTO featureDTO = featureMapper.toDto(feature);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, featureDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(featureDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feature.setId(longCount.incrementAndGet());

        // Create the Feature
        FeatureDTO featureDTO = featureMapper.toDto(feature);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(featureDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feature.setId(longCount.incrementAndGet());

        // Create the Feature
        FeatureDTO featureDTO = featureMapper.toDto(feature);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(featureDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFeatureWithPatch() throws Exception {
        // Initialize the database
        insertedFeature = featureRepository.save(feature).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feature using partial update
        Feature partialUpdatedFeature = new Feature();
        partialUpdatedFeature.setId(feature.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeature.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFeature))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Feature in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeatureUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFeature, feature), getPersistedFeature(feature));
    }

    @Test
    void fullUpdateFeatureWithPatch() throws Exception {
        // Initialize the database
        insertedFeature = featureRepository.save(feature).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feature using partial update
        Feature partialUpdatedFeature = new Feature();
        partialUpdatedFeature.setId(feature.getId());

        partialUpdatedFeature.name(UPDATED_NAME).featureEn(UPDATED_FEATURE_EN).featureId(UPDATED_FEATURE_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeature.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFeature))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Feature in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeatureUpdatableFieldsEquals(partialUpdatedFeature, getPersistedFeature(partialUpdatedFeature));
    }

    @Test
    void patchNonExistingFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feature.setId(longCount.incrementAndGet());

        // Create the Feature
        FeatureDTO featureDTO = featureMapper.toDto(feature);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, featureDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(featureDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feature.setId(longCount.incrementAndGet());

        // Create the Feature
        FeatureDTO featureDTO = featureMapper.toDto(feature);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(featureDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feature.setId(longCount.incrementAndGet());

        // Create the Feature
        FeatureDTO featureDTO = featureMapper.toDto(feature);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(featureDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFeature() {
        // Initialize the database
        insertedFeature = featureRepository.save(feature).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feature
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, feature.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return featureRepository.count().block();
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

    protected Feature getPersistedFeature(Feature feature) {
        return featureRepository.findById(feature.getId()).block();
    }

    protected void assertPersistedFeatureToMatchAllProperties(Feature expectedFeature) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFeatureAllPropertiesEquals(expectedFeature, getPersistedFeature(expectedFeature));
        assertFeatureUpdatableFieldsEquals(expectedFeature, getPersistedFeature(expectedFeature));
    }

    protected void assertPersistedFeatureToMatchUpdatableProperties(Feature expectedFeature) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFeatureAllUpdatablePropertiesEquals(expectedFeature, getPersistedFeature(expectedFeature));
        assertFeatureUpdatableFieldsEquals(expectedFeature, getPersistedFeature(expectedFeature));
    }
}
