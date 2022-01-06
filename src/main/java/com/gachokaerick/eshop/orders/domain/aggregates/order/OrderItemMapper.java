package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.gachokaerick.eshop.orders.service.dto.OrderItemDTO;
import com.gachokaerick.eshop.orders.service.mapper.EntityMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrderMapper.class })
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "order", source = "order", qualifiedByName = "id")
    OrderItemDTO toDto(OrderItem s);
}
