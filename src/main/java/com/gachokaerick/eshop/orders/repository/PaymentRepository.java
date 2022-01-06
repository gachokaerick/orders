package com.gachokaerick.eshop.orders.repository;

import com.gachokaerick.eshop.orders.domain.aggregates.order.Payment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {}
