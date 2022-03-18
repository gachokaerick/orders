package com.gachokaerick.eshop.orders.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.orders.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthorizationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorizationDTO.class);
        AuthorizationDTO authorizationDTO1 = new AuthorizationDTO();
        authorizationDTO1.setId(1L);
        AuthorizationDTO authorizationDTO2 = new AuthorizationDTO();
        assertThat(authorizationDTO1).isNotEqualTo(authorizationDTO2);
        authorizationDTO2.setId(authorizationDTO1.getId());
        assertThat(authorizationDTO1).isEqualTo(authorizationDTO2);
        authorizationDTO2.setId(2L);
        assertThat(authorizationDTO1).isNotEqualTo(authorizationDTO2);
        authorizationDTO1.setId(null);
        assertThat(authorizationDTO1).isNotEqualTo(authorizationDTO2);
    }
}
