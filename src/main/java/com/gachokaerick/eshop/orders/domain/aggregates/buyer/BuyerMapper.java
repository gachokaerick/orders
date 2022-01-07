package com.gachokaerick.eshop.orders.domain.aggregates.buyer;

import com.gachokaerick.eshop.orders.domain.aggregates.buyer.Buyer;
import com.gachokaerick.eshop.orders.service.dto.BuyerDTO;
import com.gachokaerick.eshop.orders.service.mapper.EntityMapper;
import com.gachokaerick.eshop.orders.service.mapper.UserMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Buyer} and its DTO {@link BuyerDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface BuyerMapper extends EntityMapper<BuyerDTO, Buyer> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    BuyerDTO toDto(Buyer s);

    @Named("email")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    BuyerDTO toDtoEmail(Buyer buyer);
}