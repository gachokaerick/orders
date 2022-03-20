package com.gachokaerick.eshop.orders.domain.aggregates.order;

import static org.junit.jupiter.api.Assertions.*;

import com.gachokaerick.eshop.orders.domain.enumeration.OrderStatus;
import com.gachokaerick.eshop.orders.domain.enumeration.PaymentProvider;
import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.AuthorizationDTO;
import com.gachokaerick.eshop.orders.service.dto.OrderDTO;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("domain")
class AuthorizationDomainTest {

    final String status = "COMPLETE";
    final String authId = "ABC";
    final String currencyCode = "USD";
    final BigDecimal amount = BigDecimal.valueOf(20);
    final ZonedDateTime expirationTime = ZonedDateTime.now();
    final PaymentProvider paymentProvider = PaymentProvider.PAYPAL;

    AuthorizationDTO getDTO() {
        return new AuthorizationDTO(
            null,
            status,
            authId,
            currencyCode,
            amount,
            expirationTime,
            paymentProvider,
            new OrderDTO(1L, ZonedDateTime.now(), OrderStatus.DRAFT)
        );
    }

    @Test
    void testWithNulls() {
        assertAll(
            () -> {
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new AuthorizationDomain.AuthorizationBuilder().withDTO(null).build()
                );
                assertTrue(exception.getMessage().contains("authorization is required"));
            },
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.setStatus(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build()
                );
                assertTrue(exception.getMessage().contains("authorization status is required"));
            },
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.setAuthId(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build()
                );
                assertTrue(exception.getMessage().contains("authorization authID is required"));
            },
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.setCurrencyCode(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build()
                );
                assertTrue(exception.getMessage().contains("authorization currency code is required"));
            },
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.setAmount(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build()
                );
                assertTrue(exception.getMessage().contains("authorization amount is required"));
            },
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.setExpirationTime(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build()
                );
                assertTrue(exception.getMessage().contains("authorization expiration time is required"));
            },
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.setPaymentProvider(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build()
                );
                assertTrue(exception.getMessage().contains("authorization payment provider is required"));
            },
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.setOrder(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build()
                );
                assertTrue(exception.getMessage().contains("authorization order is required"));
            },
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.getOrder().setId(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build()
                );
                assertTrue(exception.getMessage().contains("authorization order ID is required"));
            }
        );
    }

    @Test
    void testNonRequiredFieldsForEntityWithID() {
        assertAll(
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.setId(1L);
                dto.setStatus(null);
                assertDoesNotThrow(() -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build());
            },
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.setId(1L);
                dto.setAuthId(null);
                assertDoesNotThrow(() -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build());
            },
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.setId(1L);
                dto.setCurrencyCode(null);
                assertDoesNotThrow(() -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build());
            },
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.setId(1L);
                dto.setAmount(null);
                assertDoesNotThrow(() -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build());
            },
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.setId(1L);
                dto.setExpirationTime(null);
                assertDoesNotThrow(() -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build());
            },
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.setId(1L);
                dto.setPaymentProvider(null);
                assertDoesNotThrow(() -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build());
            },
            () -> {
                AuthorizationDTO dto = getDTO();
                dto.setId(1L);
                dto.setOrder(null);
                assertDoesNotThrow(() -> new AuthorizationDomain.AuthorizationBuilder().withDTO(dto).build());
            }
        );
    }
}
