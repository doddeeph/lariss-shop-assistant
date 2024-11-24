package id.lariss.service.impl;

import id.lariss.repository.ProcessorRepository;
import id.lariss.service.ProcessorService;
import id.lariss.service.dto.ProcessorDTO;
import id.lariss.service.mapper.ProcessorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link id.lariss.domain.Processor}.
 */
@Service
@Transactional
public class ProcessorServiceImpl implements ProcessorService {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessorServiceImpl.class);

    private final ProcessorRepository processorRepository;

    private final ProcessorMapper processorMapper;

    public ProcessorServiceImpl(ProcessorRepository processorRepository, ProcessorMapper processorMapper) {
        this.processorRepository = processorRepository;
        this.processorMapper = processorMapper;
    }

    @Override
    public Mono<ProcessorDTO> save(ProcessorDTO processorDTO) {
        LOG.debug("Request to save Processor : {}", processorDTO);
        return processorRepository.save(processorMapper.toEntity(processorDTO)).map(processorMapper::toDto);
    }

    @Override
    public Mono<ProcessorDTO> update(ProcessorDTO processorDTO) {
        LOG.debug("Request to update Processor : {}", processorDTO);
        return processorRepository.save(processorMapper.toEntity(processorDTO)).map(processorMapper::toDto);
    }

    @Override
    public Mono<ProcessorDTO> partialUpdate(ProcessorDTO processorDTO) {
        LOG.debug("Request to partially update Processor : {}", processorDTO);

        return processorRepository
            .findById(processorDTO.getId())
            .map(existingProcessor -> {
                processorMapper.partialUpdate(existingProcessor, processorDTO);

                return existingProcessor;
            })
            .flatMap(processorRepository::save)
            .map(processorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ProcessorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Processors");
        return processorRepository.findAllBy(pageable).map(processorMapper::toDto);
    }

    public Mono<Long> countAll() {
        return processorRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ProcessorDTO> findOne(Long id) {
        LOG.debug("Request to get Processor : {}", id);
        return processorRepository.findById(id).map(processorMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Processor : {}", id);
        return processorRepository.deleteById(id);
    }
}
