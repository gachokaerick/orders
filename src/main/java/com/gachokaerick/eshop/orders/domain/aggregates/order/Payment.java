package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Erick Gachoka
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private ZonedDateTime createTime;

    @NotNull
    @Column(name = "update_time", nullable = false)
    private ZonedDateTime updateTime;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payer_country_code")
    private String payerCountryCode;

    @Column(name = "payer_email")
    private String payerEmail;

    @NotNull
    @Column(name = "payer_name", nullable = false)
    private String payerName;

    @Column(name = "payer_surname")
    private String payerSurname;

    @NotNull
    @Column(name = "payer_id", nullable = false)
    private String payerId;

    @NotNull
    @Column(name = "currency", nullable = false)
    private String currency;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_id")
    private String paymentId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "address", "orderItems", "payments", "buyer" }, allowSetters = true)
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    Payment id(Long id) {
        this.setId(id);
        return this;
    }

    void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreateTime() {
        return this.createTime;
    }

    Payment createTime(ZonedDateTime createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return this.updateTime;
    }

    Payment updateTime(ZonedDateTime updateTime) {
        this.setUpdateTime(updateTime);
        return this;
    }

    void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    Payment paymentStatus(String paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPayerCountryCode() {
        return this.payerCountryCode;
    }

    Payment payerCountryCode(String payerCountryCode) {
        this.setPayerCountryCode(payerCountryCode);
        return this;
    }

    void setPayerCountryCode(String payerCountryCode) {
        this.payerCountryCode = payerCountryCode;
    }

    public String getPayerEmail() {
        return this.payerEmail;
    }

    Payment payerEmail(String payerEmail) {
        this.setPayerEmail(payerEmail);
        return this;
    }

    void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getPayerName() {
        return this.payerName;
    }

    Payment payerName(String payerName) {
        this.setPayerName(payerName);
        return this;
    }

    void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerSurname() {
        return this.payerSurname;
    }

    Payment payerSurname(String payerSurname) {
        this.setPayerSurname(payerSurname);
        return this;
    }

    void setPayerSurname(String payerSurname) {
        this.payerSurname = payerSurname;
    }

    public String getPayerId() {
        return this.payerId;
    }

    Payment payerId(String payerId) {
        this.setPayerId(payerId);
        return this;
    }

    void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getCurrency() {
        return this.currency;
    }

    Payment currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    Payment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentId() {
        return this.paymentId;
    }

    Payment paymentId(String paymentId) {
        this.setPaymentId(paymentId);
        return this;
    }

    void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Order getOrder() {
        return this.order;
    }

    void setOrder(Order order) {
        this.order = order;
    }

    Payment order(Order order) {
        this.setOrder(order);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
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
            "}";
    }
}
