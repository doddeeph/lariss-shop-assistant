package id.lariss.service.impl;

import id.lariss.repository.WarrantyRepository;
import id.lariss.service.WarrantyService;
import id.lariss.service.dto.WarrantyDTO;
import id.lariss.service.mapper.WarrantyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link id.lariss.domain.Warranty}.
 */
@Service
@Transactional
public class WarrantyServiceImpl implements WarrantyService {

    private static final Logger LOG = LoggerFactory.getLogger(WarrantyServiceImpl.class);

    private final WarrantyRepository warrantyRepository;

    private final WarrantyMapper warrantyMapper;

    public WarrantyServiceImpl(WarrantyRepository warrantyRepository, WarrantyMapper warrantyMapper) {
        this.warrantyRepository = warrantyRepository;
        this.warrantyMapper = warrantyMapper;
    }

    @Override
    public Mono<WarrantyDTO> save(WarrantyDTO warrantyDTO) {
        LOG.debug("Request to save Warranty : {}", warrantyDTO);
        return warrantyRepository.save(warrantyMapper.toEntity(warrantyDTO)).map(warrantyMapper::toDto);
    }

    @Override
    public Mono<WarrantyDTO> update(WarrantyDTO warrantyDTO) {
        LOG.debug("Request to update Warranty : {}", warrantyDTO);
        return warrantyRepository.save(warrantyMapper.toEntity(warrantyDTO)).map(warrantyMapper::toDto);
    }

    @Override
    public Mono<WarrantyDTO> partialUpdate(WarrantyDTO warrantyDTO) {
        LOG.debug("Request to partially update Warranty : {}", warrantyDTO);

        return warrantyRepository
            .findById(warrantyDTO.getId())
            .map(existingWarranty -> {
                warrantyMapper.partialUpdate(existingWarranty, warrantyDTO);

                return existingWarranty;
            })
            .flatMap(warrantyRepository::save)
            .map(warrantyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<WarrantyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Warranties");
        return warrantyRepository.findAllBy(pageable).map(warrantyMapper::toDto);
    }

    public Mono<Long> countAll() {
        return warrantyRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<WarrantyDTO> findOne(Long id) {
        LOG.debug("Request to get Warranty : {}", id);
        return warrantyRepository.findById(id).map(warrantyMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Warranty : {}", id);
        return warrantyRepository.deleteById(id);
    }
}
