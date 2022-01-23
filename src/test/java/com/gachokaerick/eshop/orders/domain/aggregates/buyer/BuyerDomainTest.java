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

    @Test
    void testUserIsRequiredIfUserIsSet() {
        assertAll(
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.getUser().setId(null);
                Exception exception = assertThrows(DomainException.class, () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build());
                assertTrue(exception.getMessage().contains("user id cannot be null"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.setId(1L);
                buyerDTO.getUser().setId(null);
                Exception exception = assertThrows(DomainException.class, () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build());
                assertTrue(exception.getMessage().contains("user id cannot be null"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.setId(1L);
                buyerDTO.setUser(null);
                assertDoesNotThrow(() -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build());
            }
        );
    }

    @Test
    void toEntityWithNullAndIdIsNull() {
        BuyerDTO buyerDTO = getDTO();
        BuyerDomain buyerDomain = new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build();
        Buyer buyer = buyerDomain.toEntity(null);
        assertAll(() -> {
            assertNull(buyer.getId());
            assertEquals(buyer.getFirstName(), buyerDTO.getFirstName());
            assertEquals(buyer.getLastName(), buyerDTO.getLastName());
            assertEquals(buyer.getGender(), buyerDTO.getGender());
            assertEquals(buyer.getEmail(), buyerDTO.getEmail());
            assertEquals(buyer.getPhone(), buyerDTO.getPhone());
        });
    }

    @Test
    void toEntityBuyerRequiredIfDTOIdNotNull() {
        assertAll(
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.setId(1L);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build().toEntity(null)
                );
                assertTrue(exception.getMessage().contains("Matching buyer entity from database not found"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.setId(1L);
                Buyer buyer = new Buyer();
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build().toEntity(buyer)
                );
                assertTrue(exception.getMessage().contains("Matching buyer entity from database does not have an ID"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                Buyer buyer = new Buyer();
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build().toEntity(buyer)
                );
                assertTrue(exception.getMessage().contains("Matching buyer entity from database does not have an ID"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                Buyer buyer = new Buyer();
                buyer.setId(1L);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build().toEntity(buyer)
                );
                assertTrue(exception.getMessage().contains("Id mismatch of buyer entities"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.setId(2L);
                Buyer buyer = new Buyer();
                buyer.setId(1L);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build().toEntity(buyer)
                );
                assertTrue(exception.getMessage().contains("Id mismatch of buyer entities"));
            },
            () -> {
                BuyerDTO buyerDTO = getDTO();
                buyerDTO.setId(1L);
                Buyer buyer = new Buyer();
                buyer.setId(1L);
                assertDoesNotThrow(() -> new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build().toEntity(buyer));
            }
        );
    }

    @Test
    void toEntityPartialUpdate() {
        BuyerDTO buyerDTO = getDTO();
        buyerDTO.setId(1L);
        Buyer b = new Buyer();
        b.setId(1L);
        BuyerDomain buyerDomain = new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build();
        Buyer buyer = buyerDomain.toEntity(b);
        assertAll(() -> {
            assertEquals(buyer.getId(), buyerDTO.getId());
            assertEquals(buyer.getFirstName(), buyerDTO.getFirstName());
            assertEquals(buyer.getLastName(), buyerDTO.getLastName());
            assertEquals(buyer.getGender(), buyerDTO.getGender());
            assertEquals(buyer.getEmail(), buyerDTO.getEmail());
            assertEquals(buyer.getPhone(), buyerDTO.getPhone());
        });
    }
}
