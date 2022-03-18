package com.gachokaerick.eshop.orders.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthorizationMapperTest {

    private AuthorizationMapper authorizationMapper;

    @BeforeEach
    public void setUp() {
        authorizationMapper = new AuthorizationMapperImpl();
    }
}
