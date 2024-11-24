package id.lariss.service;

import id.lariss.service.dto.StorageDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link id.lariss.domain.Storage}.
 */
public interface StorageService {
    /**
     * Save a storage.
     *
     * @param storageDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<StorageDTO> save(StorageDTO storageDTO);

    /**
     * Updates a storage.
     *
     * @param storageDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<StorageDTO> update(StorageDTO storageDTO);

    /**
     * Partially updates a storage.
     *
     * @param storageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<StorageDTO> partialUpdate(StorageDTO storageDTO);

    /**
     * Get all the storages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<StorageDTO> findAll(Pageable pageable);

    /**
     * Returns the number of storages available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" storage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<StorageDTO> findOne(Long id);

    /**
     * Delete the "id" storage.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
