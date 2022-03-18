package com.gachokaerick.eshop.orders.web.rest;

import com.gachokaerick.eshop.orders.domain.aggregates.order.Authorization;
import com.gachokaerick.eshop.orders.repository.AuthorizationRepository;
import com.gachokaerick.eshop.orders.service.AuthorizationService;
import com.gachokaerick.eshop.orders.service.dto.AuthorizationDTO;
import com.gachokaerick.eshop.orders.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Authorization}.
 */
@RestController
@RequestMapping("/api")
public class AuthorizationResource {

    private final Logger log = LoggerFactory.getLogger(AuthorizationResource.class);

    private static final String ENTITY_NAME = "ordersAuthorization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthorizationService authorizationService;

    private final AuthorizationRepository authorizationRepository;

    public AuthorizationResource(AuthorizationService authorizationService, AuthorizationRepository authorizationRepository) {
        this.authorizationService = authorizationService;
        this.authorizationRepository = authorizationRepository;
    }

    /**
     * {@code POST  /authorizations} : Create a new authorization.
     *
     * @param authorizationDTO the authorizationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new authorizationDTO, or with status {@code 400 (Bad Request)} if the authorization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/authorizations")
    public ResponseEntity<AuthorizationDTO> createAuthorization(@Valid @RequestBody AuthorizationDTO authorizationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Authorization : {}", authorizationDTO);
        if (authorizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new authorization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuthorizationDTO result = authorizationService.save(authorizationDTO);
        return ResponseEntity
            .created(new URI("/api/authorizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /authorizations/:id} : Updates an existing authorization.
     *
     * @param id the id of the authorizationDTO to save.
     * @param authorizationDTO the authorizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorizationDTO,
     * or with status {@code 400 (Bad Request)} if the authorizationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authorizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/authorizations/{id}")
    public ResponseEntity<AuthorizationDTO> updateAuthorization(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AuthorizationDTO authorizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Authorization : {}, {}", id, authorizationDTO);
        if (authorizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authorizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authorizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AuthorizationDTO result = authorizationService.save(authorizationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorizationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /authorizations/:id} : Partial updates given fields of an existing authorization, field will ignore if it is null
     *
     * @param id the id of the authorizationDTO to save.
     * @param authorizationDTO the authorizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorizationDTO,
     * or with status {@code 400 (Bad Request)} if the authorizationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the authorizationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the authorizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/authorizations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AuthorizationDTO> partialUpdateAuthorization(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AuthorizationDTO authorizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Authorization partially : {}, {}", id, authorizationDTO);
        if (authorizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authorizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authorizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AuthorizationDTO> result = authorizationService.partialUpdate(authorizationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorizationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /authorizations} : get all the authorizations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authorizations in body.
     */
    @GetMapping("/authorizations")
    public ResponseEntity<List<AuthorizationDTO>> getAllAuthorizations(Pageable pageable) {
        log.debug("REST request to get a page of Authorizations");
        Page<AuthorizationDTO> page = authorizationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /authorizations/:id} : get the "id" authorization.
     *
     * @param id the id of the authorizationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the authorizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/authorizations/{id}")
    public ResponseEntity<AuthorizationDTO> getAuthorization(@PathVariable Long id) {
        log.debug("REST request to get Authorization : {}", id);
        Optional<AuthorizationDTO> authorizationDTO = authorizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(authorizationDTO);
    }

    /**
     * {@code DELETE  /authorizations/:id} : delete the "id" authorization.
     *
     * @param id the id of the authorizationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/authorizations/{id}")
    public ResponseEntity<Void> deleteAuthorization(@PathVariable Long id) {
        log.debug("REST request to delete Authorization : {}", id);
        authorizationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
