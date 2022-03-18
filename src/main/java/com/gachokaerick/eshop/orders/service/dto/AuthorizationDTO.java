package com.gachokaerick.eshop.orders.service.dto;

import com.gachokaerick.eshop.orders.domain.aggregates.order.Authorization;
import com.gachokaerick.eshop.orders.domain.enumeration.PaymentProvider;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link Authorization} entity.
 */
public class AuthorizationDTO implements Serializable {

    private Long id;

    @NotNull
    private String status;

    @NotNull
    private String authId;

    @NotNull
    private String currencyCode;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private ZonedDateTime expirationTime;

    @NotNull
    private PaymentProvider paymentProvider;

    private OrderDTO order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ZonedDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(ZonedDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public PaymentProvider getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(PaymentProvider paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorizationDTO)) {
            return false;
        }

        AuthorizationDTO authorizationDTO = (AuthorizationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, authorizationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorizationDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", authId='" + getAuthId() + "'" +
            ", currencyCode='" + getCurrencyCode() + "'" +
            ", amount=" + getAmount() +
            ", expirationTime='" + getExpirationTime() + "'" +
            ", paymentProvider='" + getPaymentProvider() + "'" +
            "}";
    }
}
