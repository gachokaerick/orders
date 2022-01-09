package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.OrderItemDTO;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class OrderItemDomain {

    private static final String domainName = "OrderItem";

    private final OrderItemDTO orderItemDTO;
    private final OrderDomain orderDomain;

    private OrderItemDomain(OrderItemBuilder builder) {
        this.orderItemDTO = builder.orderItemDTO;
        if (orderItemDTO.getOrder() != null) {
            this.orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderItemDTO.getOrder()).build();
        } else {
            orderDomain = null;
        }
    }

    public OrderItem getOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemDTO.getId());
        orderItem.setProductName(orderItemDTO.getProductName());
        orderItem.setPictureUrl(orderItemDTO.getPictureUrl());
        orderItem.setUnitPrice(orderItemDTO.getUnitPrice());
        orderItem.setDiscount(orderItemDTO.getDiscount());
        orderItem.setUnits(orderItem.getUnits());
        orderItem.setProductId(orderItem.getProductId());
        if (orderDomain != null) {
            orderItem.setOrder(orderDomain.getOrder());
        }

        return orderItem;
    }

    public static class OrderItemBuilder {

        private OrderItemDTO orderItemDTO;

        public OrderItemBuilder() {}

        public OrderItemBuilder withDTO(@NotNull OrderItemDTO orderItemDTO) {
            this.orderItemDTO = orderItemDTO;
            return this;
        }

        private boolean isAcceptable() {
            if (orderItemDTO == null) {
                throw DomainException.throwDomainException(domainName, "order item cannot be null");
            }

            if (orderItemDTO.getProductName() == null) {
                throw DomainException.throwDomainException(domainName, "product name cannot be null");
            }
            if (orderItemDTO.getPictureUrl() == null) {
                throw DomainException.throwDomainException(domainName, "picture url cannot be null");
            }
            if (orderItemDTO.getUnitPrice() == null) {
                throw DomainException.throwDomainException(domainName, "unit price cannot be null");
            }
            if (orderItemDTO.getUnitPrice().compareTo(BigDecimal.ZERO) < 1) {
                throw DomainException.throwDomainException(domainName, "unit price must be greater than zero");
            }
            if (orderItemDTO.getDiscount() == null) {
                throw DomainException.throwDomainException(domainName, "discount cannot be null");
            }
            if (orderItemDTO.getUnits() == null) {
                throw DomainException.throwDomainException(domainName, "units cannot be null");
            }
            if (orderItemDTO.getProductId() == null) {
                throw DomainException.throwDomainException(domainName, "product id cannot be null");
            }
            if (orderItemDTO.getId() == null && orderItemDTO.getOrder() == null) {
                throw DomainException.throwDomainException(domainName, "order for order item cannot be null");
            }
            if (orderItemDTO.getOrder() != null && orderItemDTO.getOrder().getId() == null) {
                throw DomainException.throwDomainException(domainName, "order entity for an order item must have an ID");
            }

            return true;
        }

        public OrderItemDomain build() {
            if (isAcceptable()) {
                return new OrderItemDomain(this);
            }
            throw DomainException.throwDomainException(domainName, "cannot create an order item with invalid data");
        }
    }
}
