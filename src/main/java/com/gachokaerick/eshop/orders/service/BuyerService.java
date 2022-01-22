package com.gachokaerick.eshop.orders.service;

import com.gachokaerick.eshop.orders.domain.aggregates.buyer.Buyer;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.BuyerDomain;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.BuyerMapper;
import com.gachokaerick.eshop.orders.repository.BuyerRepository;
import com.gachokaerick.eshop.orders.repository.UserRepository;
import com.gachokaerick.eshop.orders.service.dto.BuyerDTO;
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
    private final UserRepository userRepository;
    private final BuyerMapper buyerMapper;

    public BuyerService(BuyerRepository buyerRepository, UserRepository userRepository, BuyerMapper buyerMapper) {
        this.buyerRepository = buyerRepository;
        this.userRepository = userRepository;
        this.buyerMapper = buyerMapper;
    }

    /**
     * Save a buyer.
     *
     * @param buyerDTO the entity to save.
     * @return the persisted entity.
     */
    public BuyerDTO save(BuyerDTO buyerDTO) {
        log.debug("Request to save Buyer : {}", buyerDTO);
        BuyerDomain buyerDomain = new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build();
        Buyer buyer;
        if (buyerDTO.getId() != null) {
            buyer = buyerDomain.toEntity(buyerRepository.getById(buyerDTO.getId()));
        } else {
            buyer = buyerDomain.toEntity(null);
            buyerDomain.setUser(buyer, userRepository.getById(buyerDTO.getUser().getId()));
        }
        buyer = buyerRepository.save(buyer);
        return buyerMapper.toDto(buyer);
    }

    /**
     * Partially update a buyer.
     *
     * @param buyerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BuyerDTO> partialUpdate(BuyerDTO buyerDTO) {
        log.debug("Request to partially update Buyer : {}", buyerDTO);

        return buyerRepository
            .findById(buyerDTO.getId())
            .map(existingBuyer -> {
                buyerMapper.partialUpdate(existingBuyer, buyerDTO);

                return existingBuyer;
            })
            .map(buyerRepository::save)
            .map(buyerMapper::toDto);
    }

    /**
     * Get all the buyers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BuyerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Buyers");
        return buyerRepository.findAll(pageable).map(buyerMapper::toDto);
    }

    /**
     * Get one buyer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BuyerDTO> findOne(Long id) {
        log.debug("Request to get Buyer : {}", id);
        return buyerRepository.findById(id).map(buyerMapper::toDto);
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
