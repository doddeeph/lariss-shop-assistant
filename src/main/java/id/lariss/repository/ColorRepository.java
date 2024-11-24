package id.lariss.repository;

import id.lariss.domain.Color;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Color entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ColorRepository extends ReactiveCrudRepository<Color, Long>, ColorRepositoryInternal {
    Flux<Color> findAllBy(Pageable pageable);

    @Override
    <S extends Color> Mono<S> save(S entity);

    @Override
    Flux<Color> findAll();

    @Override
    Mono<Color> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ColorRepositoryInternal {
    <S extends Color> Mono<S> save(S entity);

    Flux<Color> findAllBy(Pageable pageable);

    Flux<Color> findAll();

    Mono<Color> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Color> findAllBy(Pageable pageable, Criteria criteria);
}
