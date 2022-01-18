package com.gachokaerick.eshop.orders.domain.aggregates.order;

import static org.junit.jupiter.api.Assertions.*;

import com.gachokaerick.eshop.orders.domain.enumeration.OrderStatus;
import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.OrderDTO;
import com.gachokaerick.eshop.orders.service.dto.OrderItemDTO;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("domain")
class OrderItemDomainTest {

    final Long id = 1L;
    final String productName = "pants";
    final String pictureUrl = "http://pants";
    final BigDecimal unitPrice = BigDecimal.valueOf(10);
    final BigDecimal discount = BigDecimal.valueOf(2);
    final Integer units = 1;
    final Long productId = 323L;
    final OrderDTO order1 = new OrderDTO(1L, ZonedDateTime.now(), OrderStatus.DRAFT);

    final Long id2 = 2L;
    final String productName2 = "socks";
    final String pictureUrl2 = "https://socks";
    final BigDecimal unitPrice2 = BigDecimal.valueOf(1);
    final BigDecimal discount2 = BigDecimal.ZERO;
    final Integer units2 = 2;
    final Long productId2 = 56789L;
    final OrderDTO order2 = new OrderDTO(2L, ZonedDateTime.now(), OrderStatus.CANCELLED);

    OrderItemDTO getDTO() {
        return new OrderItemDTO(null, productName, pictureUrl, unitPrice, discount, units, productId, order1);
    }

    @Test
    void testWithNulls() {
        assertAll(
            () -> {
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderItemDomain.OrderItemBuilder().withDTO(null).build()
                );
                assertTrue(exception.getMessage().contains("order item is required"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.setProductName(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build()
                );
                assertTrue(exception.getMessage().contains("product name is required"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.setPictureUrl(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build()
                );
                assertTrue(exception.getMessage().contains("picture url is required"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.setUnitPrice(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build()
                );
                assertTrue(exception.getMessage().contains("unit price is required"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.setUnitPrice(BigDecimal.ZERO);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build()
                );
                assertTrue(exception.getMessage().contains("unit price must be greater than zero"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.setUnitPrice(BigDecimal.valueOf(-1));
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build()
                );
                assertTrue(exception.getMessage().contains("unit price must be greater than zero"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.setDiscount(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build()
                );
                assertTrue(exception.getMessage().contains("discount is required"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.setUnits(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build()
                );
                assertTrue(exception.getMessage().contains("units is required"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.setProductId(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build()
                );
                assertTrue(exception.getMessage().contains("product id is required"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.setOrder(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build()
                );
                assertTrue(exception.getMessage().contains("order for order item is required"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.getOrder().setId(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build()
                );
                assertTrue(exception.getMessage().contains("order entity for an order item must have an ID"));
            }
        );
    }
}
