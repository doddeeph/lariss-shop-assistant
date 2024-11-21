package id.lariss.service.impl;

import id.lariss.repository.FeatureRepository;
import id.lariss.service.FeatureService;
import id.lariss.service.dto.FeatureDTO;
import id.lariss.service.mapper.FeatureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link id.lariss.domain.Feature}.
 */
@Service
@Transactional
public class FeatureServiceImpl implements FeatureService {

    private static final Logger LOG = LoggerFactory.getLogger(FeatureServiceImpl.class);

    private final FeatureRepository featureRepository;

    private final FeatureMapper featureMapper;

    public FeatureServiceImpl(FeatureRepository featureRepository, FeatureMapper featureMapper) {
        this.featureRepository = featureRepository;
        this.featureMapper = featureMapper;
    }

    @Override
    public Mono<FeatureDTO> save(FeatureDTO featureDTO) {
        LOG.debug("Request to save Feature : {}", featureDTO);
        return featureRepository.save(featureMapper.toEntity(featureDTO)).map(featureMapper::toDto);
    }

    @Override
    public Mono<FeatureDTO> update(FeatureDTO featureDTO) {
        LOG.debug("Request to update Feature : {}", featureDTO);
        return featureRepository.save(featureMapper.toEntity(featureDTO)).map(featureMapper::toDto);
    }

    @Override
    public Mono<FeatureDTO> partialUpdate(FeatureDTO featureDTO) {
        LOG.debug("Request to partially update Feature : {}", featureDTO);

        return featureRepository
            .findById(featureDTO.getId())
            .map(existingFeature -> {
                featureMapper.partialUpdate(existingFeature, featureDTO);

                return existingFeature;
            })
            .flatMap(featureRepository::save)
            .map(featureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<FeatureDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Features");
        return featureRepository.findAllBy(pageable).map(featureMapper::toDto);
    }

    public Mono<Long> countAll() {
        return featureRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<FeatureDTO> findOne(Long id) {
        LOG.debug("Request to get Feature : {}", id);
        return featureRepository.findById(id).map(featureMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Feature : {}", id);
        return featureRepository.deleteById(id);
    }
}
