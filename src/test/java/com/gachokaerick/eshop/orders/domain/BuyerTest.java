package com.gachokaerick.eshop.orders.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.orders.domain.aggregates.buyer.Buyer;
import com.gachokaerick.eshop.orders.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BuyerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Buyer.class);
        Buyer buyer1 = new Buyer();
        buyer1.setId(1L);
        Buyer buyer2 = new Buyer();
        buyer2.setId(buyer1.getId());
        assertThat(buyer1).isEqualTo(buyer2);
        buyer2.setId(2L);
        assertThat(buyer1).isNotEqualTo(buyer2);
        buyer1.setId(null);
        assertThat(buyer1).isNotEqualTo(buyer2);
    }
}
