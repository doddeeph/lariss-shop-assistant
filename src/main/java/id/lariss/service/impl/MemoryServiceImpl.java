package id.lariss.service.impl;

import id.lariss.repository.MemoryRepository;
import id.lariss.service.MemoryService;
import id.lariss.service.dto.MemoryDTO;
import id.lariss.service.mapper.MemoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link id.lariss.domain.Memory}.
 */
@Service
@Transactional
public class MemoryServiceImpl implements MemoryService {

    private static final Logger LOG = LoggerFactory.getLogger(MemoryServiceImpl.class);

    private final MemoryRepository memoryRepository;

    private final MemoryMapper memoryMapper;

    public MemoryServiceImpl(MemoryRepository memoryRepository, MemoryMapper memoryMapper) {
        this.memoryRepository = memoryRepository;
        this.memoryMapper = memoryMapper;
    }

    @Override
    public Mono<MemoryDTO> save(MemoryDTO memoryDTO) {
        LOG.debug("Request to save Memory : {}", memoryDTO);
        return memoryRepository.save(memoryMapper.toEntity(memoryDTO)).map(memoryMapper::toDto);
    }

    @Override
    public Mono<MemoryDTO> update(MemoryDTO memoryDTO) {
        LOG.debug("Request to update Memory : {}", memoryDTO);
        return memoryRepository.save(memoryMapper.toEntity(memoryDTO)).map(memoryMapper::toDto);
    }

    @Override
    public Mono<MemoryDTO> partialUpdate(MemoryDTO memoryDTO) {
        LOG.debug("Request to partially update Memory : {}", memoryDTO);

        return memoryRepository
            .findById(memoryDTO.getId())
            .map(existingMemory -> {
                memoryMapper.partialUpdate(existingMemory, memoryDTO);

                return existingMemory;
            })
            .flatMap(memoryRepository::save)
            .map(memoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MemoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Memories");
        return memoryRepository.findAllBy(pageable).map(memoryMapper::toDto);
    }

    public Mono<Long> countAll() {
        return memoryRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MemoryDTO> findOne(Long id) {
        LOG.debug("Request to get Memory : {}", id);
        return memoryRepository.findById(id).map(memoryMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Memory : {}", id);
        return memoryRepository.deleteById(id);
    }
}
