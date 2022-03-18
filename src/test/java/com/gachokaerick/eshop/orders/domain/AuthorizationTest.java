package com.gachokaerick.eshop.orders.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.orders.domain.aggregates.order.Authorization;
import com.gachokaerick.eshop.orders.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthorizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Authorization.class);
        Authorization authorization1 = new Authorization();
        authorization1.setId(1L);
        Authorization authorization2 = new Authorization();
        authorization2.setId(authorization1.getId());
        assertThat(authorization1).isEqualTo(authorization2);
        authorization2.setId(2L);
        assertThat(authorization1).isNotEqualTo(authorization2);
        authorization1.setId(null);
        assertThat(authorization1).isNotEqualTo(authorization2);
    }
}
