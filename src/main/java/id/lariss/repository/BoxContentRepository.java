package id.lariss.repository;

import id.lariss.domain.BoxContent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the BoxContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoxContentRepository extends ReactiveCrudRepository<BoxContent, Long>, BoxContentRepositoryInternal {
    Flux<BoxContent> findAllBy(Pageable pageable);

    @Override
    <S extends BoxContent> Mono<S> save(S entity);

    @Override
    Flux<BoxContent> findAll();

    @Override
    Mono<BoxContent> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BoxContentRepositoryInternal {
    <S extends BoxContent> Mono<S> save(S entity);

    Flux<BoxContent> findAllBy(Pageable pageable);

    Flux<BoxContent> findAll();

    Mono<BoxContent> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<BoxContent> findAllBy(Pageable pageable, Criteria criteria);
}
