package com.gachokaerick.eshop.orders.service.dto;

import com.gachokaerick.eshop.orders.domain.aggregates.order.Order;
import com.gachokaerick.eshop.orders.domain.enumeration.OrderStatus;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link Order} entity.
 */
@ApiModel(description = "@author Erick Gachoka")
public class OrderDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime orderDate;

    @NotNull
    private OrderStatus orderStatus;

    private String description;

    private AddressDTO address;

    private BuyerDTO buyer;

    private BigDecimal total;

    private BigDecimal balance;

    public OrderDTO() {}

    public OrderDTO(Long id, ZonedDateTime orderDate, OrderStatus orderStatus) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }

    public OrderDTO(Long id, ZonedDateTime orderDate, OrderStatus orderStatus, String description, AddressDTO address, BuyerDTO buyer) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.description = description;
        this.address = address;
        this.buyer = buyer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public BuyerDTO getBuyer() {
        return buyer;
    }

    public void setBuyer(BuyerDTO buyer) {
        this.buyer = buyer;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + id +
            ", orderDate=" + orderDate +
            ", orderStatus=" + orderStatus +
            ", description='" + description + '\'' +
            ", address=" + address +
            ", buyer=" + buyer +
            ", total=" + total +
            ", balance=" + balance +
            '}';
    }
}
