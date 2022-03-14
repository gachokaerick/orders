package com.gachokaerick.eshop.orders.service;

import com.gachokaerick.eshop.orders.domain.Address;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.Buyer;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.BuyerDomain;
import com.gachokaerick.eshop.orders.repository.AddressRepository;
import com.gachokaerick.eshop.orders.repository.AddressSpecification;
import com.gachokaerick.eshop.orders.repository.BuyerRepository;
import com.gachokaerick.eshop.orders.service.dto.AddressDTO;
import com.gachokaerick.eshop.orders.service.mapper.AddressMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Address}.
 */
@Service
@Transactional
public class AddressService {

    private final Logger log = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository addressRepository;
    private final BuyerRepository buyerRepository;
    private final AddressMapper addressMapper;

    public AddressService(AddressRepository addressRepository, BuyerRepository buyerRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.buyerRepository = buyerRepository;
        this.addressMapper = addressMapper;
    }

    /**
     * Save a address.
     *
     * @param addressDTO the entity to save.
     * @return the persisted entity.
     */
    public AddressDTO save(AddressDTO addressDTO) {
        log.debug("Request to save Address : {}", addressDTO);
        Address address;
        if (addressDTO.getId() != null) {
            address = addressRepository.findById(addressDTO.getId()).orElseThrow();
            addressMapper.partialUpdate(address, addressDTO);
            address = addressRepository.save(address);
        } else {
            Buyer buyer = buyerRepository.findById(addressDTO.getBuyer().getId()).orElseThrow();
            address = addressMapper.toEntity(addressDTO);
            address.setBuyer(buyer);
            BuyerDomain buyerDomain = new BuyerDomain.BuyerBuilder().withDTO(addressDTO.getBuyer()).build();
            buyerDomain.addAddress(buyer, address);
            address = addressRepository.save(address);
            buyerRepository.save(buyer);
        }
        return addressMapper.toDto(address);
    }

    /**
     * Partially update a address.
     *
     * @param addressDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AddressDTO> partialUpdate(AddressDTO addressDTO) {
        log.debug("Request to partially update Address : {}", addressDTO);

        return addressRepository
            .findById(addressDTO.getId())
            .map(existingAddress -> {
                addressMapper.partialUpdate(existingAddress, addressDTO);

                return existingAddress;
            })
            .map(addressRepository::save)
            .map(addressMapper::toDto);
    }

    /**
     * Get all the addresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AddressDTO> findAll(
        List<Long> ids,
        String street,
        String city,
        String town,
        String country,
        String zipcode,
        String login,
        String term,
        Pageable pageable
    ) {
        log.debug("Request to get all Addresses");
        Specification<Address> specification = AddressSpecification.getAddressSpecification(
            ids,
            street,
            city,
            town,
            country,
            zipcode,
            login,
            term
        );
        return addressRepository.findAll(specification, pageable).map(addressMapper::toDto);
    }

    /**
     * Get one address by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AddressDTO> findOne(Long id) {
        log.debug("Request to get Address : {}", id);
        return addressRepository.findById(id).map(addressMapper::toDto);
    }

    /**
     * Delete the address by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Address : {}", id);
        addressRepository.deleteById(id);
    }
}
