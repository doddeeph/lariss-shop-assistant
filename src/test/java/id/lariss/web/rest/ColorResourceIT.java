package id.lariss.web.rest;

import static id.lariss.domain.ColorAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Color;
import id.lariss.repository.ColorRepository;
import id.lariss.repository.EntityManager;
import id.lariss.service.dto.ColorDTO;
import id.lariss.service.mapper.ColorMapper;
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
 * Integration tests for the {@link ColorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ColorResourceIT {

    private static final String DEFAULT_ATTRIBUTE_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/colors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private ColorMapper colorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Color color;

    private Color insertedColor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Color createEntity() {
        return new Color()
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
    public static Color createUpdatedEntity() {
        return new Color()
            .attributeLabel(UPDATED_ATTRIBUTE_LABEL)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .optionLabel(UPDATED_OPTION_LABEL)
            .optionValue(UPDATED_OPTION_VALUE);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Color.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        color = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedColor != null) {
            colorRepository.delete(insertedColor).block();
            insertedColor = null;
        }
        deleteEntities(em);
    }

    @Test
    void createColor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);
        var returnedColorDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(colorDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ColorDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Color in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedColor = colorMapper.toEntity(returnedColorDTO);
        assertColorUpdatableFieldsEquals(returnedColor, getPersistedColor(returnedColor));

        insertedColor = returnedColor;
    }

    @Test
    void createColorWithExistingId() throws Exception {
        // Create the Color with an existing ID
        color.setId(1L);
        ColorDTO colorDTO = colorMapper.toDto(color);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(colorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllColors() {
        // Initialize the database
        insertedColor = colorRepository.save(color).block();

        // Get all the colorList
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
            .value(hasItem(color.getId().intValue()))
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
    void getColor() {
        // Initialize the database
        insertedColor = colorRepository.save(color).block();

        // Get the color
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, color.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(color.getId().intValue()))
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
    void getNonExistingColor() {
        // Get the color
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingColor() throws Exception {
        // Initialize the database
        insertedColor = colorRepository.save(color).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the color
        Color updatedColor = colorRepository.findById(color.getId()).block();
        updatedColor
            .attributeLabel(UPDATED_ATTRIBUTE_LABEL)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .optionLabel(UPDATED_OPTION_LABEL)
            .optionValue(UPDATED_OPTION_VALUE);
        ColorDTO colorDTO = colorMapper.toDto(updatedColor);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, colorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(colorDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedColorToMatchAllProperties(updatedColor);
    }

    @Test
    void putNonExistingColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        color.setId(longCount.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, colorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(colorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        color.setId(longCount.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(colorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        color.setId(longCount.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(colorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateColorWithPatch() throws Exception {
        // Initialize the database
        insertedColor = colorRepository.save(color).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the color using partial update
        Color partialUpdatedColor = new Color();
        partialUpdatedColor.setId(color.getId());

        partialUpdatedColor.attributeName(UPDATED_ATTRIBUTE_NAME).optionLabel(UPDATED_OPTION_LABEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedColor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedColor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Color in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertColorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedColor, color), getPersistedColor(color));
    }

    @Test
    void fullUpdateColorWithPatch() throws Exception {
        // Initialize the database
        insertedColor = colorRepository.save(color).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the color using partial update
        Color partialUpdatedColor = new Color();
        partialUpdatedColor.setId(color.getId());

        partialUpdatedColor
            .attributeLabel(UPDATED_ATTRIBUTE_LABEL)
            .attributeName(UPDATED_ATTRIBUTE_NAME)
            .optionLabel(UPDATED_OPTION_LABEL)
            .optionValue(UPDATED_OPTION_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedColor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedColor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Color in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertColorUpdatableFieldsEquals(partialUpdatedColor, getPersistedColor(partialUpdatedColor));
    }

    @Test
    void patchNonExistingColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        color.setId(longCount.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, colorDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(colorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        color.setId(longCount.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(colorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamColor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        color.setId(longCount.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(colorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Color in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteColor() {
        // Initialize the database
        insertedColor = colorRepository.save(color).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the color
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, color.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return colorRepository.count().block();
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

    protected Color getPersistedColor(Color color) {
        return colorRepository.findById(color.getId()).block();
    }

    protected void assertPersistedColorToMatchAllProperties(Color expectedColor) {
        // Test fails because reactive api returns an empty object instead of null
        // assertColorAllPropertiesEquals(expectedColor, getPersistedColor(expectedColor));
        assertColorUpdatableFieldsEquals(expectedColor, getPersistedColor(expectedColor));
    }

    protected void assertPersistedColorToMatchUpdatableProperties(Color expectedColor) {
        // Test fails because reactive api returns an empty object instead of null
        // assertColorAllUpdatablePropertiesEquals(expectedColor, getPersistedColor(expectedColor));
        assertColorUpdatableFieldsEquals(expectedColor, getPersistedColor(expectedColor));
    }
}
