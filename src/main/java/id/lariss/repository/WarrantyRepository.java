package id.lariss.repository;

import id.lariss.domain.Warranty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Warranty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WarrantyRepository extends ReactiveCrudRepository<Warranty, Long>, WarrantyRepositoryInternal {
    Flux<Warranty> findAllBy(Pageable pageable);

    @Override
    <S extends Warranty> Mono<S> save(S entity);

    @Override
    Flux<Warranty> findAll();

    @Override
    Mono<Warranty> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface WarrantyRepositoryInternal {
    <S extends Warranty> Mono<S> save(S entity);

    Flux<Warranty> findAllBy(Pageable pageable);

    Flux<Warranty> findAll();

    Mono<Warranty> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Warranty> findAllBy(Pageable pageable, Criteria criteria);
}
