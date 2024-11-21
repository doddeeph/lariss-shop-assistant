package id.lariss.service.impl;

import id.lariss.repository.DescriptionRepository;
import id.lariss.service.DescriptionService;
import id.lariss.service.dto.DescriptionDTO;
import id.lariss.service.mapper.DescriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link id.lariss.domain.Description}.
 */
@Service
@Transactional
public class DescriptionServiceImpl implements DescriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(DescriptionServiceImpl.class);

    private final DescriptionRepository descriptionRepository;

    private final DescriptionMapper descriptionMapper;

    public DescriptionServiceImpl(DescriptionRepository descriptionRepository, DescriptionMapper descriptionMapper) {
        this.descriptionRepository = descriptionRepository;
        this.descriptionMapper = descriptionMapper;
    }

    @Override
    public Mono<DescriptionDTO> save(DescriptionDTO descriptionDTO) {
        LOG.debug("Request to save Description : {}", descriptionDTO);
        return descriptionRepository.save(descriptionMapper.toEntity(descriptionDTO)).map(descriptionMapper::toDto);
    }

    @Override
    public Mono<DescriptionDTO> update(DescriptionDTO descriptionDTO) {
        LOG.debug("Request to update Description : {}", descriptionDTO);
        return descriptionRepository.save(descriptionMapper.toEntity(descriptionDTO)).map(descriptionMapper::toDto);
    }

    @Override
    public Mono<DescriptionDTO> partialUpdate(DescriptionDTO descriptionDTO) {
        LOG.debug("Request to partially update Description : {}", descriptionDTO);

        return descriptionRepository
            .findById(descriptionDTO.getId())
            .map(existingDescription -> {
                descriptionMapper.partialUpdate(existingDescription, descriptionDTO);

                return existingDescription;
            })
            .flatMap(descriptionRepository::save)
            .map(descriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<DescriptionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Descriptions");
        return descriptionRepository.findAllBy(pageable).map(descriptionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return descriptionRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<DescriptionDTO> findOne(Long id) {
        LOG.debug("Request to get Description : {}", id);
        return descriptionRepository.findById(id).map(descriptionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Description : {}", id);
        return descriptionRepository.deleteById(id);
    }
}
