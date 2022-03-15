package com.gachokaerick.eshop.orders.domain.aggregates.order;

import static org.junit.jupiter.api.Assertions.*;
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

    final String productName = "pants";
    final String pictureUrl = "http://pants";
    final BigDecimal unitPrice = BigDecimal.valueOf(10);
    final BigDecimal discount = BigDecimal.valueOf(2);
    final Integer units = 1;
    final Long productId = 323L;
    final OrderDTO order1 = new OrderDTO(1L, ZonedDateTime.now(), OrderStatus.DRAFT);

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

    @Test
    void testOrderIsRequiredIfOrderIsSet() {
        assertAll(
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.setId(1L);
                orderItemDTO.getOrder().setId(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build()
                );
                assertTrue(exception.getMessage().contains("order entity for an order item must have an ID"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.getOrder().setId(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build()
                );
                assertTrue(exception.getMessage().contains("order entity for an order item must have an ID"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.setId(1L);
                orderItemDTO.setOrder(null);
                assertDoesNotThrow(() -> new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build());
            }
        );
    }

    @Test
    void toEntityWithNullAndIdIsNull() {
        OrderItemDTO orderItemDTO = getDTO();
        OrderItemDomain orderItemDomain = new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build();
        OrderItem orderItem = orderItemDomain.toEntity(null);
        assertAll(
            () -> assertNull(orderItem.getId()),
            () -> assertEquals(orderItem.getProductName(), orderItemDTO.getProductName()),
            () -> assertEquals(orderItem.getPictureUrl(), orderItemDTO.getPictureUrl()),
            () -> assertEquals(orderItem.getUnitPrice(), orderItemDTO.getUnitPrice()),
            () -> assertEquals(orderItem.getDiscount(), orderItemDTO.getDiscount()),
            () -> assertEquals(orderItem.getUnits(), orderItemDTO.getUnits()),
            () -> assertEquals(orderItem.getProductId(), orderItemDTO.getProductId())
        );
    }

    @Test
    void toEntityOrderItemRequiredIfDTOIdNotNull() {
        assertAll(
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.setId(1L);
                OrderItemDomain orderItemDomain = new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build();
                Exception exception = assertThrows(DomainException.class, () -> orderItemDomain.toEntity(null));
                assertTrue(exception.getMessage().contains("Matching orderItem entity from database not found"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                OrderItemDomain orderItemDomain = new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build();
                OrderItem orderItem = new OrderItem();
                Exception exception = assertThrows(DomainException.class, () -> orderItemDomain.toEntity(orderItem));
                assertTrue(exception.getMessage().contains("Matching orderItem entity from database does not have an ID"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.setId(1L);
                OrderItemDomain orderItemDomain = new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build();
                OrderItem orderItem = new OrderItem();
                Exception exception = assertThrows(DomainException.class, () -> orderItemDomain.toEntity(orderItem));
                assertTrue(exception.getMessage().contains("Matching orderItem entity from database does not have an ID"));
            },
            () -> {
                OrderItemDTO orderItemDTO = getDTO();
                orderItemDTO.setId(1L);
                OrderItemDomain orderItemDomain = new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build();
                OrderItem orderItem = new OrderItem();
                orderItem.setId(2L);
                Exception exception = assertThrows(DomainException.class, () -> orderItemDomain.toEntity(orderItem));
                assertTrue(exception.getMessage().contains("Id mismatch of orderItem entities"));
            }
        );
    }

    @Test
    void toEntityPartialUpdate() {
        OrderItemDTO orderItemDTO = getDTO();
        orderItemDTO.setId(1L);
        OrderItem oi = new OrderItem();
        oi.setId(1L);
        OrderItemDomain orderItemDomain = new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build();
        OrderItem orderItem = orderItemDomain.toEntity(oi);
        assertAll(
            () -> assertEquals(orderItem.getId(), orderItemDTO.getId()),
            () -> assertEquals(orderItem.getProductName(), orderItemDTO.getProductName()),
            () -> assertEquals(orderItem.getPictureUrl(), orderItemDTO.getPictureUrl()),
            () -> assertEquals(orderItem.getUnitPrice(), orderItemDTO.getUnitPrice()),
            () -> assertEquals(orderItem.getDiscount(), orderItemDTO.getDiscount()),
            () -> assertEquals(orderItem.getUnits(), orderItemDTO.getUnits()),
            () -> assertEquals(orderItem.getProductId(), orderItemDTO.getProductId())
        );
    }
}
