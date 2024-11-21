package id.lariss.web.rest;

import id.lariss.repository.DescriptionRepository;
import id.lariss.service.DescriptionService;
import id.lariss.service.dto.DescriptionDTO;
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
 * REST controller for managing {@link id.lariss.domain.Description}.
 */
@RestController
@RequestMapping("/api/descriptions")
public class DescriptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(DescriptionResource.class);

    private static final String ENTITY_NAME = "description";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DescriptionService descriptionService;

    private final DescriptionRepository descriptionRepository;

    public DescriptionResource(DescriptionService descriptionService, DescriptionRepository descriptionRepository) {
        this.descriptionService = descriptionService;
        this.descriptionRepository = descriptionRepository;
    }

    /**
     * {@code POST  /descriptions} : Create a new description.
     *
     * @param descriptionDTO the descriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new descriptionDTO, or with status {@code 400 (Bad Request)} if the description has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<DescriptionDTO>> createDescription(@Valid @RequestBody DescriptionDTO descriptionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Description : {}", descriptionDTO);
        if (descriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new description cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return descriptionService
            .save(descriptionDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/descriptions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /descriptions/:id} : Updates an existing description.
     *
     * @param id the id of the descriptionDTO to save.
     * @param descriptionDTO the descriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated descriptionDTO,
     * or with status {@code 400 (Bad Request)} if the descriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the descriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<DescriptionDTO>> updateDescription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DescriptionDTO descriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Description : {}, {}", id, descriptionDTO);
        if (descriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, descriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return descriptionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return descriptionService
                    .update(descriptionDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /descriptions/:id} : Partial updates given fields of an existing description, field will ignore if it is null
     *
     * @param id the id of the descriptionDTO to save.
     * @param descriptionDTO the descriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated descriptionDTO,
     * or with status {@code 400 (Bad Request)} if the descriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the descriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the descriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DescriptionDTO>> partialUpdateDescription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DescriptionDTO descriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Description partially : {}, {}", id, descriptionDTO);
        if (descriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, descriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return descriptionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DescriptionDTO> result = descriptionService.partialUpdate(descriptionDTO);

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
     * {@code GET  /descriptions} : get all the descriptions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of descriptions in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<DescriptionDTO>>> getAllDescriptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Descriptions");
        return descriptionService
            .countAll()
            .zipWith(descriptionService.findAll(pageable).collectList())
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
     * {@code GET  /descriptions/:id} : get the "id" description.
     *
     * @param id the id of the descriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the descriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DescriptionDTO>> getDescription(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Description : {}", id);
        Mono<DescriptionDTO> descriptionDTO = descriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(descriptionDTO);
    }

    /**
     * {@code DELETE  /descriptions/:id} : delete the "id" description.
     *
     * @param id the id of the descriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDescription(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Description : {}", id);
        return descriptionService
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
