package id.lariss.web.rest;

import id.lariss.repository.ColorRepository;
import id.lariss.service.ColorService;
import id.lariss.service.dto.ColorDTO;
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
 * REST controller for managing {@link id.lariss.domain.Color}.
 */
@RestController
@RequestMapping("/api/colors")
public class ColorResource {

    private static final Logger LOG = LoggerFactory.getLogger(ColorResource.class);

    private static final String ENTITY_NAME = "color";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ColorService colorService;

    private final ColorRepository colorRepository;

    public ColorResource(ColorService colorService, ColorRepository colorRepository) {
        this.colorService = colorService;
        this.colorRepository = colorRepository;
    }

    /**
     * {@code POST  /colors} : Create a new color.
     *
     * @param colorDTO the colorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new colorDTO, or with status {@code 400 (Bad Request)} if the color has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ColorDTO>> createColor(@RequestBody ColorDTO colorDTO) throws URISyntaxException {
        LOG.debug("REST request to save Color : {}", colorDTO);
        if (colorDTO.getId() != null) {
            throw new BadRequestAlertException("A new color cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return colorService
            .save(colorDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/colors/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /colors/:id} : Updates an existing color.
     *
     * @param id the id of the colorDTO to save.
     * @param colorDTO the colorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated colorDTO,
     * or with status {@code 400 (Bad Request)} if the colorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the colorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ColorDTO>> updateColor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ColorDTO colorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Color : {}, {}", id, colorDTO);
        if (colorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, colorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return colorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return colorService
                    .update(colorDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /colors/:id} : Partial updates given fields of an existing color, field will ignore if it is null
     *
     * @param id the id of the colorDTO to save.
     * @param colorDTO the colorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated colorDTO,
     * or with status {@code 400 (Bad Request)} if the colorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the colorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the colorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ColorDTO>> partialUpdateColor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ColorDTO colorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Color partially : {}, {}", id, colorDTO);
        if (colorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, colorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return colorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ColorDTO> result = colorService.partialUpdate(colorDTO);

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
     * {@code GET  /colors} : get all the colors.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of colors in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ColorDTO>>> getAllColors(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Colors");
        return colorService
            .countAll()
            .zipWith(colorService.findAll(pageable).collectList())
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
     * {@code GET  /colors/:id} : get the "id" color.
     *
     * @param id the id of the colorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the colorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ColorDTO>> getColor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Color : {}", id);
        Mono<ColorDTO> colorDTO = colorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(colorDTO);
    }

    /**
     * {@code DELETE  /colors/:id} : delete the "id" color.
     *
     * @param id the id of the colorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteColor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Color : {}", id);
        return colorService
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
