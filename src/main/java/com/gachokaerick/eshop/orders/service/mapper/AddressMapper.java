package com.gachokaerick.eshop.orders.service.mapper;

import com.gachokaerick.eshop.orders.domain.Address;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.Buyer;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.BuyerDomain;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.BuyerMapper;
import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.AddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = { BuyerMapper.class })
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "buyer", source = "buyer")
    AddressDTO toDto(Address s);

    @Named("town")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "town", source = "town")
    AddressDTO toDtoTown(Address address);

    default Address toEntity(AddressDTO dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getId() == null && dto.getBuyer() == null) {
            throw DomainException.throwDomainException("AddressVO", "buyer must exist for new address item");
        }
        if (dto.getBuyer() != null && dto.getBuyer().getId() == null) {
            throw DomainException.throwDomainException("AddressVO", "buyer entity for an address item must have an ID");
        }

        Address address = new Address();

        address.setId(dto.getId());
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setTown(dto.getTown());
        address.setCountry(dto.getCountry());
        address.setZipcode(dto.getZipcode());
        return address;
    }

    default void partialUpdate(Address entity, AddressDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getStreet() != null) {
            entity.setStreet(dto.getStreet());
        }
        if (dto.getCity() != null) {
            entity.setCity(dto.getCity());
        }
        if (dto.getTown() != null) {
            entity.setTown(dto.getTown());
        }
        if (dto.getCountry() != null) {
            entity.setCountry(dto.getCountry());
        }
        if (dto.getZipcode() != null) {
            entity.setZipcode(dto.getZipcode());
        }
    }
}
