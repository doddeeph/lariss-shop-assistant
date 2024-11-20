package id.lariss.service.impl;

import id.lariss.repository.CategoryRepository;
import id.lariss.service.CategoryService;
import id.lariss.service.dto.CategoryDTO;
import id.lariss.service.mapper.CategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link id.lariss.domain.Category}.
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Mono<CategoryDTO> save(CategoryDTO categoryDTO) {
        LOG.debug("Request to save Category : {}", categoryDTO);
        return categoryRepository.save(categoryMapper.toEntity(categoryDTO)).map(categoryMapper::toDto);
    }

    @Override
    public Mono<CategoryDTO> update(CategoryDTO categoryDTO) {
        LOG.debug("Request to update Category : {}", categoryDTO);
        return categoryRepository.save(categoryMapper.toEntity(categoryDTO)).map(categoryMapper::toDto);
    }

    @Override
    public Mono<CategoryDTO> partialUpdate(CategoryDTO categoryDTO) {
        LOG.debug("Request to partially update Category : {}", categoryDTO);

        return categoryRepository
            .findById(categoryDTO.getId())
            .map(existingCategory -> {
                categoryMapper.partialUpdate(existingCategory, categoryDTO);

                return existingCategory;
            })
            .flatMap(categoryRepository::save)
            .map(categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CategoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Categories");
        return categoryRepository.findAllBy(pageable).map(categoryMapper::toDto);
    }

    public Mono<Long> countAll() {
        return categoryRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CategoryDTO> findOne(Long id) {
        LOG.debug("Request to get Category : {}", id);
        return categoryRepository.findById(id).map(categoryMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Category : {}", id);
        return categoryRepository.deleteById(id);
    }
}
