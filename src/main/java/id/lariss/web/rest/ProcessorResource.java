package id.lariss.web.rest;

import id.lariss.repository.ProcessorRepository;
import id.lariss.service.ProcessorService;
import id.lariss.service.dto.ProcessorDTO;
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
 * REST controller for managing {@link id.lariss.domain.Processor}.
 */
@RestController
@RequestMapping("/api/processors")
public class ProcessorResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessorResource.class);

    private static final String ENTITY_NAME = "processor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessorService processorService;

    private final ProcessorRepository processorRepository;

    public ProcessorResource(ProcessorService processorService, ProcessorRepository processorRepository) {
        this.processorService = processorService;
        this.processorRepository = processorRepository;
    }

    /**
     * {@code POST  /processors} : Create a new processor.
     *
     * @param processorDTO the processorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processorDTO, or with status {@code 400 (Bad Request)} if the processor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ProcessorDTO>> createProcessor(@RequestBody ProcessorDTO processorDTO) throws URISyntaxException {
        LOG.debug("REST request to save Processor : {}", processorDTO);
        if (processorDTO.getId() != null) {
            throw new BadRequestAlertException("A new processor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return processorService
            .save(processorDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/processors/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /processors/:id} : Updates an existing processor.
     *
     * @param id the id of the processorDTO to save.
     * @param processorDTO the processorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processorDTO,
     * or with status {@code 400 (Bad Request)} if the processorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProcessorDTO>> updateProcessor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessorDTO processorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Processor : {}, {}", id, processorDTO);
        if (processorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return processorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return processorService
                    .update(processorDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /processors/:id} : Partial updates given fields of an existing processor, field will ignore if it is null
     *
     * @param id the id of the processorDTO to save.
     * @param processorDTO the processorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processorDTO,
     * or with status {@code 400 (Bad Request)} if the processorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the processorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the processorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ProcessorDTO>> partialUpdateProcessor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessorDTO processorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Processor partially : {}, {}", id, processorDTO);
        if (processorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return processorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ProcessorDTO> result = processorService.partialUpdate(processorDTO);

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
     * {@code GET  /processors} : get all the processors.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processors in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ProcessorDTO>>> getAllProcessors(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Processors");
        return processorService
            .countAll()
            .zipWith(processorService.findAll(pageable).collectList())
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
     * {@code GET  /processors/:id} : get the "id" processor.
     *
     * @param id the id of the processorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProcessorDTO>> getProcessor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Processor : {}", id);
        Mono<ProcessorDTO> processorDTO = processorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(processorDTO);
    }

    /**
     * {@code DELETE  /processors/:id} : delete the "id" processor.
     *
     * @param id the id of the processorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProcessor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Processor : {}", id);
        return processorService
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
