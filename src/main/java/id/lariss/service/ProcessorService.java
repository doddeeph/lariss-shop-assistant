package id.lariss.service;

import id.lariss.service.dto.ProcessorDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link id.lariss.domain.Processor}.
 */
public interface ProcessorService {
    /**
     * Save a processor.
     *
     * @param processorDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ProcessorDTO> save(ProcessorDTO processorDTO);

    /**
     * Updates a processor.
     *
     * @param processorDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ProcessorDTO> update(ProcessorDTO processorDTO);

    /**
     * Partially updates a processor.
     *
     * @param processorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ProcessorDTO> partialUpdate(ProcessorDTO processorDTO);

    /**
     * Get all the processors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ProcessorDTO> findAll(Pageable pageable);

    /**
     * Returns the number of processors available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" processor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ProcessorDTO> findOne(Long id);

    /**
     * Delete the "id" processor.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
