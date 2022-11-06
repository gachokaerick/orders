package com.gachokaerick.eshop.orders.repository;

import com.gachokaerick.eshop.orders.domain.aggregates.buyer.Buyer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Buyer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {}
