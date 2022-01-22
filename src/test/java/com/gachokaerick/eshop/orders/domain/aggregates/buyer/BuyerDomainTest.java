package com.gachokaerick.eshop.orders.domain.aggregates.buyer;

import static org.junit.jupiter.api.Assertions.*;

import com.gachokaerick.eshop.orders.domain.enumeration.Gender;
import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.BuyerDTO;
import com.gachokaerick.eshop.orders.service.dto.UserDTO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Domain")
class BuyerDomainTest {

    private final String firstName = "john";
    private final String lastName = "doe";
    private final Gender gender = Gender.MALE;
    private final String email = "johndoe@example.com";
    private final String phone = "0712345678";
    private final UserDTO userDTO = new UserDTO("1", "johndoe@example.com");

    BuyerDTO getDTO() {
        return new BuyerDTO(null, firstName, lastName, gender, email, phone, userDTO);
    }

    @Test
    void testWithNulls() {
        assertAll(
            () -> {
                Exception exception = assertThrows(DomainException.class, () -> new BuyerDomain.BuyerBuilder().withDTO(null).build());
                assertTrue(exception.getMessage().contains("buyer dto cannot be null"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.setFirstName(null);
                Exception exception = assertThrows(DomainException.class, () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build());
                assertTrue(exception.getMessage().contains("first name cannot be null"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.setLastName(null);
                Exception exception = assertThrows(DomainException.class, () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build());
                assertTrue(exception.getMessage().contains("last name cannot be null"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.setGender(null);
                Exception exception = assertThrows(DomainException.class, () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build());
                assertTrue(exception.getMessage().contains("gender cannot be null"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.setEmail(null);
                Exception exception = assertThrows(DomainException.class, () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build());
                assertTrue(exception.getMessage().contains("email cannot be null"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.setEmail("example.email");
                Exception exception = assertThrows(DomainException.class, () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build());
                assertTrue(exception.getMessage().contains("email is invalid"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.setPhone(null);
                Exception exception = assertThrows(DomainException.class, () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build());
                assertTrue(exception.getMessage().contains("phone cannot be null"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.setUser(null);
                Exception exception = assertThrows(DomainException.class, () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build());
                assertTrue(exception.getMessage().contains("user dto cannot be null"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.getUser().setId(null);
                Exception exception = assertThrows(DomainException.class, () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build());
                assertTrue(exception.getMessage().contains("user id cannot be null"));
            },
            () -> {
                BuyerDTO buyerDTO = new BuyerDTO();
                buyerDTO.setId(1L);
                buyerDTO.setUser(null);
                assertDoesNotThrow(() -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build());
            }
        );
    }
}
