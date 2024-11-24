package id.lariss.web.rest;

import static id.lariss.domain.ProcessorAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Processor;
import id.lariss.repository.EntityManager;
import id.lariss.repository.ProcessorRepository;
import id.lariss.service.dto.ProcessorDTO;
import id.lariss.service.mapper.ProcessorMapper;
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
 * Integration tests for the {@link ProcessorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProcessorResourceIT {

    private static final String DEFAULT_ATTRIBUTE_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/processors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProcessorRepository processorRepository;

    @Autowired
    private ProcessorMapper processorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Processor processor;

    private Processor insertedProcessor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Processor createEntity() {
        return new Processor()
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
    public static Processor createUpdatedEntity() {
        return new Processor()
            .attributeLabel(UPDATED_ATTRIBUTE_LABEL)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .optionLabel(UPDATED_OPTION_LABEL)
            .optionValue(UPDATED_OPTION_VALUE);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Processor.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        processor = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProcessor != null) {
            processorRepository.delete(insertedProcessor).block();
            insertedProcessor = null;
        }
        deleteEntities(em);
    }

    @Test
    void createProcessor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Processor
        ProcessorDTO processorDTO = processorMapper.toDto(processor);
        var returnedProcessorDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(processorDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ProcessorDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Processor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProcessor = processorMapper.toEntity(returnedProcessorDTO);
        assertProcessorUpdatableFieldsEquals(returnedProcessor, getPersistedProcessor(returnedProcessor));

        insertedProcessor = returnedProcessor;
    }

    @Test
    void createProcessorWithExistingId() throws Exception {
        // Create the Processor with an existing ID
        processor.setId(1L);
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(processorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllProcessors() {
        // Initialize the database
        insertedProcessor = processorRepository.save(processor).block();

        // Get all the processorList
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
            .value(hasItem(processor.getId().intValue()))
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
    void getProcessor() {
        // Initialize the database
        insertedProcessor = processorRepository.save(processor).block();

        // Get the processor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, processor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(processor.getId().intValue()))
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
    void getNonExistingProcessor() {
        // Get the processor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingProcessor() throws Exception {
        // Initialize the database
        insertedProcessor = processorRepository.save(processor).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the processor
        Processor updatedProcessor = processorRepository.findById(processor.getId()).block();
        updatedProcessor
            .attributeLabel(UPDATED_ATTRIBUTE_LABEL)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .optionLabel(UPDATED_OPTION_LABEL)
            .optionValue(UPDATED_OPTION_VALUE);
        ProcessorDTO processorDTO = processorMapper.toDto(updatedProcessor);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, processorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(processorDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProcessorToMatchAllProperties(updatedProcessor);
    }

    @Test
    void putNonExistingProcessor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processor.setId(longCount.incrementAndGet());

        // Create the Processor
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, processorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(processorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProcessor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processor.setId(longCount.incrementAndGet());

        // Create the Processor
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(processorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProcessor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processor.setId(longCount.incrementAndGet());

        // Create the Processor
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(processorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProcessorWithPatch() throws Exception {
        // Initialize the database
        insertedProcessor = processorRepository.save(processor).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the processor using partial update
        Processor partialUpdatedProcessor = new Processor();
        partialUpdatedProcessor.setId(processor.getId());

        partialUpdatedProcessor.optionValue(UPDATED_OPTION_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProcessor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedProcessor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Processor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProcessorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProcessor, processor),
            getPersistedProcessor(processor)
        );
    }

    @Test
    void fullUpdateProcessorWithPatch() throws Exception {
        // Initialize the database
        insertedProcessor = processorRepository.save(processor).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the processor using partial update
        Processor partialUpdatedProcessor = new Processor();
        partialUpdatedProcessor.setId(processor.getId());

        partialUpdatedProcessor
            .attributeLabel(UPDATED_ATTRIBUTE_LABEL)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .optionLabel(UPDATED_OPTION_LABEL)
            .optionValue(UPDATED_OPTION_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProcessor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedProcessor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Processor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProcessorUpdatableFieldsEquals(partialUpdatedProcessor, getPersistedProcessor(partialUpdatedProcessor));
    }

    @Test
    void patchNonExistingProcessor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processor.setId(longCount.incrementAndGet());

        // Create the Processor
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, processorDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(processorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProcessor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processor.setId(longCount.incrementAndGet());

        // Create the Processor
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(processorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProcessor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        processor.setId(longCount.incrementAndGet());

        // Create the Processor
        ProcessorDTO processorDTO = processorMapper.toDto(processor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(processorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Processor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProcessor() {
        // Initialize the database
        insertedProcessor = processorRepository.save(processor).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the processor
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, processor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return processorRepository.count().block();
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

    protected Processor getPersistedProcessor(Processor processor) {
        return processorRepository.findById(processor.getId()).block();
    }

    protected void assertPersistedProcessorToMatchAllProperties(Processor expectedProcessor) {
        // Test fails because reactive api returns an empty object instead of null
        // assertProcessorAllPropertiesEquals(expectedProcessor, getPersistedProcessor(expectedProcessor));
        assertProcessorUpdatableFieldsEquals(expectedProcessor, getPersistedProcessor(expectedProcessor));
    }

    protected void assertPersistedProcessorToMatchUpdatableProperties(Processor expectedProcessor) {
        // Test fails because reactive api returns an empty object instead of null
        // assertProcessorAllUpdatablePropertiesEquals(expectedProcessor, getPersistedProcessor(expectedProcessor));
        assertProcessorUpdatableFieldsEquals(expectedProcessor, getPersistedProcessor(expectedProcessor));
    }
}
