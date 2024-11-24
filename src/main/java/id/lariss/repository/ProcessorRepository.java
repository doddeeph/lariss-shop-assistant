package id.lariss.repository;

import id.lariss.domain.Processor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Processor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessorRepository extends ReactiveCrudRepository<Processor, Long>, ProcessorRepositoryInternal {
    Flux<Processor> findAllBy(Pageable pageable);

    @Override
    <S extends Processor> Mono<S> save(S entity);

    @Override
    Flux<Processor> findAll();

    @Override
    Mono<Processor> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ProcessorRepositoryInternal {
    <S extends Processor> Mono<S> save(S entity);

    Flux<Processor> findAllBy(Pageable pageable);

    Flux<Processor> findAll();

    Mono<Processor> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Processor> findAllBy(Pageable pageable, Criteria criteria);
}
