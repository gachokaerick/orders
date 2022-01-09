package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.OrderDTO;
import com.gachokaerick.eshop.orders.service.dto.OrderItemDTO;
import javax.validation.constraints.NotNull;
import liquibase.pro.packaged.O;

public class OrderDomain {

    private static final String domainName = "Order";

    private final OrderDTO orderDTO;
    private final Order order = new Order();

    private OrderDomain(OrderBuilder builder) {
        orderDTO = builder.orderDTO;
    }

    public Order getOrder() {
        order.setId(order.getId());
        order.setOrderDate(orderDTO.getOrderDate());
        order.setOrderStatus(orderDTO.getOrderStatus());
        order.setDescription(orderDTO.getDescription());
        if (orderDTO.getAddress() != null) {
            order.setAddress(orderDTO.getAddress().toEntity());
        }
        if (orderDTO.getBuyer() != null) {
            order.setBuyerId(orderDTO.getBuyer().getId());
        }
        return order;
    }

    public void addOrderItem(OrderItemDTO orderItemDTO) {
        Order order = getOrder();
        OrderItemDomain orderItemDomain = new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build();
        order.addOrderItems(orderItemDomain.getOrderItem());
    }

    public static class OrderBuilder {

        private OrderDTO orderDTO;

        public OrderBuilder() {}

        public OrderBuilder withOrderDTO(@NotNull OrderDTO orderDTO) {
            this.orderDTO = orderDTO;
            return this;
        }

        private boolean isAcceptable() {
            if (this.orderDTO == null) {
                throw DomainException.throwDomainException(domainName, "catalogItemDTO cannot be null");
            }

            if (orderDTO.getOrderDate() == null) {
                throw DomainException.throwDomainException(domainName, "orderDate cannot be null");
            }
            if (orderDTO.getOrderStatus() == null) {
                throw DomainException.throwDomainException(domainName, "orderStatus cannot be null");
            }
            if (orderDTO.getId() == null && orderDTO.getAddress() == null) {
                throw DomainException.throwDomainException(domainName, "address cannot be null");
            }
            if (orderDTO.getId() == null && orderDTO.getAddress().getId() == null) {
                throw DomainException.throwDomainException(domainName, "address id cannot be null");
            }
            if (orderDTO.getId() == null && orderDTO.getBuyer() == null) {
                throw DomainException.throwDomainException(domainName, "buyer cannot be null");
            }
            if (orderDTO.getBuyer() != null && orderDTO.getBuyer().getId() == null) {
                throw DomainException.throwDomainException(domainName, "buyer id cannot be null");
            }
            return true;
        }

        public OrderDomain build() {
            if (isAcceptable()) {
                return new OrderDomain(this);
            }
            throw DomainException.throwDomainException(domainName, "cannot build an order with invalid data");
        }
    }
}
