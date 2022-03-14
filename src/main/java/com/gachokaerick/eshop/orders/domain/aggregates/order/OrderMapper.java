package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.gachokaerick.eshop.orders.domain.aggregates.buyer.BuyerMapper;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.BuyerMapperImpl;
import com.gachokaerick.eshop.orders.service.dto.BuyerDTO;
import com.gachokaerick.eshop.orders.service.dto.OrderDTO;
import com.gachokaerick.eshop.orders.service.mapper.AddressMapper;
import com.gachokaerick.eshop.orders.service.mapper.AddressMapperImpl;
import com.gachokaerick.eshop.orders.service.mapper.EntityMapper;
import java.util.Objects;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = { AddressMapper.class, BuyerMapper.class, OrderItemMapper.class, PaymentMapper.class })
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "address", source = "address", qualifiedByName = "town")
    @Mapping(target = "buyer", source = "buyer", qualifiedByName = "email")
    OrderDTO toDto(Order s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoId(Order order);

    default void partialUpdate(Order entity, OrderDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null && entity.getId() != null) {
            if (!Objects.equals(dto.getId(), entity.getId())) {
                return;
            }
        }

        if (dto.getOrderDate() != null) {
            entity.setOrderDate(dto.getOrderDate());
        }
        if (dto.getOrderStatus() != null) {
            entity.setOrderStatus(dto.getOrderStatus());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
    }

    @AfterMapping
    default void setBalanceAndTotal(@MappingTarget OrderDTO orderDTO, Order order) {
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build();
        orderDTO.setTotal(orderDomain.calculateItemsTotal(order));
        orderDTO.setBalance(orderDomain.calculateOrderBalance(order));
        BuyerDTO buyerDTO = new BuyerDTO();
        buyerDTO.setId(order.getBuyerId());
        orderDTO.setBuyer(buyerDTO);
    }
}
