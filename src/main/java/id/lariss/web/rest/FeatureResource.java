package id.lariss.web.rest;

import id.lariss.repository.FeatureRepository;
import id.lariss.service.FeatureService;
import id.lariss.service.dto.FeatureDTO;
import id.lariss.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link id.lariss.domain.Feature}.
 */
@RestController
@RequestMapping("/api/features")
public class FeatureResource {

    private static final Logger LOG = LoggerFactory.getLogger(FeatureResource.class);

    private static final String ENTITY_NAME = "feature";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeatureService featureService;

    private final FeatureRepository featureRepository;

    public FeatureResource(FeatureService featureService, FeatureRepository featureRepository) {
        this.featureService = featureService;
        this.featureRepository = featureRepository;
    }

    /**
     * {@code POST  /features} : Create a new feature.
     *
     * @param featureDTO the featureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new featureDTO, or with status {@code 400 (Bad Request)} if the feature has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FeatureDTO>> createFeature(@Valid @RequestBody FeatureDTO featureDTO) throws URISyntaxException {
        LOG.debug("REST request to save Feature : {}", featureDTO);
        if (featureDTO.getId() != null) {
            throw new BadRequestAlertException("A new feature cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return featureService
            .save(featureDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/features/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /features/:id} : Updates an existing feature.
     *
     * @param id the id of the featureDTO to save.
     * @param featureDTO the featureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated featureDTO,
     * or with status {@code 400 (Bad Request)} if the featureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the featureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FeatureDTO>> updateFeature(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FeatureDTO featureDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Feature : {}, {}", id, featureDTO);
        if (featureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, featureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return featureRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return featureService
                    .update(featureDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /features/:id} : Partial updates given fields of an existing feature, field will ignore if it is null
     *
     * @param id the id of the featureDTO to save.
     * @param featureDTO the featureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated featureDTO,
     * or with status {@code 400 (Bad Request)} if the featureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the featureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the featureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FeatureDTO>> partialUpdateFeature(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FeatureDTO featureDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Feature partially : {}, {}", id, featureDTO);
        if (featureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, featureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return featureRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FeatureDTO> result = featureService.partialUpdate(featureDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /features} : get all the features.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of features in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<FeatureDTO>>> getAllFeatures(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Features");
        return featureService
            .countAll()
            .zipWith(featureService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /features/:id} : get the "id" feature.
     *
     * @param id the id of the featureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the featureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FeatureDTO>> getFeature(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Feature : {}", id);
        Mono<FeatureDTO> featureDTO = featureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(featureDTO);
    }

    /**
     * {@code DELETE  /features/:id} : delete the "id" feature.
     *
     * @param id the id of the featureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFeature(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Feature : {}", id);
        return featureService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
