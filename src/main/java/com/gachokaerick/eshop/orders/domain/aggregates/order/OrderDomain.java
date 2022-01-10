package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.OrderDTO;
import com.gachokaerick.eshop.orders.service.dto.OrderItemDTO;
import com.gachokaerick.eshop.orders.service.dto.PaymentDTO;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

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
        OrderItemDomain orderItemDomain = new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build();
        order.addOrderItems(orderItemDomain.getOrderItem());
    }

    public void addPayment(PaymentDTO paymentDTO) {
        PaymentDomain paymentDomain = new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build();
        order.addPayments(paymentDomain.getPayment());
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
        return calculateTotalPaid(order).subtract(calculateItemsTotal(order));
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

            if (orderDTO.getId() == null && orderDTO.getOrderDate() == null) {
                throw DomainException.throwDomainException(domainName, "orderDate cannot be null");
            }
            if (orderDTO.getId() == null && orderDTO.getOrderStatus() == null) {
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
