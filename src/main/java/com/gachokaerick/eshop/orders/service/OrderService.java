package com.gachokaerick.eshop.orders.service;

import com.gachokaerick.eshop.orders.domain.Address;
import com.gachokaerick.eshop.orders.domain.aggregates.order.*;
import com.gachokaerick.eshop.orders.repository.AddressRepository;
import com.gachokaerick.eshop.orders.repository.OrderItemRepository;
import com.gachokaerick.eshop.orders.repository.OrderRepository;
import com.gachokaerick.eshop.orders.service.dto.OrderDTO;
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

    public OrderService(
        OrderRepository orderRepository,
        AddressRepository addressRepository,
        OrderMapper orderMapper,
        OrderItemRepository orderItemRepository
    ) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.orderMapper = orderMapper;
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Save a order.
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
            order = orderDomain.setAddress(order, address);
        }

        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    public OrderItem addOrderItem(OrderItemDomain orderItemDomain) {
        OrderItem orderItem = orderItemDomain.toEntity(null);
        Order order = orderRepository.getById(orderItem.getOrder().getId());
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderMapper.toDto(order)).build();
        orderDomain.addOrderItem(order, orderItem);
        orderItem = orderItemRepository.save(orderItem);
        orderRepository.save(order);
        return orderItem;
    }

    public void deleteOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.getById(orderItemId);
        Order order = orderItem.getOrder();
        OrderDomain orderDomain = new OrderDomain.OrderBuilder().withOrderDTO(orderMapper.toDto(order)).build();
        orderDomain.removeOrderItem(order, orderItem);
        orderRepository.save(order);
        orderItemRepository.deleteById(orderItemId);
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
