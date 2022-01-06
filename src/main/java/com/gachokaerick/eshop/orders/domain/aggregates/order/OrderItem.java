package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Erick Gachoka
 */
@Entity
@Table(name = "order_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "picture_url", nullable = false)
    private String pictureUrl;

    @NotNull
    @Column(name = "unit_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @NotNull
    @Column(name = "discount", precision = 21, scale = 2, nullable = false)
    private BigDecimal discount;

    @NotNull
    @Column(name = "units", nullable = false)
    private Integer units;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "address", "orderItems", "payments", "buyer" }, allowSetters = true)
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    OrderItem id(Long id) {
        this.setId(id);
        return this;
    }

    void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return this.productName;
    }

    OrderItem productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPictureUrl() {
        return this.pictureUrl;
    }

    OrderItem pictureUrl(String pictureUrl) {
        this.setPictureUrl(pictureUrl);
        return this;
    }

    void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    OrderItem unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscount() {
        return this.discount;
    }

    OrderItem discount(BigDecimal discount) {
        this.setDiscount(discount);
        return this;
    }

    void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getUnits() {
        return this.units;
    }

    OrderItem units(Integer units) {
        this.setUnits(units);
        return this;
    }

    void setUnits(Integer units) {
        this.units = units;
    }

    public Long getProductId() {
        return this.productId;
    }

    OrderItem productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    void setProductId(Long productId) {
        this.productId = productId;
    }

    public Order getOrder() {
        return this.order;
    }

    void setOrder(Order order) {
        this.order = order;
    }

    OrderItem order(Order order) {
        this.setOrder(order);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        return id != null && id.equals(((OrderItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItem{" +
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
