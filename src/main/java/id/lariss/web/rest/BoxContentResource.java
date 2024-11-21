package id.lariss.web.rest;

import id.lariss.repository.BoxContentRepository;
import id.lariss.service.BoxContentService;
import id.lariss.service.dto.BoxContentDTO;
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
 * REST controller for managing {@link id.lariss.domain.BoxContent}.
 */
@RestController
@RequestMapping("/api/box-contents")
public class BoxContentResource {

    private static final Logger LOG = LoggerFactory.getLogger(BoxContentResource.class);

    private static final String ENTITY_NAME = "boxContent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoxContentService boxContentService;

    private final BoxContentRepository boxContentRepository;

    public BoxContentResource(BoxContentService boxContentService, BoxContentRepository boxContentRepository) {
        this.boxContentService = boxContentService;
        this.boxContentRepository = boxContentRepository;
    }

    /**
     * {@code POST  /box-contents} : Create a new boxContent.
     *
     * @param boxContentDTO the boxContentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boxContentDTO, or with status {@code 400 (Bad Request)} if the boxContent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<BoxContentDTO>> createBoxContent(@Valid @RequestBody BoxContentDTO boxContentDTO) throws URISyntaxException {
        LOG.debug("REST request to save BoxContent : {}", boxContentDTO);
        if (boxContentDTO.getId() != null) {
            throw new BadRequestAlertException("A new boxContent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return boxContentService
            .save(boxContentDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/box-contents/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /box-contents/:id} : Updates an existing boxContent.
     *
     * @param id the id of the boxContentDTO to save.
     * @param boxContentDTO the boxContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boxContentDTO,
     * or with status {@code 400 (Bad Request)} if the boxContentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boxContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<BoxContentDTO>> updateBoxContent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BoxContentDTO boxContentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BoxContent : {}, {}", id, boxContentDTO);
        if (boxContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boxContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return boxContentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return boxContentService
                    .update(boxContentDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /box-contents/:id} : Partial updates given fields of an existing boxContent, field will ignore if it is null
     *
     * @param id the id of the boxContentDTO to save.
     * @param boxContentDTO the boxContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boxContentDTO,
     * or with status {@code 400 (Bad Request)} if the boxContentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the boxContentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the boxContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<BoxContentDTO>> partialUpdateBoxContent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BoxContentDTO boxContentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BoxContent partially : {}, {}", id, boxContentDTO);
        if (boxContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boxContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return boxContentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<BoxContentDTO> result = boxContentService.partialUpdate(boxContentDTO);

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
     * {@code GET  /box-contents} : get all the boxContents.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boxContents in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<BoxContentDTO>>> getAllBoxContents(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of BoxContents");
        return boxContentService
            .countAll()
            .zipWith(boxContentService.findAll(pageable).collectList())
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
     * {@code GET  /box-contents/:id} : get the "id" boxContent.
     *
     * @param id the id of the boxContentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boxContentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<BoxContentDTO>> getBoxContent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BoxContent : {}", id);
        Mono<BoxContentDTO> boxContentDTO = boxContentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(boxContentDTO);
    }

    /**
     * {@code DELETE  /box-contents/:id} : delete the "id" boxContent.
     *
     * @param id the id of the boxContentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteBoxContent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BoxContent : {}", id);
        return boxContentService
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
