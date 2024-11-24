package id.lariss.web.rest;

import id.lariss.repository.VariantRepository;
import id.lariss.service.VariantService;
import id.lariss.service.dto.VariantDTO;
import id.lariss.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link id.lariss.domain.Variant}.
 */
@RestController
@RequestMapping("/api")
public class VariantResource {

    private static final Logger LOG = LoggerFactory.getLogger(VariantResource.class);

    private static final String ENTITY_NAME = "variant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VariantService variantService;

    private final VariantRepository variantRepository;

    public VariantResource(VariantService variantService, VariantRepository variantRepository) {
        this.variantService = variantService;
        this.variantRepository = variantRepository;
    }

    /**
     * {@code POST  /variants} : Create a new variant.
     *
     * @param variantDTO the variantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new variantDTO, or with status {@code 400 (Bad Request)} if the variant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/variants")
    public Mono<ResponseEntity<VariantDTO>> createVariant(@RequestBody VariantDTO variantDTO) throws URISyntaxException {
        LOG.debug("REST request to save Variant : {}", variantDTO);
        if (variantDTO.getId() != null) {
            throw new BadRequestAlertException("A new variant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return variantService
            .save(variantDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/variants/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /variants/:id} : Updates an existing variant.
     *
     * @param id the id of the variantDTO to save.
     * @param variantDTO the variantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated variantDTO,
     * or with status {@code 400 (Bad Request)} if the variantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the variantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/variants/{id}")
    public Mono<ResponseEntity<VariantDTO>> updateVariant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VariantDTO variantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Variant : {}, {}", id, variantDTO);
        if (variantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, variantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return variantRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return variantService
                    .update(variantDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /variants/:id} : Partial updates given fields of an existing variant, field will ignore if it is null
     *
     * @param id the id of the variantDTO to save.
     * @param variantDTO the variantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated variantDTO,
     * or with status {@code 400 (Bad Request)} if the variantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the variantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the variantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/variants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<VariantDTO>> partialUpdateVariant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VariantDTO variantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Variant partially : {}, {}", id, variantDTO);
        if (variantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, variantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return variantRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<VariantDTO> result = variantService.partialUpdate(variantDTO);

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
     * {@code GET  /variants} : get all the variants.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of variants in body.
     */
    @GetMapping(value = "/variants", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<VariantDTO>>> getAllVariants(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Variants");
        return variantService
            .countAll()
            .zipWith(variantService.findAll(pageable).collectList())
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
     * {@code GET  /variants/:id} : get the "id" variant.
     *
     * @param id the id of the variantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the variantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/variants/{id}")
    public Mono<ResponseEntity<VariantDTO>> getVariant(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Variant : {}", id);
        Mono<VariantDTO> variantDTO = variantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(variantDTO);
    }

    /**
     * {@code DELETE  /variants/:id} : delete the "id" variant.
     *
     * @param id the id of the variantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/variants/{id}")
    public Mono<ResponseEntity<Void>> deleteVariant(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Variant : {}", id);
        return variantService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    @GetMapping(value = "/public/variants", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<VariantDTO>>> getAllPublicVariants(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam String productName
    ) {
        LOG.debug("REST request to get Variants by product name: {}", productName);
        return variantService
            .findAllByProductName(productName, pageable)
            .collectList()
            .map(entities ->
                ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(entities, pageable, entities.size())
                        )
                    )
                    .body(entities)
            );
    }
}
