package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.gachokaerick.eshop.orders.service.dto.OrderItemDTO;
import com.gachokaerick.eshop.orders.service.mapper.EntityMapper;
import java.util.Objects;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrderMapper.class })
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "order", source = "order", qualifiedByName = "id")
    OrderItemDTO toDto(OrderItem s);

    default void partialUpdate(OrderItem entity, OrderItemDTO dto) {
        OrderMapperImpl orderMapper = new OrderMapperImpl();
        if (dto == null) {
            return;
        }

        if (dto.getId() != null && entity.getId() != null) {
            if (!Objects.equals(dto.getId(), entity.getId())) {
                return;
            }
        }

        if (dto.getProductName() != null) {
            entity.setProductName(dto.getProductName());
        }
        if (dto.getPictureUrl() != null) {
            entity.setPictureUrl(dto.getPictureUrl());
        }
        if (dto.getUnitPrice() != null) {
            entity.setUnitPrice(dto.getUnitPrice());
        }
        if (dto.getDiscount() != null) {
            entity.setDiscount(dto.getDiscount());
        }
        if (dto.getUnits() != null) {
            entity.setUnits(dto.getUnits());
        }
        if (dto.getProductId() != null) {
            entity.setProductId(dto.getProductId());
        }
    }
}
