package id.lariss.service;

import id.lariss.service.dto.WarrantyDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link id.lariss.domain.Warranty}.
 */
public interface WarrantyService {
    /**
     * Save a warranty.
     *
     * @param warrantyDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<WarrantyDTO> save(WarrantyDTO warrantyDTO);

    /**
     * Updates a warranty.
     *
     * @param warrantyDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<WarrantyDTO> update(WarrantyDTO warrantyDTO);

    /**
     * Partially updates a warranty.
     *
     * @param warrantyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<WarrantyDTO> partialUpdate(WarrantyDTO warrantyDTO);

    /**
     * Get all the warranties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<WarrantyDTO> findAll(Pageable pageable);

    /**
     * Returns the number of warranties available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" warranty.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<WarrantyDTO> findOne(Long id);

    /**
     * Delete the "id" warranty.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
