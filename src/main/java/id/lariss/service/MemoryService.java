package id.lariss.service;

import id.lariss.service.dto.MemoryDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link id.lariss.domain.Memory}.
 */
public interface MemoryService {
    /**
     * Save a memory.
     *
     * @param memoryDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<MemoryDTO> save(MemoryDTO memoryDTO);

    /**
     * Updates a memory.
     *
     * @param memoryDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<MemoryDTO> update(MemoryDTO memoryDTO);

    /**
     * Partially updates a memory.
     *
     * @param memoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<MemoryDTO> partialUpdate(MemoryDTO memoryDTO);

    /**
     * Get all the memories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<MemoryDTO> findAll(Pageable pageable);

    /**
     * Returns the number of memories available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" memory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<MemoryDTO> findOne(Long id);

    /**
     * Delete the "id" memory.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
