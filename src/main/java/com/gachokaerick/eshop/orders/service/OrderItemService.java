package com.gachokaerick.eshop.orders.service;

import com.gachokaerick.eshop.orders.domain.aggregates.order.OrderItem;
import com.gachokaerick.eshop.orders.domain.aggregates.order.OrderItemDomain;
import com.gachokaerick.eshop.orders.domain.aggregates.order.OrderItemMapper;
import com.gachokaerick.eshop.orders.repository.OrderItemRepository;
import com.gachokaerick.eshop.orders.service.dto.OrderItemDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderItem}.
 */
@Service
@Transactional
public class OrderItemService {

    private final Logger log = LoggerFactory.getLogger(OrderItemService.class);

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderService orderService;

    public OrderItemService(OrderItemRepository orderItemRepository, OrderItemMapper orderItemMapper, OrderService orderService) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
        this.orderService = orderService;
    }

    /**
     * Save a orderItem.
     *
     * @param orderItemDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderItemDTO save(OrderItemDTO orderItemDTO) {
        log.debug("Request to save OrderItem : {}", orderItemDTO);
        OrderItemDomain orderItemDomain = new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build();
        OrderItem orderItem;
        if (orderItemDTO.getId() != null) {
            orderItem = orderItemDomain.toEntity(orderItemRepository.getById(orderItemDTO.getId()));
            orderItem = orderItemRepository.save(orderItem);
        } else {
            orderItem = orderService.addOrderItem(orderItemDomain);
        }
        return orderItemMapper.toDto(orderItem);
    }

    /**
     * Partially update a orderItem.
     *
     * @param orderItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderItemDTO> partialUpdate(OrderItemDTO orderItemDTO) {
        log.debug("Request to partially update OrderItem : {}", orderItemDTO);

        return orderItemRepository
            .findById(orderItemDTO.getId())
            .map(existingOrderItem -> {
                orderItemMapper.partialUpdate(existingOrderItem, orderItemDTO);

                return existingOrderItem;
            })
            .map(orderItemRepository::save)
            .map(orderItemMapper::toDto);
    }

    /**
     * Get all the orderItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderItems");
        return orderItemRepository.findAll(pageable).map(orderItemMapper::toDto);
    }

    /**
     * Get one orderItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderItemDTO> findOne(Long id) {
        log.debug("Request to get OrderItem : {}", id);
        return orderItemRepository.findById(id).map(orderItemMapper::toDto);
    }

    /**
     * Delete the orderItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderItem : {}", id);
        orderService.deleteOrderItem(id);
    }
}
