package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.gachokaerick.eshop.orders.domain.Address;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.Buyer;
import com.gachokaerick.eshop.orders.domain.enumeration.OrderStatus;
import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.OrderDTO;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class OrderDomain {

    private static final String domainName = "Order";

    private final OrderDTO orderDTO;
    private final Order order = new Order();
    private final OrderMapperImpl orderMapper = new OrderMapperImpl();

    private OrderDomain(OrderBuilder builder) {
        orderDTO = builder.orderDTO;
    }

    public Order toEntity(Order order) {
        if (order == null) {
            order = new Order();
        }

        if (orderDTO.getId() != null) {
            order.setId(orderDTO.getId());
            orderMapper.partialUpdate(order, orderDTO);
        } else {
            order.setOrderDate(orderDTO.getOrderDate());
            order.setOrderStatus(OrderStatus.DRAFT);
            order.setDescription(orderDTO.getDescription());
            order.setAddress(orderDTO.getAddress().toEntity());
            order.setBuyerId(orderDTO.getBuyer().getId());
        }

        return order;
    }

    public BigDecimal calculateItemsTotal(Order order) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem orderItem : order.getOrderItems()) {
            BigDecimal itemTotal =
                (orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getUnits()))).subtract(orderItem.getDiscount());
            total = total.add(itemTotal);
        }
        return total;
    }

    public BigDecimal calculateTotalPaid(Order order) {
        BigDecimal total = BigDecimal.ZERO;
        for (Payment payment : order.getPayments()) {
            total = total.add(payment.getAmount());
        }
        return total;
    }

    public BigDecimal calculateOrderBalance(Order order) {
        return calculateItemsTotal(order).subtract(calculateTotalPaid(order));
    }

    public Order setAddress(Order order, Address address) {
        if (address == null || address.getId() == null) {
            throw DomainException.throwDomainException(domainName, "Address for an order must exist");
        }
        order.setAddress(address);
        return order;
    }

    public void addOrderItem(Order order, OrderItem orderItem) {
        order.addOrderItems(orderItem);
    }

    public void removeOrderItem(Order order, OrderItem orderItem) {
        order.removeOrderItems(orderItem);
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
                throw DomainException.throwDomainException(domainName, "catalogItemDTO is required");
            }

            if (orderDTO.getId() == null && orderDTO.getOrderDate() == null) {
                throw DomainException.throwDomainException(domainName, "orderDate is required");
            }
            if (orderDTO.getId() == null && orderDTO.getOrderStatus() == null) {
                throw DomainException.throwDomainException(domainName, "orderStatus is required");
            }
            if (orderDTO.getId() == null && orderDTO.getAddress() == null) {
                throw DomainException.throwDomainException(domainName, "address is required");
            }
            if (orderDTO.getAddress() != null && orderDTO.getAddress().getId() == null) {
                throw DomainException.throwDomainException(domainName, "address id is required");
            }
            if (orderDTO.getId() == null && orderDTO.getBuyer() == null) {
                throw DomainException.throwDomainException(domainName, "buyer is required");
            }
            if (orderDTO.getBuyer() != null && orderDTO.getBuyer().getId() == null) {
                throw DomainException.throwDomainException(domainName, "buyer id is required");
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
