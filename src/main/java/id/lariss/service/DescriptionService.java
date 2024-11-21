package id.lariss.service;

import id.lariss.service.dto.DescriptionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link id.lariss.domain.Description}.
 */
public interface DescriptionService {
    /**
     * Save a description.
     *
     * @param descriptionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<DescriptionDTO> save(DescriptionDTO descriptionDTO);

    /**
     * Updates a description.
     *
     * @param descriptionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<DescriptionDTO> update(DescriptionDTO descriptionDTO);

    /**
     * Partially updates a description.
     *
     * @param descriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<DescriptionDTO> partialUpdate(DescriptionDTO descriptionDTO);

    /**
     * Get all the descriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<DescriptionDTO> findAll(Pageable pageable);

    /**
     * Returns the number of descriptions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" description.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<DescriptionDTO> findOne(Long id);

    /**
     * Delete the "id" description.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
