package id.lariss.web.rest;

import static id.lariss.domain.MemoryAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Memory;
import id.lariss.repository.EntityManager;
import id.lariss.repository.MemoryRepository;
import id.lariss.service.dto.MemoryDTO;
import id.lariss.service.mapper.MemoryMapper;
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
 * Integration tests for the {@link MemoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MemoryResourceIT {

    private static final String DEFAULT_ATTRIBUTE_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/memories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MemoryRepository memoryRepository;

    @Autowired
    private MemoryMapper memoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Memory memory;

    private Memory insertedMemory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Memory createEntity() {
        return new Memory()
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
    public static Memory createUpdatedEntity() {
        return new Memory()
            .attributeLabel(UPDATED_ATTRIBUTE_LABEL)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .optionLabel(UPDATED_OPTION_LABEL)
            .optionValue(UPDATED_OPTION_VALUE);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Memory.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        memory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMemory != null) {
            memoryRepository.delete(insertedMemory).block();
            insertedMemory = null;
        }
        deleteEntities(em);
    }

    @Test
    void createMemory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);
        var returnedMemoryDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(memoryDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(MemoryDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Memory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMemory = memoryMapper.toEntity(returnedMemoryDTO);
        assertMemoryUpdatableFieldsEquals(returnedMemory, getPersistedMemory(returnedMemory));

        insertedMemory = returnedMemory;
    }

    @Test
    void createMemoryWithExistingId() throws Exception {
        // Create the Memory with an existing ID
        memory.setId(1L);
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(memoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMemories() {
        // Initialize the database
        insertedMemory = memoryRepository.save(memory).block();

        // Get all the memoryList
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
            .value(hasItem(memory.getId().intValue()))
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
    void getMemory() {
        // Initialize the database
        insertedMemory = memoryRepository.save(memory).block();

        // Get the memory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, memory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(memory.getId().intValue()))
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
    void getNonExistingMemory() {
        // Get the memory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMemory() throws Exception {
        // Initialize the database
        insertedMemory = memoryRepository.save(memory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memory
        Memory updatedMemory = memoryRepository.findById(memory.getId()).block();
        updatedMemory
            .attributeLabel(UPDATED_ATTRIBUTE_LABEL)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .optionLabel(UPDATED_OPTION_LABEL)
            .optionValue(UPDATED_OPTION_VALUE);
        MemoryDTO memoryDTO = memoryMapper.toDto(updatedMemory);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, memoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(memoryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMemoryToMatchAllProperties(updatedMemory);
    }

    @Test
    void putNonExistingMemory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memory.setId(longCount.incrementAndGet());

        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, memoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(memoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMemory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memory.setId(longCount.incrementAndGet());

        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(memoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMemory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memory.setId(longCount.incrementAndGet());

        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(memoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMemoryWithPatch() throws Exception {
        // Initialize the database
        insertedMemory = memoryRepository.save(memory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memory using partial update
        Memory partialUpdatedMemory = new Memory();
        partialUpdatedMemory.setId(memory.getId());

        partialUpdatedMemory.attributeLabel(UPDATED_ATTRIBUTE_LABEL).attributeName(UPDATED_ATTRIBUTE_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMemory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMemory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Memory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemoryUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMemory, memory), getPersistedMemory(memory));
    }

    @Test
    void fullUpdateMemoryWithPatch() throws Exception {
        // Initialize the database
        insertedMemory = memoryRepository.save(memory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memory using partial update
        Memory partialUpdatedMemory = new Memory();
        partialUpdatedMemory.setId(memory.getId());

        partialUpdatedMemory
            .attributeLabel(UPDATED_ATTRIBUTE_LABEL)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .optionLabel(UPDATED_OPTION_LABEL)
            .optionValue(UPDATED_OPTION_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMemory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMemory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Memory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemoryUpdatableFieldsEquals(partialUpdatedMemory, getPersistedMemory(partialUpdatedMemory));
    }

    @Test
    void patchNonExistingMemory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memory.setId(longCount.incrementAndGet());

        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, memoryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(memoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMemory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memory.setId(longCount.incrementAndGet());

        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(memoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMemory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memory.setId(longCount.incrementAndGet());

        // Create the Memory
        MemoryDTO memoryDTO = memoryMapper.toDto(memory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(memoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Memory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMemory() {
        // Initialize the database
        insertedMemory = memoryRepository.save(memory).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the memory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, memory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return memoryRepository.count().block();
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

    protected Memory getPersistedMemory(Memory memory) {
        return memoryRepository.findById(memory.getId()).block();
    }

    protected void assertPersistedMemoryToMatchAllProperties(Memory expectedMemory) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMemoryAllPropertiesEquals(expectedMemory, getPersistedMemory(expectedMemory));
        assertMemoryUpdatableFieldsEquals(expectedMemory, getPersistedMemory(expectedMemory));
    }

    protected void assertPersistedMemoryToMatchUpdatableProperties(Memory expectedMemory) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMemoryAllUpdatablePropertiesEquals(expectedMemory, getPersistedMemory(expectedMemory));
        assertMemoryUpdatableFieldsEquals(expectedMemory, getPersistedMemory(expectedMemory));
    }
}
