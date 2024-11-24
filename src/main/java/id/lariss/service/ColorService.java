package id.lariss.service;

import id.lariss.service.dto.ColorDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link id.lariss.domain.Color}.
 */
public interface ColorService {
    /**
     * Save a color.
     *
     * @param colorDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ColorDTO> save(ColorDTO colorDTO);

    /**
     * Updates a color.
     *
     * @param colorDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ColorDTO> update(ColorDTO colorDTO);

    /**
     * Partially updates a color.
     *
     * @param colorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ColorDTO> partialUpdate(ColorDTO colorDTO);

    /**
     * Get all the colors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ColorDTO> findAll(Pageable pageable);

    /**
     * Returns the number of colors available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" color.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ColorDTO> findOne(Long id);

    /**
     * Delete the "id" color.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
