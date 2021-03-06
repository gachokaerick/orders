package com.gachokaerick.eshop.orders.service.dto;

import com.gachokaerick.eshop.orders.domain.aggregates.order.Payment;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link Payment} entity.
 */
@ApiModel(description = "@author Erick Gachoka")
public class PaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime createTime;

    @NotNull
    private ZonedDateTime updateTime;

    private String paymentStatus;

    private String payerCountryCode;

    private String payerEmail;

    @NotNull
    private String payerName;

    private String payerSurname;

    @NotNull
    private String payerId;

    @NotNull
    private String currency;

    @NotNull
    private BigDecimal amount;

    private String paymentId;

    private OrderDTO order;

    public PaymentDTO() {}

    public PaymentDTO(
        Long id,
        ZonedDateTime createTime,
        ZonedDateTime updateTime,
        String paymentStatus,
        String payerCountryCode,
        String payerEmail,
        String payerName,
        String payerSurname,
        String payerId,
        String currency,
        BigDecimal amount,
        String paymentId,
        OrderDTO order
    ) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.paymentStatus = paymentStatus;
        this.payerCountryCode = payerCountryCode;
        this.payerEmail = payerEmail;
        this.payerName = payerName;
        this.payerSurname = payerSurname;
        this.payerId = payerId;
        this.currency = currency;
        this.amount = amount;
        this.paymentId = paymentId;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPayerCountryCode() {
        return payerCountryCode;
    }

    public void setPayerCountryCode(String payerCountryCode) {
        this.payerCountryCode = payerCountryCode;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerSurname() {
        return payerSurname;
    }

    public void setPayerSurname(String payerSurname) {
        this.payerSurname = payerSurname;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    public PaymentDTO createTime(ZonedDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public PaymentDTO updateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public PaymentDTO paymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public PaymentDTO payerCountryCode(String payerCountryCode) {
        this.payerCountryCode = payerCountryCode;
        return this;
    }

    public PaymentDTO payerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
        return this;
    }

    public PaymentDTO payerName(String payerName) {
        this.payerName = payerName;
        return this;
    }

    public PaymentDTO payerSurname(String payerSurname) {
        this.payerSurname = payerSurname;
        return this;
    }

    public PaymentDTO payerId(String payerId) {
        this.payerId = payerId;
        return this;
    }

    public PaymentDTO currency(String currency) {
        this.currency = currency;
        return this;
    }

    public PaymentDTO amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public PaymentDTO paymentId(String paymentId) {
        this.paymentId = paymentId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + getId() +
            ", createTime='" + getCreateTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", payerCountryCode='" + getPayerCountryCode() + "'" +
            ", payerEmail='" + getPayerEmail() + "'" +
            ", payerName='" + getPayerName() + "'" +
            ", payerSurname='" + getPayerSurname() + "'" +
            ", payerId='" + getPayerId() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", amount=" + getAmount() +
            ", paymentId='" + getPaymentId() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
