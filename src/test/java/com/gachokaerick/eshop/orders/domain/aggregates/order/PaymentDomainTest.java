package com.gachokaerick.eshop.orders.domain.aggregates.order;

import static org.junit.jupiter.api.Assertions.*;

import com.gachokaerick.eshop.orders.domain.enumeration.OrderStatus;
import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.OrderDTO;
import com.gachokaerick.eshop.orders.service.dto.PaymentDTO;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("domain")
class PaymentDomainTest {

    final ZonedDateTime createTime = ZonedDateTime.now();
    final ZonedDateTime updateTime = ZonedDateTime.now();
    final String paymentStatus = "Complete";
    final String payerCountryCode = "KE";
    final String payerEmail = "johndoe@gmail.com";
    final String payerName = "John";
    final String payerSurname = "Doe";
    final String payerId = "johndoe";
    final String currency = "USD";
    final BigDecimal amount = BigDecimal.valueOf(20);
    final String paymentId = "67893";
    final OrderDTO order = new OrderDTO(1L, ZonedDateTime.now(), OrderStatus.DRAFT);

    PaymentDTO getDTO() {
        return new PaymentDTO(
            null,
            createTime,
            updateTime,
            paymentStatus,
            payerCountryCode,
            payerEmail,
            payerName,
            payerSurname,
            payerId,
            currency,
            amount,
            paymentId,
            order
        );
    }

    @Test
    void testWithNulls() {
        assertAll(
            () -> {
                Exception exception = assertThrows(DomainException.class, () -> new PaymentDomain.PaymentBuilder().withDTO(null).build());
                assertTrue(exception.getMessage().contains("payment dto is required"));
            },
            () -> {
                PaymentDTO paymentDTO = getDTO();
                paymentDTO.setCreateTime(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build()
                );
                assertTrue(exception.getMessage().contains("create time is required"));
            },
            () -> {
                PaymentDTO paymentDTO = getDTO();
                paymentDTO.setUpdateTime(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build()
                );
                assertTrue(exception.getMessage().contains("update time is required"));
            },
            () -> {
                PaymentDTO paymentDTO = getDTO();
                paymentDTO.setPayerName(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build()
                );
                assertTrue(exception.getMessage().contains("payer name is required"));
            },
            () -> {
                PaymentDTO paymentDTO = getDTO();
                paymentDTO.setPayerId(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build()
                );
                assertTrue(exception.getMessage().contains("payer ID is required"));
            },
            () -> {
                PaymentDTO paymentDTO = getDTO();
                paymentDTO.setCurrency(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build()
                );
                assertTrue(exception.getMessage().contains("currency is required"));
            },
            () -> {
                PaymentDTO paymentDTO = getDTO();
                paymentDTO.setAmount(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build()
                );
                assertTrue(exception.getMessage().contains("amount paid must be greater than zero"));
            },
            () -> {
                PaymentDTO paymentDTO = getDTO();
                paymentDTO.setAmount(BigDecimal.ZERO);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build()
                );
                assertTrue(exception.getMessage().contains("amount paid must be greater than zero"));
            },
            () -> {
                PaymentDTO paymentDTO = getDTO();
                paymentDTO.setOrder(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build()
                );
                assertTrue(exception.getMessage().contains("order for payment is required"));
            },
            () -> {
                PaymentDTO paymentDTO = getDTO();
                paymentDTO.getOrder().setId(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build()
                );
                assertTrue(exception.getMessage().contains("order ID for payment is required"));
            }
        );
    }

    @Test
    void testOrderIsRequiredIfOrderIsSet() {
        assertAll(
            () -> {
                PaymentDTO paymentDTO = getDTO();
                paymentDTO.setId(1L);
                paymentDTO.getOrder().setId(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build()
                );
                assertTrue(exception.getMessage().contains("order ID for payment is required"));
            },
            () -> {
                PaymentDTO paymentDTO = getDTO();
                paymentDTO.setId(null);
                paymentDTO.getOrder().setId(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build()
                );
                assertTrue(exception.getMessage().contains("order ID for payment is required"));
            },
            () -> {
                PaymentDTO paymentDTO = getDTO();
                paymentDTO.setId(1L);
                paymentDTO.setOrder(null);
                assertDoesNotThrow(() -> new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build());
            }
        );
    }

    @Test
    void toEntityWithNullAndIdIsNull() {
        PaymentDTO paymentDTO = getDTO();
        PaymentDomain paymentDomain = new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build();
        Payment payment = paymentDomain.toEntity(null);
        assertAll(
            () -> assertNull(payment.getId()),
            () -> assertEquals(payment.getCreateTime(), paymentDTO.getCreateTime()),
            () -> assertEquals(payment.getUpdateTime(), paymentDTO.getUpdateTime()),
            () -> assertEquals(payment.getPaymentStatus(), paymentDTO.getPaymentStatus()),
            () -> assertEquals(payment.getPayerCountryCode(), paymentDTO.getPayerCountryCode()),
            () -> assertEquals(payment.getPayerEmail(), paymentDTO.getPayerEmail()),
            () -> assertEquals(payment.getPayerName(), paymentDTO.getPayerName()),
            () -> assertEquals(payment.getPayerSurname(), paymentDTO.getPayerSurname()),
            () -> assertEquals(payment.getPayerId(), paymentDTO.getPayerId()),
            () -> assertEquals(payment.getCurrency(), paymentDTO.getCurrency()),
            () -> assertEquals(payment.getAmount(), paymentDTO.getAmount()),
            () -> assertEquals(payment.getPaymentId(), paymentDTO.getPaymentId()),
            () -> assertEquals(payment.getOrder().getId(), paymentDTO.getOrder().getId())
        );
    }

    @Test
    void toEntityPaymentRequiredIfDTOIdNotNull() {
        assertAll(
            () -> {
                PaymentDTO paymentDTO = getDTO();
                paymentDTO.setId(1L);
                PaymentDomain paymentDomain = new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build();
                Exception exception = assertThrows(DomainException.class, () -> paymentDomain.toEntity(null));
                assertTrue(exception.getMessage().contains("Matching payment entity from database not found"));
            },
            () -> {
                PaymentDTO paymentDTO = getDTO();
                Payment payment = new Payment();
                PaymentDomain paymentDomain = new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build();
                Exception exception = assertThrows(DomainException.class, () -> paymentDomain.toEntity(payment));
                assertTrue(exception.getMessage().contains("Matching payment entity from database does not have an ID"));
            },
            () -> {
                PaymentDTO paymentDTO = getDTO();
                paymentDTO.setId(1L);
                Payment payment = new Payment();
                payment.setId(2L);
                PaymentDomain paymentDomain = new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build();
                Exception exception = assertThrows(DomainException.class, () -> paymentDomain.toEntity(payment));
                assertTrue(exception.getMessage().contains("Id mismatch of payment entities"));
            }
        );
    }

    @Test
    void toEntityPartialUpdate() {
        PaymentDTO paymentDTO = getDTO();
        paymentDTO.setId(1L);
        Payment p = new Payment();
        p.setId(1L);
        PaymentDomain paymentDomain = new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build();
        Payment payment = paymentDomain.toEntity(p);
        assertAll(
            () -> assertEquals(payment.getId(), paymentDTO.getId()),
            () -> assertEquals(payment.getCreateTime(), paymentDTO.getCreateTime()),
            () -> assertEquals(payment.getUpdateTime(), paymentDTO.getUpdateTime()),
            () -> assertEquals(payment.getPaymentStatus(), paymentDTO.getPaymentStatus()),
            () -> assertEquals(payment.getPayerCountryCode(), paymentDTO.getPayerCountryCode()),
            () -> assertEquals(payment.getPayerEmail(), paymentDTO.getPayerEmail()),
            () -> assertEquals(payment.getPayerName(), paymentDTO.getPayerName()),
            () -> assertEquals(payment.getPayerSurname(), paymentDTO.getPayerSurname()),
            () -> assertEquals(payment.getPayerId(), paymentDTO.getPayerId()),
            () -> assertEquals(payment.getCurrency(), paymentDTO.getCurrency()),
            () -> assertEquals(payment.getAmount(), paymentDTO.getAmount()),
            () -> assertEquals(payment.getPaymentId(), paymentDTO.getPaymentId())
        );
    }

    @Test
    void setOrder() {}
}
