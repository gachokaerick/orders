package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gachokaerick.eshop.orders.domain.Address;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.Buyer;
import com.gachokaerick.eshop.orders.domain.enumeration.OrderStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Erick Gachoka
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private ZonedDateTime orderDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "description")
    private String description;

    @JsonIgnoreProperties(value = { "buyer" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Address address;

    @OneToMany(mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @OneToMany(mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @NotNull
    @Column(name = "buyer_id")
    private Long buyerId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    Order id(Long id) {
        this.setId(id);
        return this;
    }

    void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getOrderDate() {
        return this.orderDate;
    }

    Order orderDate(ZonedDateTime orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    Order orderStatus(OrderStatus orderStatus) {
        this.setOrderStatus(orderStatus);
        return this;
    }

    void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDescription() {
        return this.description;
    }

    Order description(String description) {
        this.setDescription(description);
        return this;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return this.address;
    }

    void setAddress(Address address) {
        this.address = address;
    }

    Order address(Address address) {
        this.setAddress(address);
        return this;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.setOrder(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setOrder(this));
        }
        this.orderItems = orderItems;
    }

    Order orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    Order addOrderItems(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
        return this;
    }

    Order removeOrderItems(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setOrder(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setOrder(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setOrder(this));
        }
        this.payments = payments;
    }

    Order payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    Order addPayments(Payment payment) {
        this.payments.add(payment);
        payment.setOrder(this);
        return this;
    }

    Order removePayments(Payment payment) {
        this.payments.remove(payment);
        payment.setOrder(null);
        return this;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    Order buyerId(Long buyerId) {
        this.setBuyerId(buyerId);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
