package id.lariss.repository;

import id.lariss.domain.Storage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Storage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StorageRepository extends ReactiveCrudRepository<Storage, Long>, StorageRepositoryInternal {
    Flux<Storage> findAllBy(Pageable pageable);

    @Override
    <S extends Storage> Mono<S> save(S entity);

    @Override
    Flux<Storage> findAll();

    @Override
    Mono<Storage> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface StorageRepositoryInternal {
    <S extends Storage> Mono<S> save(S entity);

    Flux<Storage> findAllBy(Pageable pageable);

    Flux<Storage> findAll();

    Mono<Storage> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Storage> findAllBy(Pageable pageable, Criteria criteria);
}
