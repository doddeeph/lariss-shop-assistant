package id.lariss.service;

import id.lariss.service.dto.VariantDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link id.lariss.domain.Variant}.
 */
public interface VariantService {
    /**
     * Save a variant.
     *
     * @param variantDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<VariantDTO> save(VariantDTO variantDTO);

    /**
     * Updates a variant.
     *
     * @param variantDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<VariantDTO> update(VariantDTO variantDTO);

    /**
     * Partially updates a variant.
     *
     * @param variantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<VariantDTO> partialUpdate(VariantDTO variantDTO);

    /**
     * Get all the variants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<VariantDTO> findAll(Pageable pageable);

    /**
     * Get all the variants with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<VariantDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of variants available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" variant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<VariantDTO> findOne(Long id);

    /**
     * Delete the "id" variant.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    Flux<VariantDTO> findAllByProductName(String productName, Pageable pageable);
}
