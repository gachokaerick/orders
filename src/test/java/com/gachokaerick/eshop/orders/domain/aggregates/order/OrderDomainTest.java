package com.gachokaerick.eshop.orders.domain.aggregates.order;

import static org.junit.jupiter.api.Assertions.*;

import com.gachokaerick.eshop.orders.domain.enumeration.Gender;
import com.gachokaerick.eshop.orders.domain.enumeration.OrderStatus;
import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.AddressDTO;
import com.gachokaerick.eshop.orders.service.dto.BuyerDTO;
import com.gachokaerick.eshop.orders.service.dto.OrderDTO;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("domain")
class OrderDomainTest {

    final ZonedDateTime orderDate = ZonedDateTime.now();
    final OrderStatus orderStatus = OrderStatus.DRAFT;
    final String description = "test";
    final BuyerDTO buyer = new BuyerDTO(2L, "john", "doe", Gender.MALE, "johndoe@gmail.com", "+254712345678");
    final AddressDTO address = new AddressDTO(3L, "mfangano", "nairobi", "cbd", "kenya", "01101", buyer);

    OrderDTO getDTO() {
        return new OrderDTO(null, orderDate, orderStatus, description, address, buyer);
    }

    @Test
    void testWithNulls() {
        assertAll(
            () -> {
                Exception exception = assertThrows(DomainException.class, () -> new OrderDomain.OrderBuilder().withOrderDTO(null).build());
                assertTrue(exception.getMessage().contains("order is required"));
            },
            () -> {
                OrderDTO orderDTO = getDTO();
                orderDTO.setOrderDate(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build()
                );
                assertTrue(exception.getMessage().contains("orderDate is required"));
            },
            () -> {
                OrderDTO orderDTO = getDTO();
                orderDTO.setAddress(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build()
                );
                assertTrue(exception.getMessage().contains("address is required"));
            },
            () -> {
                OrderDTO orderDTO = getDTO();
                orderDTO.setBuyer(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build()
                );
                assertTrue(exception.getMessage().contains("buyer is required"));
            }
        );
    }

    @Test
    void testAddressIsRequiredIfAddressIsSet() {
        assertAll(
            () -> {
                OrderDTO orderDTO = getDTO();
                orderDTO.getAddress().setId(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build()
                );
                assertTrue(exception.getMessage().contains("address id is required"));
            },
            () -> {
                OrderDTO orderDTO = getDTO();
                orderDTO.setId(1L);
                orderDTO.getAddress().setId(null);
                Exception exception = assertThrows(
                    DomainException.class,
                    () -> new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build()
                );
                assertTrue(exception.getMessage().contains("address id is required"));
            },
            () -> {
                OrderDTO orderDTO = getDTO();
                orderDTO.setId(1L);
                orderDTO.setAddress(null);
                assertDoesNotThrow(() -> new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build());
            }
        );
    }

    @Test
    void toEntityOrderRequiredIfOrderIDIsNotNull() {
        assertAll(
            () -> {
                OrderDTO orderDTO = getDTO();
                orderDTO.setId(1L);
                OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build();
                Exception exception = assertThrows(DomainException.class, () -> orderDomain.toEntity(null));
                assertTrue(exception.getMessage().contains("Matching order entity from database not found"));
            },
            () -> {
                OrderDTO orderDTO = getDTO();
                orderDTO.setId(1L);
                OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build();
                Order order = new Order();
                Exception exception = assertThrows(DomainException.class, () -> orderDomain.toEntity(order));
                assertTrue(exception.getMessage().contains("Matching order entity from database does not have an ID"));
            },
            () -> {
                OrderDTO orderDTO = getDTO();
                OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build();
                Order order = new Order();
                Exception exception = assertThrows(DomainException.class, () -> orderDomain.toEntity(order));
                assertTrue(exception.getMessage().contains("Matching order entity from database does not have an ID"));
            },
            () -> {
                OrderDTO orderDTO = getDTO();
                orderDTO.setId(1L);
                OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build();
                Order order = new Order();
                order.setId(2L);
                Exception exception = assertThrows(DomainException.class, () -> orderDomain.toEntity(order));
                assertTrue(exception.getMessage().contains("Id mismatch of order entities"));
            }
        );
    }

    @Test
    void toEntityWithNullAndIdIsNull() {
        OrderDTO orderDTO = getDTO();
        Order order = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build().toEntity(null);
        assertAll(
            () -> assertNull(order.getId()),
            () -> assertEquals(order.getOrderDate(), orderDTO.getOrderDate()),
            () -> assertEquals(order.getOrderStatus(), OrderStatus.DRAFT),
            () -> assertEquals(order.getDescription(), orderDTO.getDescription()),
            () -> assertEquals(order.getAddress().getId(), orderDTO.getAddress().getId()),
            () -> assertEquals(order.getBuyerId(), orderDTO.getBuyer().getId())
        );
    }

    @Test
    void toEntityPartialUpdate() {
        OrderDTO orderDTO = getDTO();
        orderDTO.setId(1L);
        orderDTO.setOrderStatus(OrderStatus.CANCELLED);
        Order o = new Order();
        o.setId(1L);
        Order order = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build().toEntity(o);
        assertAll(
            () -> assertEquals(order.getId(), orderDTO.getId()),
            () -> assertEquals(order.getOrderDate(), orderDTO.getOrderDate()),
            () -> assertEquals(order.getOrderStatus(), orderDTO.getOrderStatus()),
            () -> assertEquals(order.getDescription(), orderDTO.getDescription())
        );
    }
}
