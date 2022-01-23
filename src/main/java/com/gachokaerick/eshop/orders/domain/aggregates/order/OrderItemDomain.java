package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.OrderItemDTO;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class OrderItemDomain {

    private static final String domainName = "OrderItem";

    private final OrderItemDTO orderItemDTO;
    private final OrderItemMapperImpl orderItemMapper = new OrderItemMapperImpl();

    private OrderItemDomain(OrderItemBuilder builder) {
        this.orderItemDTO = builder.orderItemDTO;
    }

    public OrderItem toEntity(OrderItem orderItem) {
        if (orderItem == null && orderItemDTO.getId() != null) {
            throw DomainException.throwDomainException(domainName, "Matching orderItem entity from database not found");
        }
        if (orderItem != null && orderItem.getId() == null) {
            throw DomainException.throwDomainException(domainName, "Matching orderItem entity from database does not have an ID");
        }
        if (orderItem != null && !orderItem.getId().equals(orderItemDTO.getId())) {
            throw DomainException.throwDomainException(domainName, "Id mismatch of orderItem entities");
        }

        if (orderItem == null) {
            orderItem = new OrderItem();
        }
        if (orderItemDTO.getId() != null) {
            orderItemMapper.partialUpdate(orderItem, orderItemDTO);
        } else {
            orderItem.setProductName(orderItemDTO.getProductName());
            orderItem.setPictureUrl(orderItemDTO.getPictureUrl());
            orderItem.setUnitPrice(orderItemDTO.getUnitPrice());
            orderItem.setDiscount(orderItemDTO.getDiscount());
            orderItem.setUnits(orderItemDTO.getUnits());
            orderItem.setProductId(orderItemDTO.getProductId());
        }

        return orderItem;
    }

    public void setOrder(OrderItem orderItem, Order order) {
        orderItem.setOrder(order);
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
                throw DomainException.throwDomainException(domainName, "order item is required");
            }

            if (orderItemDTO.getId() == null && orderItemDTO.getProductName() == null) {
                throw DomainException.throwDomainException(domainName, "product name is required");
            }
            if (orderItemDTO.getId() == null && orderItemDTO.getPictureUrl() == null) {
                throw DomainException.throwDomainException(domainName, "picture url is required");
            }
            if (orderItemDTO.getId() == null && orderItemDTO.getUnitPrice() == null) {
                throw DomainException.throwDomainException(domainName, "unit price is required");
            }
            if (orderItemDTO.getId() == null && orderItemDTO.getUnitPrice().compareTo(BigDecimal.ZERO) < 1) {
                throw DomainException.throwDomainException(domainName, "unit price must be greater than zero");
            }
            if (orderItemDTO.getId() == null && orderItemDTO.getDiscount() == null) {
                throw DomainException.throwDomainException(domainName, "discount is required");
            }
            if (orderItemDTO.getId() == null && orderItemDTO.getUnits() == null) {
                throw DomainException.throwDomainException(domainName, "units is required");
            }
            if (orderItemDTO.getId() == null && orderItemDTO.getProductId() == null) {
                throw DomainException.throwDomainException(domainName, "product id is required");
            }
            if (orderItemDTO.getId() == null && orderItemDTO.getOrder() == null) {
                throw DomainException.throwDomainException(domainName, "order for order item is required");
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
