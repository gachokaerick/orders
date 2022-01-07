package com.gachokaerick.eshop.orders.domain.aggregates.order;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.orders.domain.aggregates.order.OrderItemMapper;
import org.junit.jupiter.api.BeforeEach;

class OrderItemMapperTest {

    private OrderItemMapper orderItemMapper;

    @BeforeEach
    public void setUp() {
        orderItemMapper = new OrderItemMapperImpl();
    }
}
