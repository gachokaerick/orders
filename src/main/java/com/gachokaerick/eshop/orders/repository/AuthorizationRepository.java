package com.gachokaerick.eshop.orders.repository;

import com.gachokaerick.eshop.orders.domain.aggregates.order.Authorization;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Authorization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorizationRepository extends JpaRepository<Authorization, Long> {}
