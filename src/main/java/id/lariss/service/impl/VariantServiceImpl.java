package id.lariss.service.impl;

import id.lariss.repository.VariantRepository;
import id.lariss.service.VariantService;
import id.lariss.service.dto.VariantDTO;
import id.lariss.service.mapper.VariantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link id.lariss.domain.Variant}.
 */
@Service
@Transactional
public class VariantServiceImpl implements VariantService {

    private static final Logger LOG = LoggerFactory.getLogger(VariantServiceImpl.class);

    private final VariantRepository variantRepository;

    private final VariantMapper variantMapper;

    public VariantServiceImpl(VariantRepository variantRepository, VariantMapper variantMapper) {
        this.variantRepository = variantRepository;
        this.variantMapper = variantMapper;
    }

    @Override
    public Mono<VariantDTO> save(VariantDTO variantDTO) {
        LOG.debug("Request to save Variant : {}", variantDTO);
        return variantRepository.save(variantMapper.toEntity(variantDTO)).map(variantMapper::toDto);
    }

    @Override
    public Mono<VariantDTO> update(VariantDTO variantDTO) {
        LOG.debug("Request to update Variant : {}", variantDTO);
        return variantRepository.save(variantMapper.toEntity(variantDTO)).map(variantMapper::toDto);
    }

    @Override
    public Mono<VariantDTO> partialUpdate(VariantDTO variantDTO) {
        LOG.debug("Request to partially update Variant : {}", variantDTO);

        return variantRepository
            .findById(variantDTO.getId())
            .map(existingVariant -> {
                variantMapper.partialUpdate(existingVariant, variantDTO);

                return existingVariant;
            })
            .flatMap(variantRepository::save)
            .map(variantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<VariantDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Variants");
        return variantRepository.findAllBy(pageable).map(variantMapper::toDto);
    }

    public Flux<VariantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return variantRepository.findAllWithEagerRelationships(pageable).map(variantMapper::toDto);
    }

    public Mono<Long> countAll() {
        return variantRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<VariantDTO> findOne(Long id) {
        LOG.debug("Request to get Variant : {}", id);
        return variantRepository.findOneWithEagerRelationships(id).map(variantMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Variant : {}", id);
        return variantRepository.deleteById(id);
    }
}
