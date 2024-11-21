package id.lariss.web.rest;

import static id.lariss.domain.ProductAsserts.*;
import static id.lariss.web.rest.TestUtil.createUpdateProxyForBean;
import static id.lariss.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.lariss.IntegrationTest;
import id.lariss.domain.Product;
import id.lariss.domain.enumeration.Color;
import id.lariss.domain.enumeration.Currency;
import id.lariss.domain.enumeration.DiscountType;
import id.lariss.domain.enumeration.Memory;
import id.lariss.domain.enumeration.Processor;
import id.lariss.domain.enumeration.Storage;
import id.lariss.repository.EntityManager;
import id.lariss.repository.ProductRepository;
import id.lariss.service.ProductService;
import id.lariss.service.dto.ProductDTO;
import id.lariss.service.mapper.ProductMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BASE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BASE_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DISCOUNT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DISCOUNT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT_AMOUNT = new BigDecimal(2);

    private static final DiscountType DEFAULT_DISCOUNT_TYPE = DiscountType.PERCENTAGE;
    private static final DiscountType UPDATED_DISCOUNT_TYPE = DiscountType.AMOUNT;

    private static final Currency DEFAULT_CURRENCY = Currency.IDR;
    private static final Currency UPDATED_CURRENCY = Currency.USD;

    private static final Color DEFAULT_COLOR = Color.MIDNIGHT;
    private static final Color UPDATED_COLOR = Color.SPACE_GREY;

    private static final Processor DEFAULT_PROCESSOR = Processor.APPLE_M2_8CPU_8GPU;
    private static final Processor UPDATED_PROCESSOR = Processor.APPLE_M3_8CPU_8GPU;

    private static final Memory DEFAULT_MEMORY = Memory.MEMORY_16GB;
    private static final Memory UPDATED_MEMORY = Memory.MEMORY_24GB;

    private static final Storage DEFAULT_STORAGE = Storage.STORAGE_256GB;
    private static final Storage UPDATED_STORAGE = Storage.STORAGE_512GB;

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private ProductRepository productRepositoryMock;

    @Autowired
    private ProductMapper productMapper;

    @Mock
    private ProductService productServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Product product;

    private Product insertedProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity() {
        return new Product()
            .name(DEFAULT_NAME)
            .sku(DEFAULT_SKU)
            .basePrice(DEFAULT_BASE_PRICE)
            .discountPrice(DEFAULT_DISCOUNT_PRICE)
            .discountAmount(DEFAULT_DISCOUNT_AMOUNT)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .currency(DEFAULT_CURRENCY)
            .color(DEFAULT_COLOR)
            .processor(DEFAULT_PROCESSOR)
            .memory(DEFAULT_MEMORY)
            .storage(DEFAULT_STORAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity() {
        return new Product()
            .name(UPDATED_NAME)
            .sku(UPDATED_SKU)
            .basePrice(UPDATED_BASE_PRICE)
            .discountPrice(UPDATED_DISCOUNT_PRICE)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .currency(UPDATED_CURRENCY)
            .color(UPDATED_COLOR)
            .processor(UPDATED_PROCESSOR)
            .memory(UPDATED_MEMORY)
            .storage(UPDATED_STORAGE);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Product.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        product = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProduct != null) {
            productRepository.delete(insertedProduct).block();
            insertedProduct = null;
        }
        deleteEntities(em);
    }

    @Test
    void createProduct() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        var returnedProductDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ProductDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Product in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProduct = productMapper.toEntity(returnedProductDTO);
        assertProductUpdatableFieldsEquals(returnedProduct, getPersistedProduct(returnedProduct));

        insertedProduct = returnedProduct;
    }

    @Test
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkSkuIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setSku(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkBasePriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setBasePrice(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCurrencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setCurrency(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkColorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setColor(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkProcessorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setProcessor(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkMemoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setMemory(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkStorageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setStorage(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllProducts() {
        // Initialize the database
        insertedProduct = productRepository.save(product).block();

        // Get all the productList
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
            .value(hasItem(product.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].sku")
            .value(hasItem(DEFAULT_SKU))
            .jsonPath("$.[*].basePrice")
            .value(hasItem(sameNumber(DEFAULT_BASE_PRICE)))
            .jsonPath("$.[*].discountPrice")
            .value(hasItem(sameNumber(DEFAULT_DISCOUNT_PRICE)))
            .jsonPath("$.[*].discountAmount")
            .value(hasItem(sameNumber(DEFAULT_DISCOUNT_AMOUNT)))
            .jsonPath("$.[*].discountType")
            .value(hasItem(DEFAULT_DISCOUNT_TYPE.toString()))
            .jsonPath("$.[*].currency")
            .value(hasItem(DEFAULT_CURRENCY.toString()))
            .jsonPath("$.[*].color")
            .value(hasItem(DEFAULT_COLOR.toString()))
            .jsonPath("$.[*].processor")
            .value(hasItem(DEFAULT_PROCESSOR.toString()))
            .jsonPath("$.[*].memory")
            .value(hasItem(DEFAULT_MEMORY.toString()))
            .jsonPath("$.[*].storage")
            .value(hasItem(DEFAULT_STORAGE.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsEnabled() {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsNotEnabled() {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(productRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getProduct() {
        // Initialize the database
        insertedProduct = productRepository.save(product).block();

        // Get the product
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, product.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(product.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.sku")
            .value(is(DEFAULT_SKU))
            .jsonPath("$.basePrice")
            .value(is(sameNumber(DEFAULT_BASE_PRICE)))
            .jsonPath("$.discountPrice")
            .value(is(sameNumber(DEFAULT_DISCOUNT_PRICE)))
            .jsonPath("$.discountAmount")
            .value(is(sameNumber(DEFAULT_DISCOUNT_AMOUNT)))
            .jsonPath("$.discountType")
            .value(is(DEFAULT_DISCOUNT_TYPE.toString()))
            .jsonPath("$.currency")
            .value(is(DEFAULT_CURRENCY.toString()))
            .jsonPath("$.color")
            .value(is(DEFAULT_COLOR.toString()))
            .jsonPath("$.processor")
            .value(is(DEFAULT_PROCESSOR.toString()))
            .jsonPath("$.memory")
            .value(is(DEFAULT_MEMORY.toString()))
            .jsonPath("$.storage")
            .value(is(DEFAULT_STORAGE.toString()));
    }

    @Test
    void getNonExistingProduct() {
        // Get the product
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.save(product).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).block();
        updatedProduct
            .name(UPDATED_NAME)
            .sku(UPDATED_SKU)
            .basePrice(UPDATED_BASE_PRICE)
            .discountPrice(UPDATED_DISCOUNT_PRICE)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .currency(UPDATED_CURRENCY)
            .color(UPDATED_COLOR)
            .processor(UPDATED_PROCESSOR)
            .memory(UPDATED_MEMORY)
            .storage(UPDATED_STORAGE);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, productDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductToMatchAllProperties(updatedProduct);
    }

    @Test
    void putNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, productDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.save(product).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .name(UPDATED_NAME)
            .sku(UPDATED_SKU)
            .discountPrice(UPDATED_DISCOUNT_PRICE)
            .currency(UPDATED_CURRENCY)
            .color(UPDATED_COLOR)
            .storage(UPDATED_STORAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedProduct))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProduct, product), getPersistedProduct(product));
    }

    @Test
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.save(product).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .name(UPDATED_NAME)
            .sku(UPDATED_SKU)
            .basePrice(UPDATED_BASE_PRICE)
            .discountPrice(UPDATED_DISCOUNT_PRICE)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .currency(UPDATED_CURRENCY)
            .color(UPDATED_COLOR)
            .processor(UPDATED_PROCESSOR)
            .memory(UPDATED_MEMORY)
            .storage(UPDATED_STORAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedProduct))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(partialUpdatedProduct, getPersistedProduct(partialUpdatedProduct));
    }

    @Test
    void patchNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, productDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(productDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProduct() {
        // Initialize the database
        insertedProduct = productRepository.save(product).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the product
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, product.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productRepository.count().block();
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

    protected Product getPersistedProduct(Product product) {
        return productRepository.findById(product.getId()).block();
    }

    protected void assertPersistedProductToMatchAllProperties(Product expectedProduct) {
        // Test fails because reactive api returns an empty object instead of null
        // assertProductAllPropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
        assertProductUpdatableFieldsEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }

    protected void assertPersistedProductToMatchUpdatableProperties(Product expectedProduct) {
        // Test fails because reactive api returns an empty object instead of null
        // assertProductAllUpdatablePropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
        assertProductUpdatableFieldsEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }
}
