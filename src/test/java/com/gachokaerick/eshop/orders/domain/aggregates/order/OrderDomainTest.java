package com.gachokaerick.eshop.orders.domain.aggregates.order;

import static org.junit.jupiter.api.Assertions.*;

import com.gachokaerick.eshop.orders.domain.Address;
import com.gachokaerick.eshop.orders.domain.enumeration.Gender;
import com.gachokaerick.eshop.orders.domain.enumeration.OrderStatus;
import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("domain")
class OrderDomainTest {

    final ZonedDateTime orderDate = ZonedDateTime.now();
    final OrderStatus orderStatus = OrderStatus.DRAFT;
    final String description = "test";
    final BuyerDTO buyer = new BuyerDTO(2L, "john", "doe", Gender.MALE, "johndoe@gmail.com", "+254712345678");
    final AddressDTO address = new AddressDTO(3L, "mfangano", "nairobi", "cbd", "kenya", "01101", buyer);
    final AddressDTO address2 = new AddressDTO(4L, "moi avenue", "nairobi", "cbd", "kenya", "920903", buyer);

    final OrderItemDTO orderItemDTO1 = new OrderItemDTO(
        null,
        "test1",
        "test",
        BigDecimal.valueOf(20),
        BigDecimal.valueOf(3),
        2,
        292L,
        getDTO().withId(1L)
    );
    final OrderItemDTO orderItemDTO2 = new OrderItemDTO(
        null,
        "test2",
        "test",
        BigDecimal.valueOf(30),
        BigDecimal.valueOf(8),
        3,
        232L,
        getDTO().withId(1L)
    );

    final PaymentDTO paymentDTO1 = new PaymentDTO(
        null,
        ZonedDateTime.now(),
        ZonedDateTime.now(),
        "complete",
        "KE",
        "johndoe@example.com",
        "john",
        "doe",
        "232",
        "KES",
        BigDecimal.valueOf(246),
        "hjkjk",
        getDTO().withId(1L)
    );
    final PaymentDTO paymentDTO2 = new PaymentDTO(
        null,
        ZonedDateTime.now(),
        ZonedDateTime.now(),
        "complete",
        "KE",
        "johndoe@example.com",
        "john",
        "doe",
        "232",
        "KES",
        BigDecimal.valueOf(54),
        "fddfgdf",
        getDTO().withId(1L)
    );

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

    @Test
    void calculateItemsTotal() {
        OrderDTO orderDTO = getDTO();
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build();

        assertAll(
            () -> {
                Exception exception = assertThrows(DomainException.class, () -> orderDomain.calculateItemsTotal(null));
                assertTrue(exception.getMessage().contains("Cannot calculate items total for null order"));
            },
            () -> {
                Order order = orderDomain.toEntity(null);
                order.setOrderItems(null);
                Exception exception = assertThrows(DomainException.class, () -> orderDomain.calculateItemsTotal(order));
                assertTrue(exception.getMessage().contains("Cannot calculate items total for null order items"));
            },
            () -> {
                Order order = orderDomain.toEntity(null);
                order.setOrderItems(new HashSet<>());
                assertEquals(BigDecimal.ZERO, orderDomain.calculateItemsTotal(order));
            },
            () -> {
                Order order = orderDomain.toEntity(null);
                order.addOrderItems(new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO1).build().toEntity(null));
                order.addOrderItems(new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO2).build().toEntity(null));
                assertEquals(BigDecimal.valueOf(119), orderDomain.calculateItemsTotal(order));
            }
        );
    }

    @Test
    void calculateTotalPaid() {
        OrderDTO orderDTO = getDTO();
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build();

        assertAll(
            () -> {
                Exception exception = assertThrows(DomainException.class, () -> orderDomain.calculateTotalPaid(null));
                assertTrue(exception.getMessage().contains("Cannot calculate total paid for null order"));
            },
            () -> {
                Order order = orderDomain.toEntity(null);
                order.setPayments(null);
                Exception exception = assertThrows(DomainException.class, () -> orderDomain.calculateTotalPaid(order));
                assertTrue(exception.getMessage().contains("Cannot calculate items total for null order payments"));
            },
            () -> {
                Order order = orderDomain.toEntity(null);
                order.setPayments(new HashSet<>());
                assertEquals(BigDecimal.ZERO, orderDomain.calculateTotalPaid(order));
            },
            () -> {
                Order order = orderDomain.toEntity(null);
                order.addPayments(new PaymentDomain.PaymentBuilder().withDTO(paymentDTO1).build().toEntity(null));
                order.addPayments(new PaymentDomain.PaymentBuilder().withDTO(paymentDTO2).build().toEntity(null));
                assertEquals(BigDecimal.valueOf(300), orderDomain.calculateTotalPaid(order));
            }
        );
    }

    @Test
    void calculateOrderBalance() {
        OrderDTO orderDTO = getDTO();
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build();
        Order order = orderDomain.toEntity(null);

        order.addOrderItems(new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO1).build().toEntity(null));
        order.addOrderItems(new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO2).build().toEntity(null));

        order.addPayments(new PaymentDomain.PaymentBuilder().withDTO(paymentDTO1).build().toEntity(null));
        order.addPayments(new PaymentDomain.PaymentBuilder().withDTO(paymentDTO2).build().toEntity(null));

        assertEquals(BigDecimal.valueOf(-181), orderDomain.calculateOrderBalance(order));
    }

    @Test
    void setAddress() {
        OrderDTO orderDTO = getDTO();
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build();
        assertAll(
            () -> {
                Order order = orderDomain.toEntity(null);
                orderDomain.setAddress(order, address2.toEntity());
                assertEquals(order.getAddress().getId(), address2.getId());
            },
            () -> {
                Order order = orderDomain.toEntity(null);
                Exception exception = assertThrows(DomainException.class, () -> orderDomain.setAddress(order, null));
                assertTrue(exception.getMessage().contains("Address for an order must exist"));
            },
            () -> {
                Order order = orderDomain.toEntity(null);
                Address address = address2.toEntity();
                address.setId(null);
                Exception exception = assertThrows(DomainException.class, () -> orderDomain.setAddress(order, address));
                assertTrue(exception.getMessage().contains("Address for an order must exist"));
            }
        );
    }

    @Test
    void addOrderItem() {
        OrderDTO orderDTO = getDTO();
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build();
        assertAll(() -> {
            Order order = orderDomain.toEntity(null);
            OrderItem orderItem = new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO1).build().toEntity(null);
            orderDomain.addOrderItem(order, orderItem);
            assertFalse(order.getOrderItems().isEmpty());
            assertEquals(order.getOrderItems().size(), 1);
            assertTrue(order.getOrderItems().stream().anyMatch(it -> it.getProductName().equals(orderItem.getProductName())));
            assertNull(orderItem.getOrder().getId());
        });
    }

    @Test
    void addPayment() {
        OrderDTO orderDTO = getDTO();
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build();
        Order order = orderDomain.toEntity(null);
        Payment payment = new PaymentDomain.PaymentBuilder().withDTO(paymentDTO1).build().toEntity(null);
        orderDomain.addPayment(order, payment);

        assertFalse(order.getPayments().isEmpty());
        assertEquals(order.getPayments().size(), 1);
        assertTrue(order.getPayments().stream().anyMatch(it -> it.getPaymentId().equals(payment.getPaymentId())));
        assertNull(payment.getOrder().getId());
    }

    @Test
    void removeOrderItem() {
        OrderDTO orderDTO = getDTO();
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build();
        Order order = orderDomain.toEntity(null);
        OrderItem orderItem = new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO1).build().toEntity(null);

        orderDomain.addOrderItem(order, orderItem);
        assertFalse(order.getOrderItems().isEmpty());

        orderDomain.removeOrderItem(order, orderItem);
        assertTrue(order.getOrderItems().isEmpty());

        assertNull(orderItem.getOrder());
    }

    @Test
    void removePayment() {
        OrderDTO orderDTO = getDTO();
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build();
        Order order = orderDomain.toEntity(null);
        Payment payment = new PaymentDomain.PaymentBuilder().withDTO(paymentDTO1).build().toEntity(null);

        orderDomain.addPayment(order, payment);
        assertFalse(order.getPayments().isEmpty());

        orderDomain.removePayment(order, payment);
        assertTrue(order.getPayments().isEmpty());

        assertNull(payment.getOrder());
    }
}
