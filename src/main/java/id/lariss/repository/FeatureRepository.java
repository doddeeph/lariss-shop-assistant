package id.lariss.repository;

import id.lariss.domain.Feature;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Feature entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeatureRepository extends ReactiveCrudRepository<Feature, Long>, FeatureRepositoryInternal {
    Flux<Feature> findAllBy(Pageable pageable);

    @Override
    <S extends Feature> Mono<S> save(S entity);

    @Override
    Flux<Feature> findAll();

    @Override
    Mono<Feature> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FeatureRepositoryInternal {
    <S extends Feature> Mono<S> save(S entity);

    Flux<Feature> findAllBy(Pageable pageable);

    Flux<Feature> findAll();

    Mono<Feature> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Feature> findAllBy(Pageable pageable, Criteria criteria);
}
