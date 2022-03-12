package com.gachokaerick.eshop.orders.service;

import com.gachokaerick.eshop.orders.domain.Address;
import com.gachokaerick.eshop.orders.domain.aggregates.order.*;
import com.gachokaerick.eshop.orders.repository.AddressRepository;
import com.gachokaerick.eshop.orders.repository.OrderItemRepository;
import com.gachokaerick.eshop.orders.repository.OrderRepository;
import com.gachokaerick.eshop.orders.repository.PaymentRepository;
import com.gachokaerick.eshop.orders.service.dto.OrderDTO;
import com.gachokaerick.eshop.orders.service.dto.OrderItemDTO;
import com.gachokaerick.eshop.orders.service.dto.PaymentDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final OrderItemMapper orderItemMapper;
    private final PaymentMapper paymentMapper;

    public OrderService(
        OrderRepository orderRepository,
        AddressRepository addressRepository,
        OrderMapper orderMapper,
        OrderItemRepository orderItemRepository,
        PaymentRepository paymentRepository,
        OrderItemMapper orderItemMapper,
        PaymentMapper paymentMapper
    ) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.orderMapper = orderMapper;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
        this.orderItemMapper = orderItemMapper;
        this.paymentMapper = paymentMapper;
    }

    /**
     * Save an order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderDTO save(OrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderDTO).build();
        Order order;
        if (orderDTO.getId() != null) {
            order = orderDomain.toEntity(orderRepository.getById(orderDTO.getId()));
        } else {
            order = orderDomain.toEntity(null);
            Address address = addressRepository.getById(orderDTO.getAddress().getId());
            orderDomain.setAddress(order, address);
        }

        order = orderRepository.save(order);
        orderDTO.setId(order.getId());

        // save order items
        if (orderDTO.getOrderItems() != null) {
            orderDTO
                .getOrderItems()
                .forEach(orderItemDTO -> {
                    orderItemDTO.setOrder(orderDTO);
                    OrderItem orderItem = addOrderItem(orderItemDTO);
                    orderDTO.addOrderItemDTO(orderItemMapper.toDto(orderItem));
                });
        }

        // save payments
        if (orderDTO.getPayments() != null) {
            orderDTO
                .getPayments()
                .forEach(paymentDTO -> {
                    paymentDTO.setOrder(orderDTO);
                    Payment payment = addPayment(paymentDTO);
                    orderDTO.addPaymentDTO(paymentMapper.toDto(payment));
                });
        }

        return orderMapper.toDto(order);
    }

    public OrderItem addOrderItem(OrderItemDTO orderItemDTO) {
        OrderItemDomain orderItemDomain = new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build();
        OrderItem orderItem = orderItemDomain.toEntity(null);
        Order order = orderRepository.getById(orderItemDTO.getOrder().getId());
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderMapper.toDto(order)).build();
        orderDomain.addOrderItem(order, orderItem);
        orderItem = orderItemRepository.save(orderItem);
        orderRepository.save(order);
        return orderItem;
    }

    public Payment addPayment(PaymentDTO paymentDTO) {
        PaymentDomain paymentDomain = new PaymentDomain.PaymentBuilder().withDTO(paymentDTO).build();
        Payment payment = paymentDomain.toEntity(null);
        Order order = orderRepository.getById(paymentDTO.getOrder().getId());
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderMapper.toDto(order)).build();
        orderDomain.addPayment(order, payment);
        payment = paymentRepository.save(payment);
        orderRepository.save(order);
        return payment;
    }

    public void deleteOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.getById(orderItemId);
        Order order = orderItem.getOrder();
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderMapper.toDto(order)).build();
        orderDomain.removeOrderItem(order, orderItem);
        orderRepository.save(order);
        orderItemRepository.deleteById(orderItemId);
    }

    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepository.getById(paymentId);
        Order order = payment.getOrder();
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderMapper.toDto(order)).build();
        orderDomain.removePayment(order, payment);
        orderRepository.save(order);
        paymentRepository.deleteById(paymentId);
    }

    /**
     * Partially update a order.
     *
     * @param orderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderDTO> partialUpdate(OrderDTO orderDTO) {
        log.debug("Request to partially update Order : {}", orderDTO);

        return orderRepository
            .findById(orderDTO.getId())
            .map(existingOrder -> {
                orderMapper.partialUpdate(existingOrder, orderDTO);

                return existingOrder;
            })
            .map(orderRepository::save)
            .map(orderMapper::toDto);
    }

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderDTO> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id).map(orderMapper::toDto);
    }

    /**
     * Delete the order by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }
}
