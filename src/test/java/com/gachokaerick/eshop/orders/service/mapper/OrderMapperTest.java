package com.gachokaerick.eshop.orders.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.orders.domain.aggregates.order.OrderMapper;
import org.junit.jupiter.api.BeforeEach;

class OrderMapperTest {

    private OrderMapper orderMapper;

    @BeforeEach
    public void setUp() {
        orderMapper = new OrderMapperImpl();
    }
}
