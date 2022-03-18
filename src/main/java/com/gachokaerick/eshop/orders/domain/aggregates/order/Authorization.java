package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gachokaerick.eshop.orders.domain.enumeration.PaymentProvider;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Authorization.
 */
@Entity
@Table(name = "jhi_authorization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Authorization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull
    @Column(name = "auth_id", nullable = false)
    private String authId;

    @NotNull
    @Column(name = "currency_code", nullable = false)
    private String currencyCode;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "expiration_time", nullable = false)
    private ZonedDateTime expirationTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_provider", nullable = false)
    private PaymentProvider paymentProvider;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "address", "orderItems", "payments", "buyer" }, allowSetters = true)
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Authorization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
    }

    public Authorization status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthId() {
        return this.authId;
    }

    public Authorization authId(String authId) {
        this.setAuthId(authId);
        return this;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public Authorization currencyCode(String currencyCode) {
        this.setCurrencyCode(currencyCode);
        return this;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Authorization amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ZonedDateTime getExpirationTime() {
        return this.expirationTime;
    }

    public Authorization expirationTime(ZonedDateTime expirationTime) {
        this.setExpirationTime(expirationTime);
        return this;
    }

    public void setExpirationTime(ZonedDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public PaymentProvider getPaymentProvider() {
        return this.paymentProvider;
    }

    public Authorization paymentProvider(PaymentProvider paymentProvider) {
        this.setPaymentProvider(paymentProvider);
        return this;
    }

    public void setPaymentProvider(PaymentProvider paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Authorization order(Order order) {
        this.setOrder(order);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authorization)) {
            return false;
        }
        return id != null && id.equals(((Authorization) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Authorization{" +
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
