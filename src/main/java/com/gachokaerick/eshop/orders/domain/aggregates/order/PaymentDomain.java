package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.PaymentDTO;
import java.math.BigDecimal;

public class PaymentDomain {

    private static final String domainName = "Payment";

    private final PaymentDTO paymentDTO;
    private final OrderDomain orderDomain;

    private PaymentDomain(PaymentBuilder builder) {
        this.paymentDTO = builder.paymentDTO;
        if (paymentDTO.getOrder() != null) {
            orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(paymentDTO.getOrder()).build();
        } else {
            orderDomain = null;
        }
    }

    public Payment getPayment() {
        Payment payment = new Payment();
        payment.setId(paymentDTO.getId());
        payment.setCreateTime(paymentDTO.getCreateTime());
        payment.setUpdateTime(paymentDTO.getUpdateTime());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());
        payment.setPayerCountryCode(paymentDTO.getPayerCountryCode());
        payment.setPayerEmail(paymentDTO.getPayerEmail());
        payment.setPayerName(paymentDTO.getPayerName());
        payment.setPayerSurname(paymentDTO.getPayerSurname());
        payment.setPayerId(paymentDTO.getPayerId());
        payment.setCurrency(paymentDTO.getCurrency());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentId(paymentDTO.getPaymentId());
        if (orderDomain != null) {
            payment.setOrder(orderDomain.getOrder());
        }
        return payment;
    }

    public static class PaymentBuilder {

        private PaymentDTO paymentDTO;

        public PaymentBuilder() {}

        public PaymentBuilder withDTO(PaymentDTO paymentDTO) {
            this.paymentDTO = paymentDTO;
            return this;
        }

        private boolean isAcceptable() {
            if (paymentDTO == null) {
                throw DomainException.throwDomainException(domainName, "payment dto cannot be null");
            }

            if (paymentDTO.getId() == null && paymentDTO.getCreateTime() == null) {
                throw DomainException.throwDomainException(domainName, "create time cannot be null");
            }
            if (paymentDTO.getId() == null && paymentDTO.getUpdateTime() == null) {
                throw DomainException.throwDomainException(domainName, "update time cannot be null");
            }
            if (paymentDTO.getId() == null && paymentDTO.getPayerName() == null) {
                throw DomainException.throwDomainException(domainName, "payer name cannot be null");
            }
            if (paymentDTO.getId() == null && paymentDTO.getPayerId() == null) {
                throw DomainException.throwDomainException(domainName, "payer ID cannot be null");
            }
            if (paymentDTO.getId() == null && paymentDTO.getCurrency() == null) {
                throw DomainException.throwDomainException(domainName, "currency cannot be null");
            }
            if (paymentDTO.getId() == null && (paymentDTO.getAmount() == null || paymentDTO.getAmount().compareTo(BigDecimal.ZERO) < 1)) {
                throw DomainException.throwDomainException(domainName, "amount paid must be greater than zero");
            }
            if (paymentDTO.getId() == null && paymentDTO.getOrder() == null) {
                throw DomainException.throwDomainException(domainName, "order for payment cannot be null");
            }
            if (paymentDTO.getOrder() != null && paymentDTO.getOrder().getId() == null) {
                throw DomainException.throwDomainException(domainName, "order ID for payment cannot be null");
            }

            return true;
        }

        public PaymentDomain build() {
            if (isAcceptable()) {
                return new PaymentDomain(this);
            }
            throw DomainException.throwDomainException(domainName, "cannot create a payment with invalid data");
        }
    }
}
