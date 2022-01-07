package com.gachokaerick.eshop.orders.domain.aggregates.buyer;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.orders.domain.aggregates.buyer.BuyerMapper;
import org.junit.jupiter.api.BeforeEach;

class BuyerMapperTest {

    private BuyerMapper buyerMapper;

    @BeforeEach
    public void setUp() {
        buyerMapper = new BuyerMapperImpl();
    }
}
