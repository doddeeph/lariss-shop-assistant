package id.lariss.service.impl;

import id.lariss.repository.BoxContentRepository;
import id.lariss.service.BoxContentService;
import id.lariss.service.dto.BoxContentDTO;
import id.lariss.service.mapper.BoxContentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link id.lariss.domain.BoxContent}.
 */
@Service
@Transactional
public class BoxContentServiceImpl implements BoxContentService {

    private static final Logger LOG = LoggerFactory.getLogger(BoxContentServiceImpl.class);

    private final BoxContentRepository boxContentRepository;

    private final BoxContentMapper boxContentMapper;

    public BoxContentServiceImpl(BoxContentRepository boxContentRepository, BoxContentMapper boxContentMapper) {
        this.boxContentRepository = boxContentRepository;
        this.boxContentMapper = boxContentMapper;
    }

    @Override
    public Mono<BoxContentDTO> save(BoxContentDTO boxContentDTO) {
        LOG.debug("Request to save BoxContent : {}", boxContentDTO);
        return boxContentRepository.save(boxContentMapper.toEntity(boxContentDTO)).map(boxContentMapper::toDto);
    }

    @Override
    public Mono<BoxContentDTO> update(BoxContentDTO boxContentDTO) {
        LOG.debug("Request to update BoxContent : {}", boxContentDTO);
        return boxContentRepository.save(boxContentMapper.toEntity(boxContentDTO)).map(boxContentMapper::toDto);
    }

    @Override
    public Mono<BoxContentDTO> partialUpdate(BoxContentDTO boxContentDTO) {
        LOG.debug("Request to partially update BoxContent : {}", boxContentDTO);

        return boxContentRepository
            .findById(boxContentDTO.getId())
            .map(existingBoxContent -> {
                boxContentMapper.partialUpdate(existingBoxContent, boxContentDTO);

                return existingBoxContent;
            })
            .flatMap(boxContentRepository::save)
            .map(boxContentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BoxContentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BoxContents");
        return boxContentRepository.findAllBy(pageable).map(boxContentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return boxContentRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<BoxContentDTO> findOne(Long id) {
        LOG.debug("Request to get BoxContent : {}", id);
        return boxContentRepository.findById(id).map(boxContentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete BoxContent : {}", id);
        return boxContentRepository.deleteById(id);
    }
}
