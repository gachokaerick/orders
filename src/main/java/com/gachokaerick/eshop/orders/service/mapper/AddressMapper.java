package com.gachokaerick.eshop.orders.service.mapper;

import com.gachokaerick.eshop.orders.domain.Address;
import com.gachokaerick.eshop.orders.service.dto.AddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = { BuyerMapper.class })
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "buyer", source = "buyer", qualifiedByName = "email")
    AddressDTO toDto(Address s);

    @Named("town")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "town", source = "town")
    AddressDTO toDtoTown(Address address);
}
