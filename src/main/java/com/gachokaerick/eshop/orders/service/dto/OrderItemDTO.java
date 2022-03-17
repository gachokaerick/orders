package com.gachokaerick.eshop.orders.service.dto;

import com.gachokaerick.eshop.orders.domain.aggregates.order.OrderItem;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link OrderItem} entity.
 */
@ApiModel(description = "@author Erick Gachoka")
public class OrderItemDTO implements Serializable {

    private Long id;

    @NotNull
    private String productName;

    @NotNull
    private String pictureUrl;

    @NotNull
    private BigDecimal unitPrice;

    @NotNull
    private BigDecimal discount;

    @NotNull
    private Integer units;

    @NotNull
    private Long productId;

    private OrderDTO order;

    public OrderItemDTO() {}

    public OrderItemDTO(
        Long id,
        String productName,
        String pictureUrl,
        BigDecimal unitPrice,
        BigDecimal discount,
        Integer units,
        Long productId,
        OrderDTO order
    ) {
        this.id = id;
        this.productName = productName;
        this.pictureUrl = pictureUrl;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.units = units;
        this.productId = productId;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    public OrderItemDTO productName(String productName) {
        this.productName = productName;
        return this;
    }

    public OrderItemDTO pictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public OrderItemDTO unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public OrderItemDTO discount(BigDecimal discount) {
        this.discount = discount;
        return this;
    }

    public OrderItemDTO units(Integer units) {
        this.units = units;
        return this;
    }

    public OrderItemDTO productId(Long productId) {
        this.productId = productId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItemDTO)) {
            return false;
        }

        OrderItemDTO orderItemDTO = (OrderItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemDTO{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", pictureUrl='" + getPictureUrl() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", discount=" + getDiscount() +
            ", units=" + getUnits() +
            ", productId=" + getProductId() +
            "}";
    }
}
