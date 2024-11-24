package id.lariss.repository;

import id.lariss.domain.Variant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Variant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VariantRepository extends ReactiveCrudRepository<Variant, Long>, VariantRepositoryInternal {
    Flux<Variant> findAllBy(Pageable pageable);

    @Override
    Mono<Variant> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Variant> findAllWithEagerRelationships();

    @Override
    Flux<Variant> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM variant entity WHERE entity.color_id = :id")
    Flux<Variant> findByColor(Long id);

    @Query("SELECT * FROM variant entity WHERE entity.color_id IS NULL")
    Flux<Variant> findAllWhereColorIsNull();

    @Query("SELECT * FROM variant entity WHERE entity.processor_id = :id")
    Flux<Variant> findByProcessor(Long id);

    @Query("SELECT * FROM variant entity WHERE entity.processor_id IS NULL")
    Flux<Variant> findAllWhereProcessorIsNull();

    @Query("SELECT * FROM variant entity WHERE entity.memory_id = :id")
    Flux<Variant> findByMemory(Long id);

    @Query("SELECT * FROM variant entity WHERE entity.memory_id IS NULL")
    Flux<Variant> findAllWhereMemoryIsNull();

    @Query("SELECT * FROM variant entity WHERE entity.storage_id = :id")
    Flux<Variant> findByStorage(Long id);

    @Query("SELECT * FROM variant entity WHERE entity.storage_id IS NULL")
    Flux<Variant> findAllWhereStorageIsNull();

    @Override
    <S extends Variant> Mono<S> save(S entity);

    @Override
    Flux<Variant> findAll();

    @Override
    Mono<Variant> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);

    @Override
    Flux<Variant> findAllByProductName(String productName, Pageable pageable);
}

interface VariantRepositoryInternal {
    <S extends Variant> Mono<S> save(S entity);

    Flux<Variant> findAllBy(Pageable pageable);

    Flux<Variant> findAll();

    Mono<Variant> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Variant> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Variant> findOneWithEagerRelationships(Long id);

    Flux<Variant> findAllWithEagerRelationships();

    Flux<Variant> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);

    Flux<Variant> findAllByProductName(String productName, Pageable pageable);
}
