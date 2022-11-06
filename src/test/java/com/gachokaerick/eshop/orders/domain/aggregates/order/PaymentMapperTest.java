package com.gachokaerick.eshop.orders.domain.aggregates.order;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.orders.domain.aggregates.order.PaymentMapper;
import org.junit.jupiter.api.BeforeEach;

class PaymentMapperTest {

    private PaymentMapper paymentMapper;

    @BeforeEach
    public void setUp() {
        paymentMapper = new PaymentMapperImpl();
    }
}
