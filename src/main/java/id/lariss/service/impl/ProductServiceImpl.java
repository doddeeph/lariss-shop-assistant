package id.lariss.service.impl;

import id.lariss.config.ProductFunctionConfiguration.ProductDetails;
import id.lariss.domain.*;
import id.lariss.repository.ProductRepository;
import id.lariss.service.*;
import id.lariss.service.dto.*;
import id.lariss.service.mapper.ProductMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;

/**
 * Service Implementation for managing {@link id.lariss.domain.Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final DescriptionService descriptionService;

    private final FeatureService featureService;

    private final BoxContentService contentService;

    private final WarrantyService warrantyService;

    public ProductServiceImpl(
        ProductRepository productRepository,
        ProductMapper productMapper,
        DescriptionService descriptionService,
        FeatureService featureService,
        BoxContentService contentService,
        WarrantyService warrantyService
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.descriptionService = descriptionService;
        this.featureService = featureService;
        this.contentService = contentService;
        this.warrantyService = warrantyService;
    }

    @Override
    public Mono<ProductDTO> save(ProductDTO productDTO) {
        LOG.debug("Request to save Product : {}", productDTO);
        return productRepository.save(productMapper.toEntity(productDTO)).map(productMapper::toDto);
    }

    @Override
    public Mono<ProductDTO> update(ProductDTO productDTO) {
        LOG.debug("Request to update Product : {}", productDTO);
        return productRepository.save(productMapper.toEntity(productDTO)).map(productMapper::toDto);
    }

    @Override
    public Mono<ProductDTO> partialUpdate(ProductDTO productDTO) {
        LOG.debug("Request to partially update Product : {}", productDTO);

        return productRepository
            .findById(productDTO.getId())
            .map(existingProduct -> {
                productMapper.partialUpdate(existingProduct, productDTO);

                return existingProduct;
            })
            .flatMap(productRepository::save)
            .map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ProductDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Products");
        return productRepository.findAllBy(pageable).map(productMapper::toDto);
    }

    public Flux<ProductDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productRepository.findAllWithEagerRelationships(pageable).map(productMapper::toDto);
    }

    public Mono<Long> countAll() {
        return productRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ProductDTO> findOne(Long id) {
        LOG.debug("Request to get Product : {}", id);
        return productRepository.findOneWithEagerRelationships(id).map(productMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Product : {}", id);
        return productRepository.deleteById(id);
    }

    @Override
    public Mono<ProductDetails> findByCategory(String category) {
        return productRepository
            .findByCategory(category)
            .flatMap(product ->
                getNeededData(product).map(tuple ->
                    new ProductDetails(
                        product.getName(),
                        product.getColor().getValue(),
                        product.getProcessor().getValue(),
                        product.getMemory().getValue(),
                        product.getStorage().getValue(),
                        tuple.getT1(),
                        tuple.getT2(),
                        tuple.getT3(),
                        tuple.getT4()
                    )
                )
            )
            .elementAt(0, new ProductDetails("Not Found", null, null, null, null, null, null, null, null))
            .doOnError(t -> LOG.error("Error findByCategory -> category: {}", category));
    }

    private Mono<Tuple4<String, String, String, String>> getNeededData(Product product) {
        return Mono.zip(
            descriptionService.findOne(product.getDescriptionId()).map(DescriptionDTO::getDescriptionEn),
            featureService.findOne(product.getFeatureId()).map(FeatureDTO::getFeatureEn),
            contentService.findOne(product.getBoxContentId()).map(BoxContentDTO::getContentEn),
            warrantyService.findOne(product.getWarrantyId()).map(WarrantyDTO::getWarrantyEn)
        );
    }
}
