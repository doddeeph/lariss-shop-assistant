package id.lariss.service.impl;

import id.lariss.repository.ColorRepository;
import id.lariss.service.ColorService;
import id.lariss.service.dto.ColorDTO;
import id.lariss.service.mapper.ColorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link id.lariss.domain.Color}.
 */
@Service
@Transactional
public class ColorServiceImpl implements ColorService {

    private static final Logger LOG = LoggerFactory.getLogger(ColorServiceImpl.class);

    private final ColorRepository colorRepository;

    private final ColorMapper colorMapper;

    public ColorServiceImpl(ColorRepository colorRepository, ColorMapper colorMapper) {
        this.colorRepository = colorRepository;
        this.colorMapper = colorMapper;
    }

    @Override
    public Mono<ColorDTO> save(ColorDTO colorDTO) {
        LOG.debug("Request to save Color : {}", colorDTO);
        return colorRepository.save(colorMapper.toEntity(colorDTO)).map(colorMapper::toDto);
    }

    @Override
    public Mono<ColorDTO> update(ColorDTO colorDTO) {
        LOG.debug("Request to update Color : {}", colorDTO);
        return colorRepository.save(colorMapper.toEntity(colorDTO)).map(colorMapper::toDto);
    }

    @Override
    public Mono<ColorDTO> partialUpdate(ColorDTO colorDTO) {
        LOG.debug("Request to partially update Color : {}", colorDTO);

        return colorRepository
            .findById(colorDTO.getId())
            .map(existingColor -> {
                colorMapper.partialUpdate(existingColor, colorDTO);

                return existingColor;
            })
            .flatMap(colorRepository::save)
            .map(colorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ColorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Colors");
        return colorRepository.findAllBy(pageable).map(colorMapper::toDto);
    }

    public Mono<Long> countAll() {
        return colorRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ColorDTO> findOne(Long id) {
        LOG.debug("Request to get Color : {}", id);
        return colorRepository.findById(id).map(colorMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Color : {}", id);
        return colorRepository.deleteById(id);
    }
}
