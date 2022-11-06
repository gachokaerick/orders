package com.gachokaerick.eshop.orders.repository;

import com.gachokaerick.eshop.orders.domain.aggregates.order.Order;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {}
