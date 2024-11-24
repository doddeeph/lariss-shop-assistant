package id.lariss.web.rest;

import static id.lariss.domain.StorageAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Storage;
import id.lariss.repository.EntityManager;
import id.lariss.repository.StorageRepository;
import id.lariss.service.dto.StorageDTO;
import id.lariss.service.mapper.StorageMapper;
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
 * Integration tests for the {@link StorageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class StorageResourceIT {

    private static final String DEFAULT_ATTRIBUTE_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/storages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private StorageMapper storageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Storage storage;

    private Storage insertedStorage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Storage createEntity() {
        return new Storage()
            .attributeLabel(DEFAULT_ATTRIBUTE_LABEL)
            .attributeName(DEFAULT_ATTRIBUTE_NAME)
            .optionLabel(DEFAULT_OPTION_LABEL)
            .optionValue(DEFAULT_OPTION_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Storage createUpdatedEntity() {
        return new Storage()
            .attributeLabel(UPDATED_ATTRIBUTE_LABEL)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .optionLabel(UPDATED_OPTION_LABEL)
            .optionValue(UPDATED_OPTION_VALUE);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Storage.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        storage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStorage != null) {
            storageRepository.delete(insertedStorage).block();
            insertedStorage = null;
        }
        deleteEntities(em);
    }

    @Test
    void createStorage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);
        var returnedStorageDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(storageDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(StorageDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Storage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStorage = storageMapper.toEntity(returnedStorageDTO);
        assertStorageUpdatableFieldsEquals(returnedStorage, getPersistedStorage(returnedStorage));

        insertedStorage = returnedStorage;
    }

    @Test
    void createStorageWithExistingId() throws Exception {
        // Create the Storage with an existing ID
        storage.setId(1L);
        StorageDTO storageDTO = storageMapper.toDto(storage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(storageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllStorages() {
        // Initialize the database
        insertedStorage = storageRepository.save(storage).block();

        // Get all the storageList
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
            .value(hasItem(storage.getId().intValue()))
            .jsonPath("$.[*].attributeLabel")
            .value(hasItem(DEFAULT_ATTRIBUTE_LABEL))
            .jsonPath("$.[*].attributeName")
            .value(hasItem(DEFAULT_ATTRIBUTE_NAME))
            .jsonPath("$.[*].optionLabel")
            .value(hasItem(DEFAULT_OPTION_LABEL))
            .jsonPath("$.[*].optionValue")
            .value(hasItem(DEFAULT_OPTION_VALUE));
    }

    @Test
    void getStorage() {
        // Initialize the database
        insertedStorage = storageRepository.save(storage).block();

        // Get the storage
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, storage.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(storage.getId().intValue()))
            .jsonPath("$.attributeLabel")
            .value(is(DEFAULT_ATTRIBUTE_LABEL))
            .jsonPath("$.attributeName")
            .value(is(DEFAULT_ATTRIBUTE_NAME))
            .jsonPath("$.optionLabel")
            .value(is(DEFAULT_OPTION_LABEL))
            .jsonPath("$.optionValue")
            .value(is(DEFAULT_OPTION_VALUE));
    }

    @Test
    void getNonExistingStorage() {
        // Get the storage
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingStorage() throws Exception {
        // Initialize the database
        insertedStorage = storageRepository.save(storage).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storage
        Storage updatedStorage = storageRepository.findById(storage.getId()).block();
        updatedStorage
            .attributeLabel(UPDATED_ATTRIBUTE_LABEL)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .optionLabel(UPDATED_OPTION_LABEL)
            .optionValue(UPDATED_OPTION_VALUE);
        StorageDTO storageDTO = storageMapper.toDto(updatedStorage);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, storageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(storageDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStorageToMatchAllProperties(updatedStorage);
    }

    @Test
    void putNonExistingStorage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storage.setId(longCount.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, storageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(storageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchStorage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storage.setId(longCount.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(storageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamStorage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storage.setId(longCount.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(storageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateStorageWithPatch() throws Exception {
        // Initialize the database
        insertedStorage = storageRepository.save(storage).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storage using partial update
        Storage partialUpdatedStorage = new Storage();
        partialUpdatedStorage.setId(storage.getId());

        partialUpdatedStorage.attributeLabel(UPDATED_ATTRIBUTE_LABEL).optionValue(UPDATED_OPTION_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedStorage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedStorage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Storage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStorageUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedStorage, storage), getPersistedStorage(storage));
    }

    @Test
    void fullUpdateStorageWithPatch() throws Exception {
        // Initialize the database
        insertedStorage = storageRepository.save(storage).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storage using partial update
        Storage partialUpdatedStorage = new Storage();
        partialUpdatedStorage.setId(storage.getId());

        partialUpdatedStorage
            .attributeLabel(UPDATED_ATTRIBUTE_LABEL)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .optionLabel(UPDATED_OPTION_LABEL)
            .optionValue(UPDATED_OPTION_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedStorage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedStorage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Storage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStorageUpdatableFieldsEquals(partialUpdatedStorage, getPersistedStorage(partialUpdatedStorage));
    }

    @Test
    void patchNonExistingStorage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storage.setId(longCount.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, storageDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(storageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchStorage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storage.setId(longCount.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(storageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamStorage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        storage.setId(longCount.incrementAndGet());

        // Create the Storage
        StorageDTO storageDTO = storageMapper.toDto(storage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(storageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Storage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteStorage() {
        // Initialize the database
        insertedStorage = storageRepository.save(storage).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the storage
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, storage.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return storageRepository.count().block();
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

    protected Storage getPersistedStorage(Storage storage) {
        return storageRepository.findById(storage.getId()).block();
    }

    protected void assertPersistedStorageToMatchAllProperties(Storage expectedStorage) {
        // Test fails because reactive api returns an empty object instead of null
        // assertStorageAllPropertiesEquals(expectedStorage, getPersistedStorage(expectedStorage));
        assertStorageUpdatableFieldsEquals(expectedStorage, getPersistedStorage(expectedStorage));
    }

    protected void assertPersistedStorageToMatchUpdatableProperties(Storage expectedStorage) {
        // Test fails because reactive api returns an empty object instead of null
        // assertStorageAllUpdatablePropertiesEquals(expectedStorage, getPersistedStorage(expectedStorage));
        assertStorageUpdatableFieldsEquals(expectedStorage, getPersistedStorage(expectedStorage));
    }
}
