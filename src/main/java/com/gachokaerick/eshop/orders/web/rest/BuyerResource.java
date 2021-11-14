package com.gachokaerick.eshop.orders.web.rest;

import com.gachokaerick.eshop.orders.domain.Buyer;
import com.gachokaerick.eshop.orders.repository.BuyerRepository;
import com.gachokaerick.eshop.orders.repository.UserRepository;
import com.gachokaerick.eshop.orders.service.BuyerService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gachokaerick.eshop.orders.domain.Buyer}.
 */
@RestController
@RequestMapping("/api")
public class BuyerResource {

    private final Logger log = LoggerFactory.getLogger(BuyerResource.class);

    private static final String ENTITY_NAME = "ordersBuyer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BuyerService buyerService;

    private final BuyerRepository buyerRepository;

    private final UserRepository userRepository;

    public BuyerResource(BuyerService buyerService, BuyerRepository buyerRepository, UserRepository userRepository) {
        this.buyerService = buyerService;
        this.buyerRepository = buyerRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /buyers} : Create a new buyer.
     *
     * @param buyer the buyer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new buyer, or with status {@code 400 (Bad Request)} if the buyer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/buyers")
    public ResponseEntity<Buyer> createBuyer(@Valid @RequestBody Buyer buyer) throws URISyntaxException {
        log.debug("REST request to save Buyer : {}", buyer);
        if (buyer.getId() != null) {
            throw new BadRequestAlertException("A new buyer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (buyer.getUser() != null) {
            // Save user in case it's new and only exists in gateway
            userRepository.save(buyer.getUser());
        }
        Buyer result = buyerService.save(buyer);
        return ResponseEntity
            .created(new URI("/api/buyers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /buyers/:id} : Updates an existing buyer.
     *
     * @param id the id of the buyer to save.
     * @param buyer the buyer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated buyer,
     * or with status {@code 400 (Bad Request)} if the buyer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the buyer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/buyers/{id}")
    public ResponseEntity<Buyer> updateBuyer(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Buyer buyer)
        throws URISyntaxException {
        log.debug("REST request to update Buyer : {}, {}", id, buyer);
        if (buyer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, buyer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!buyerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (buyer.getUser() != null) {
            // Save user in case it's new and only exists in gateway
            userRepository.save(buyer.getUser());
        }
        Buyer result = buyerService.save(buyer);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, buyer.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /buyers/:id} : Partial updates given fields of an existing buyer, field will ignore if it is null
     *
     * @param id the id of the buyer to save.
     * @param buyer the buyer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated buyer,
     * or with status {@code 400 (Bad Request)} if the buyer is not valid,
     * or with status {@code 404 (Not Found)} if the buyer is not found,
     * or with status {@code 500 (Internal Server Error)} if the buyer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/buyers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Buyer> partialUpdateBuyer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Buyer buyer
    ) throws URISyntaxException {
        log.debug("REST request to partial update Buyer partially : {}, {}", id, buyer);
        if (buyer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, buyer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!buyerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (buyer.getUser() != null) {
            // Save user in case it's new and only exists in gateway
            userRepository.save(buyer.getUser());
        }

        Optional<Buyer> result = buyerService.partialUpdate(buyer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, buyer.getId().toString())
        );
    }

    /**
     * {@code GET  /buyers} : get all the buyers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of buyers in body.
     */
    @GetMapping("/buyers")
    public ResponseEntity<List<Buyer>> getAllBuyers(Pageable pageable) {
        log.debug("REST request to get a page of Buyers");
        Page<Buyer> page = buyerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /buyers/:id} : get the "id" buyer.
     *
     * @param id the id of the buyer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the buyer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/buyers/{id}")
    public ResponseEntity<Buyer> getBuyer(@PathVariable Long id) {
        log.debug("REST request to get Buyer : {}", id);
        Optional<Buyer> buyer = buyerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(buyer);
    }

    /**
     * {@code DELETE  /buyers/:id} : delete the "id" buyer.
     *
     * @param id the id of the buyer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/buyers/{id}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable Long id) {
        log.debug("REST request to delete Buyer : {}", id);
        buyerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
