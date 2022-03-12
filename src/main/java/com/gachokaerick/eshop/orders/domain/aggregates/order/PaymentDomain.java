package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.PaymentDTO;
import java.math.BigDecimal;

public class PaymentDomain {

    private static final String domainName = "Payment";

    private final PaymentDTO paymentDTO;
    private final PaymentMapperImpl paymentMapper = new PaymentMapperImpl();

    private PaymentDomain(PaymentBuilder builder) {
        this.paymentDTO = builder.paymentDTO;
    }

    public Payment toEntity(Payment payment) {
        if (payment == null && paymentDTO.getId() != null) {
            throw DomainException.throwDomainException(domainName, "Matching payment entity from database not found");
        }
        if (payment != null && payment.getId() == null) {
            throw DomainException.throwDomainException(domainName, "Matching payment entity from database does not have an ID");
        }
        if (payment != null && !payment.getId().equals(paymentDTO.getId())) {
            throw DomainException.throwDomainException(domainName, "Id mismatch of payment entities");
        }

        if (payment == null) {
            payment = new Payment();
        }
        if (paymentDTO.getId() != null) {
            paymentMapper.partialUpdate(payment, paymentDTO);
        } else {
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
        }

        return payment;
    }

    public void setOrder(Payment payment, Order order) {
        payment.setOrder(order);
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
                throw DomainException.throwDomainException(domainName, "payment dto is required");
            }

            if (paymentDTO.getId() == null && paymentDTO.getCreateTime() == null) {
                throw DomainException.throwDomainException(domainName, "create time is required");
            }
            if (paymentDTO.getId() == null && paymentDTO.getUpdateTime() == null) {
                throw DomainException.throwDomainException(domainName, "update time is required");
            }
            if (paymentDTO.getId() == null && paymentDTO.getPayerName() == null) {
                throw DomainException.throwDomainException(domainName, "payer name is required");
            }
            if (paymentDTO.getId() == null && paymentDTO.getPayerId() == null) {
                throw DomainException.throwDomainException(domainName, "payer ID is required");
            }
            if (paymentDTO.getId() == null && paymentDTO.getCurrency() == null) {
                throw DomainException.throwDomainException(domainName, "currency is required");
            }
            if (paymentDTO.getId() == null && (paymentDTO.getAmount() == null || paymentDTO.getAmount().compareTo(BigDecimal.ZERO) < 1)) {
                throw DomainException.throwDomainException(domainName, "amount paid must be greater than zero");
            }
            if (paymentDTO.getId() == null && paymentDTO.getOrder() == null) {
                throw DomainException.throwDomainException(domainName, "order for payment is required");
            }
            if (paymentDTO.getId() == null && !paymentDTO.getOrder().getTotal().equals(paymentDTO.getAmount())) {
                throw DomainException.throwDomainException(domainName, "order must be paid for in full");
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
