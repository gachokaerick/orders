package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.gachokaerick.eshop.orders.domain.aggregates.buyer.BuyerMapper;
import com.gachokaerick.eshop.orders.service.dto.OrderDTO;
import com.gachokaerick.eshop.orders.service.mapper.AddressMapper;
import com.gachokaerick.eshop.orders.service.mapper.EntityMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = { AddressMapper.class, BuyerMapper.class })
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "address", source = "address", qualifiedByName = "town")
    @Mapping(target = "buyer", source = "buyer", qualifiedByName = "email")
    OrderDTO toDto(Order s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoId(Order order);
}
