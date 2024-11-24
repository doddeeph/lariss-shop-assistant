package id.lariss.service.impl;

import id.lariss.repository.StorageRepository;
import id.lariss.service.StorageService;
import id.lariss.service.dto.StorageDTO;
import id.lariss.service.mapper.StorageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link id.lariss.domain.Storage}.
 */
@Service
@Transactional
public class StorageServiceImpl implements StorageService {

    private static final Logger LOG = LoggerFactory.getLogger(StorageServiceImpl.class);

    private final StorageRepository storageRepository;

    private final StorageMapper storageMapper;

    public StorageServiceImpl(StorageRepository storageRepository, StorageMapper storageMapper) {
        this.storageRepository = storageRepository;
        this.storageMapper = storageMapper;
    }

    @Override
    public Mono<StorageDTO> save(StorageDTO storageDTO) {
        LOG.debug("Request to save Storage : {}", storageDTO);
        return storageRepository.save(storageMapper.toEntity(storageDTO)).map(storageMapper::toDto);
    }

    @Override
    public Mono<StorageDTO> update(StorageDTO storageDTO) {
        LOG.debug("Request to update Storage : {}", storageDTO);
        return storageRepository.save(storageMapper.toEntity(storageDTO)).map(storageMapper::toDto);
    }

    @Override
    public Mono<StorageDTO> partialUpdate(StorageDTO storageDTO) {
        LOG.debug("Request to partially update Storage : {}", storageDTO);

        return storageRepository
            .findById(storageDTO.getId())
            .map(existingStorage -> {
                storageMapper.partialUpdate(existingStorage, storageDTO);

                return existingStorage;
            })
            .flatMap(storageRepository::save)
            .map(storageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<StorageDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Storages");
        return storageRepository.findAllBy(pageable).map(storageMapper::toDto);
    }

    public Mono<Long> countAll() {
        return storageRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<StorageDTO> findOne(Long id) {
        LOG.debug("Request to get Storage : {}", id);
        return storageRepository.findById(id).map(storageMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Storage : {}", id);
        return storageRepository.deleteById(id);
    }
}
