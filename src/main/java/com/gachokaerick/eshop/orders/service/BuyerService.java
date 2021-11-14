package com.gachokaerick.eshop.orders.service;

import com.gachokaerick.eshop.orders.domain.Buyer;
import com.gachokaerick.eshop.orders.repository.BuyerRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Buyer}.
 */
@Service
@Transactional
public class BuyerService {

    private final Logger log = LoggerFactory.getLogger(BuyerService.class);

    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    /**
     * Save a buyer.
     *
     * @param buyer the entity to save.
     * @return the persisted entity.
     */
    public Buyer save(Buyer buyer) {
        log.debug("Request to save Buyer : {}", buyer);
        return buyerRepository.save(buyer);
    }

    /**
     * Partially update a buyer.
     *
     * @param buyer the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Buyer> partialUpdate(Buyer buyer) {
        log.debug("Request to partially update Buyer : {}", buyer);

        return buyerRepository
            .findById(buyer.getId())
            .map(existingBuyer -> {
                if (buyer.getFirstName() != null) {
                    existingBuyer.setFirstName(buyer.getFirstName());
                }
                if (buyer.getLastName() != null) {
                    existingBuyer.setLastName(buyer.getLastName());
                }
                if (buyer.getGender() != null) {
                    existingBuyer.setGender(buyer.getGender());
                }
                if (buyer.getEmail() != null) {
                    existingBuyer.setEmail(buyer.getEmail());
                }
                if (buyer.getPhone() != null) {
                    existingBuyer.setPhone(buyer.getPhone());
                }

                return existingBuyer;
            })
            .map(buyerRepository::save);
    }

    /**
     * Get all the buyers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Buyer> findAll(Pageable pageable) {
        log.debug("Request to get all Buyers");
        return buyerRepository.findAll(pageable);
    }

    /**
     * Get one buyer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Buyer> findOne(Long id) {
        log.debug("Request to get Buyer : {}", id);
        return buyerRepository.findById(id);
    }

    /**
     * Delete the buyer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Buyer : {}", id);
        buyerRepository.deleteById(id);
    }
}
