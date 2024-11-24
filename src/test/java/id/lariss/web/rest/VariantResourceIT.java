package id.lariss.web.rest;

import static id.lariss.domain.VariantAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Variant;
import id.lariss.repository.EntityManager;
import id.lariss.repository.VariantRepository;
import id.lariss.service.VariantService;
import id.lariss.service.dto.VariantDTO;
import id.lariss.service.mapper.VariantMapper;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link VariantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class VariantResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/variants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VariantRepository variantRepository;

    @Mock
    private VariantRepository variantRepositoryMock;

    @Autowired
    private VariantMapper variantMapper;

    @Mock
    private VariantService variantServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Variant variant;

    private Variant insertedVariant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Variant createEntity() {
        return new Variant().label(DEFAULT_LABEL).sku(DEFAULT_SKU);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Variant createUpdatedEntity() {
        return new Variant().label(UPDATED_LABEL).sku(UPDATED_SKU);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Variant.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        variant = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVariant != null) {
            variantRepository.delete(insertedVariant).block();
            insertedVariant = null;
        }
        deleteEntities(em);
    }

    @Test
    void createVariant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Variant
        VariantDTO variantDTO = variantMapper.toDto(variant);
        var returnedVariantDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(variantDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(VariantDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Variant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVariant = variantMapper.toEntity(returnedVariantDTO);
        assertVariantUpdatableFieldsEquals(returnedVariant, getPersistedVariant(returnedVariant));

        insertedVariant = returnedVariant;
    }

    @Test
    void createVariantWithExistingId() throws Exception {
        // Create the Variant with an existing ID
        variant.setId(1L);
        VariantDTO variantDTO = variantMapper.toDto(variant);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(variantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllVariants() {
        // Initialize the database
        insertedVariant = variantRepository.save(variant).block();

        // Get all the variantList
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
            .value(hasItem(variant.getId().intValue()))
            .jsonPath("$.[*].label")
            .value(hasItem(DEFAULT_LABEL))
            .jsonPath("$.[*].sku")
            .value(hasItem(DEFAULT_SKU));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVariantsWithEagerRelationshipsIsEnabled() {
        when(variantServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(variantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVariantsWithEagerRelationshipsIsNotEnabled() {
        when(variantServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(variantRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getVariant() {
        // Initialize the database
        insertedVariant = variantRepository.save(variant).block();

        // Get the variant
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, variant.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(variant.getId().intValue()))
            .jsonPath("$.label")
            .value(is(DEFAULT_LABEL))
            .jsonPath("$.sku")
            .value(is(DEFAULT_SKU));
    }

    @Test
    void getNonExistingVariant() {
        // Get the variant
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingVariant() throws Exception {
        // Initialize the database
        insertedVariant = variantRepository.save(variant).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the variant
        Variant updatedVariant = variantRepository.findById(variant.getId()).block();
        updatedVariant.label(UPDATED_LABEL).sku(UPDATED_SKU);
        VariantDTO variantDTO = variantMapper.toDto(updatedVariant);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, variantDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(variantDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVariantToMatchAllProperties(updatedVariant);
    }

    @Test
    void putNonExistingVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        variant.setId(longCount.incrementAndGet());

        // Create the Variant
        VariantDTO variantDTO = variantMapper.toDto(variant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, variantDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(variantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        variant.setId(longCount.incrementAndGet());

        // Create the Variant
        VariantDTO variantDTO = variantMapper.toDto(variant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(variantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        variant.setId(longCount.incrementAndGet());

        // Create the Variant
        VariantDTO variantDTO = variantMapper.toDto(variant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(variantDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateVariantWithPatch() throws Exception {
        // Initialize the database
        insertedVariant = variantRepository.save(variant).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the variant using partial update
        Variant partialUpdatedVariant = new Variant();
        partialUpdatedVariant.setId(variant.getId());

        partialUpdatedVariant.label(UPDATED_LABEL).sku(UPDATED_SKU);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVariant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedVariant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Variant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVariantUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVariant, variant), getPersistedVariant(variant));
    }

    @Test
    void fullUpdateVariantWithPatch() throws Exception {
        // Initialize the database
        insertedVariant = variantRepository.save(variant).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the variant using partial update
        Variant partialUpdatedVariant = new Variant();
        partialUpdatedVariant.setId(variant.getId());

        partialUpdatedVariant.label(UPDATED_LABEL).sku(UPDATED_SKU);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVariant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedVariant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Variant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVariantUpdatableFieldsEquals(partialUpdatedVariant, getPersistedVariant(partialUpdatedVariant));
    }

    @Test
    void patchNonExistingVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        variant.setId(longCount.incrementAndGet());

        // Create the Variant
        VariantDTO variantDTO = variantMapper.toDto(variant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, variantDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(variantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        variant.setId(longCount.incrementAndGet());

        // Create the Variant
        VariantDTO variantDTO = variantMapper.toDto(variant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(variantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        variant.setId(longCount.incrementAndGet());

        // Create the Variant
        VariantDTO variantDTO = variantMapper.toDto(variant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(variantDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteVariant() {
        // Initialize the database
        insertedVariant = variantRepository.save(variant).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the variant
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, variant.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return variantRepository.count().block();
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

    protected Variant getPersistedVariant(Variant variant) {
        return variantRepository.findById(variant.getId()).block();
    }

    protected void assertPersistedVariantToMatchAllProperties(Variant expectedVariant) {
        // Test fails because reactive api returns an empty object instead of null
        // assertVariantAllPropertiesEquals(expectedVariant, getPersistedVariant(expectedVariant));
        assertVariantUpdatableFieldsEquals(expectedVariant, getPersistedVariant(expectedVariant));
    }

    protected void assertPersistedVariantToMatchUpdatableProperties(Variant expectedVariant) {
        // Test fails because reactive api returns an empty object instead of null
        // assertVariantAllUpdatablePropertiesEquals(expectedVariant, getPersistedVariant(expectedVariant));
        assertVariantUpdatableFieldsEquals(expectedVariant, getPersistedVariant(expectedVariant));
    }
}
