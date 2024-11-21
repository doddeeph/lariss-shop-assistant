package id.lariss.service;

import id.lariss.service.dto.BoxContentDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link id.lariss.domain.BoxContent}.
 */
public interface BoxContentService {
    /**
     * Save a boxContent.
     *
     * @param boxContentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<BoxContentDTO> save(BoxContentDTO boxContentDTO);

    /**
     * Updates a boxContent.
     *
     * @param boxContentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<BoxContentDTO> update(BoxContentDTO boxContentDTO);

    /**
     * Partially updates a boxContent.
     *
     * @param boxContentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<BoxContentDTO> partialUpdate(BoxContentDTO boxContentDTO);

    /**
     * Get all the boxContents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<BoxContentDTO> findAll(Pageable pageable);

    /**
     * Returns the number of boxContents available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" boxContent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<BoxContentDTO> findOne(Long id);

    /**
     * Delete the "id" boxContent.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
