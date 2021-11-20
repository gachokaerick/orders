package com.gachokaerick.eshop.orders.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BuyerMapperTest {

    private BuyerMapper buyerMapper;

    @BeforeEach
    public void setUp() {
        buyerMapper = new BuyerMapperImpl();
    }
}
