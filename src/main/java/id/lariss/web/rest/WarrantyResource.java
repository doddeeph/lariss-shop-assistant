package id.lariss.web.rest;

import id.lariss.repository.WarrantyRepository;
import id.lariss.service.WarrantyService;
import id.lariss.service.dto.WarrantyDTO;
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
 * REST controller for managing {@link id.lariss.domain.Warranty}.
 */
@RestController
@RequestMapping("/api/warranties")
public class WarrantyResource {

    private static final Logger LOG = LoggerFactory.getLogger(WarrantyResource.class);

    private static final String ENTITY_NAME = "warranty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WarrantyService warrantyService;

    private final WarrantyRepository warrantyRepository;

    public WarrantyResource(WarrantyService warrantyService, WarrantyRepository warrantyRepository) {
        this.warrantyService = warrantyService;
        this.warrantyRepository = warrantyRepository;
    }

    /**
     * {@code POST  /warranties} : Create a new warranty.
     *
     * @param warrantyDTO the warrantyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new warrantyDTO, or with status {@code 400 (Bad Request)} if the warranty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<WarrantyDTO>> createWarranty(@Valid @RequestBody WarrantyDTO warrantyDTO) throws URISyntaxException {
        LOG.debug("REST request to save Warranty : {}", warrantyDTO);
        if (warrantyDTO.getId() != null) {
            throw new BadRequestAlertException("A new warranty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return warrantyService
            .save(warrantyDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/warranties/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /warranties/:id} : Updates an existing warranty.
     *
     * @param id the id of the warrantyDTO to save.
     * @param warrantyDTO the warrantyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warrantyDTO,
     * or with status {@code 400 (Bad Request)} if the warrantyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the warrantyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<WarrantyDTO>> updateWarranty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WarrantyDTO warrantyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Warranty : {}, {}", id, warrantyDTO);
        if (warrantyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warrantyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return warrantyRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return warrantyService
                    .update(warrantyDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /warranties/:id} : Partial updates given fields of an existing warranty, field will ignore if it is null
     *
     * @param id the id of the warrantyDTO to save.
     * @param warrantyDTO the warrantyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warrantyDTO,
     * or with status {@code 400 (Bad Request)} if the warrantyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the warrantyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the warrantyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<WarrantyDTO>> partialUpdateWarranty(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WarrantyDTO warrantyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Warranty partially : {}, {}", id, warrantyDTO);
        if (warrantyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warrantyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return warrantyRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<WarrantyDTO> result = warrantyService.partialUpdate(warrantyDTO);

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
     * {@code GET  /warranties} : get all the warranties.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of warranties in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<WarrantyDTO>>> getAllWarranties(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Warranties");
        return warrantyService
            .countAll()
            .zipWith(warrantyService.findAll(pageable).collectList())
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
     * {@code GET  /warranties/:id} : get the "id" warranty.
     *
     * @param id the id of the warrantyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the warrantyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<WarrantyDTO>> getWarranty(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Warranty : {}", id);
        Mono<WarrantyDTO> warrantyDTO = warrantyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(warrantyDTO);
    }

    /**
     * {@code DELETE  /warranties/:id} : delete the "id" warranty.
     *
     * @param id the id of the warrantyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteWarranty(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Warranty : {}", id);
        return warrantyService
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
