package com.gachokaerick.eshop.orders.domain.enumeration;

/**
 * @author Erick Gachoka
 */
public enum OrderStatus {
    SUBMITTED,
    AWAITING_VALIDATION,
    DRAFT,
    STOCK_CONFIRMED,
    PAID,
    SHIPPED,
    CANCELLED,
}
