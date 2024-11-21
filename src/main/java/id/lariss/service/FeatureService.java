package id.lariss.service;

import id.lariss.service.dto.FeatureDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link id.lariss.domain.Feature}.
 */
public interface FeatureService {
    /**
     * Save a feature.
     *
     * @param featureDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<FeatureDTO> save(FeatureDTO featureDTO);

    /**
     * Updates a feature.
     *
     * @param featureDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<FeatureDTO> update(FeatureDTO featureDTO);

    /**
     * Partially updates a feature.
     *
     * @param featureDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<FeatureDTO> partialUpdate(FeatureDTO featureDTO);

    /**
     * Get all the features.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<FeatureDTO> findAll(Pageable pageable);

    /**
     * Returns the number of features available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" feature.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<FeatureDTO> findOne(Long id);

    /**
     * Delete the "id" feature.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
