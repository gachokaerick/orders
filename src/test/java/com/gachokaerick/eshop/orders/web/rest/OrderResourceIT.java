//package com.gachokaerick.eshop.orders.web.rest;
//
//import static com.gachokaerick.eshop.orders.web.rest.TestUtil.sameInstant;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.gachokaerick.eshop.orders.IntegrationTest;
//import com.gachokaerick.eshop.orders.domain.Address;
//import com.gachokaerick.eshop.orders.domain.aggregates.buyer.Buyer;
//import com.gachokaerick.eshop.orders.domain.aggregates.order.Order;
//import com.gachokaerick.eshop.orders.domain.enumeration.OrderStatus;
//import com.gachokaerick.eshop.orders.repository.OrderRepository;
//import com.gachokaerick.eshop.orders.service.dto.OrderDTO;
//import com.gachokaerick.eshop.orders.domain.aggregates.order.OrderMapper;
//import java.time.Instant;
//import java.time.ZoneId;
//import java.time.ZoneOffset;
//import java.time.ZonedDateTime;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.atomic.AtomicLong;
//import javax.persistence.EntityManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Integration tests for the {@link OrderResource} REST controller.
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@WithMockUser
//class OrderResourceIT {
//
//    private static final ZonedDateTime DEFAULT_ORDER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_ORDER_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final OrderStatus DEFAULT_ORDER_STATUS = OrderStatus.SUBMITTED;
//    private static final OrderStatus UPDATED_ORDER_STATUS = OrderStatus.AWAITING_VALIDATION;
//
//    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
//    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";
//
//    private static final String ENTITY_API_URL = "/api/orders";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private OrderMapper orderMapper;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restOrderMockMvc;
//
//    private Order order;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Order createEntity(EntityManager em) {
//        Order order = new Order().orderDate(DEFAULT_ORDER_DATE).orderStatus(DEFAULT_ORDER_STATUS).description(DEFAULT_DESCRIPTION);
//        // Add required entity
//        Address address;
//        if (TestUtil.findAll(em, Address.class).isEmpty()) {
//            address = AddressResourceIT.createEntity(em);
//            em.persist(address);
//            em.flush();
//        } else {
//            address = TestUtil.findAll(em, Address.class).get(0);
//        }
//        order.setAddress(address);
//        // Add required entity
//        Buyer buyer;
//        if (TestUtil.findAll(em, Buyer.class).isEmpty()) {
//            buyer = BuyerResourceIT.createEntity(em);
//            em.persist(buyer);
//            em.flush();
//        } else {
//            buyer = TestUtil.findAll(em, Buyer.class).get(0);
//        }
//        order.setBuyer(buyer);
//        return order;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Order createUpdatedEntity(EntityManager em) {
//        Order order = new Order().orderDate(UPDATED_ORDER_DATE).orderStatus(UPDATED_ORDER_STATUS).description(UPDATED_DESCRIPTION);
//        // Add required entity
//        Address address;
//        if (TestUtil.findAll(em, Address.class).isEmpty()) {
//            address = AddressResourceIT.createUpdatedEntity(em);
//            em.persist(address);
//            em.flush();
//        } else {
//            address = TestUtil.findAll(em, Address.class).get(0);
//        }
//        order.setAddress(address);
//        // Add required entity
//        Buyer buyer;
//        if (TestUtil.findAll(em, Buyer.class).isEmpty()) {
//            buyer = BuyerResourceIT.createUpdatedEntity(em);
//            em.persist(buyer);
//            em.flush();
//        } else {
//            buyer = TestUtil.findAll(em, Buyer.class).get(0);
//        }
//        order.setBuyer(buyer);
//        return order;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        order = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    void createOrder() throws Exception {
//        int databaseSizeBeforeCreate = orderRepository.findAll().size();
//        // Create the Order
//        OrderDTO orderDTO = orderMapper.toDto(order);
//        restOrderMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
//            )
//            .andExpect(status().isCreated());
//
//        // Validate the Order in the database
//        List<Order> orderList = orderRepository.findAll();
//        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
//        Order testOrder = orderList.get(orderList.size() - 1);
//        assertThat(testOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
//        assertThat(testOrder.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
//        assertThat(testOrder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
//    }
//
//    @Test
//    @Transactional
//    void createOrderWithExistingId() throws Exception {
//        // Create the Order with an existing ID
//        order.setId(1L);
//        OrderDTO orderDTO = orderMapper.toDto(order);
//
//        int databaseSizeBeforeCreate = orderRepository.findAll().size();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restOrderMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Order in the database
//        List<Order> orderList = orderRepository.findAll();
//        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void checkOrderDateIsRequired() throws Exception {
//        int databaseSizeBeforeTest = orderRepository.findAll().size();
//        // set the field null
//        order.setOrderDate(null);
//
//        // Create the Order, which fails.
//        OrderDTO orderDTO = orderMapper.toDto(order);
//
//        restOrderMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        List<Order> orderList = orderRepository.findAll();
//        assertThat(orderList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void checkOrderStatusIsRequired() throws Exception {
//        int databaseSizeBeforeTest = orderRepository.findAll().size();
//        // set the field null
//        order.setOrderStatus(null);
//
//        // Create the Order, which fails.
//        OrderDTO orderDTO = orderMapper.toDto(order);
//
//        restOrderMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        List<Order> orderList = orderRepository.findAll();
//        assertThat(orderList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void getAllOrders() throws Exception {
//        // Initialize the database
//        orderRepository.saveAndFlush(order);
//
//        // Get all the orderList
//        restOrderMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
//            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(sameInstant(DEFAULT_ORDER_DATE))))
//            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())))
//            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
//    }
//
//    @Test
//    @Transactional
//    void getOrder() throws Exception {
//        // Initialize the database
//        orderRepository.saveAndFlush(order);
//
//        // Get the order
//        restOrderMockMvc
//            .perform(get(ENTITY_API_URL_ID, order.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
//            .andExpect(jsonPath("$.orderDate").value(sameInstant(DEFAULT_ORDER_DATE)))
//            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.toString()))
//            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingOrder() throws Exception {
//        // Get the order
//        restOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putNewOrder() throws Exception {
//        // Initialize the database
//        orderRepository.saveAndFlush(order);
//
//        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
//
//        // Update the order
//        Order updatedOrder = orderRepository.findById(order.getId()).get();
//        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
//        em.detach(updatedOrder);
//        updatedOrder.orderDate(UPDATED_ORDER_DATE).orderStatus(UPDATED_ORDER_STATUS).description(UPDATED_DESCRIPTION);
//        OrderDTO orderDTO = orderMapper.toDto(updatedOrder);
//
//        restOrderMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, orderDTO.getId())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Order in the database
//        List<Order> orderList = orderRepository.findAll();
//        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
//        Order testOrder = orderList.get(orderList.size() - 1);
//        assertThat(testOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
//        assertThat(testOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
//        assertThat(testOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingOrder() throws Exception {
//        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
//        order.setId(count.incrementAndGet());
//
//        // Create the Order
//        OrderDTO orderDTO = orderMapper.toDto(order);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restOrderMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, orderDTO.getId())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Order in the database
//        List<Order> orderList = orderRepository.findAll();
//        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchOrder() throws Exception {
//        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
//        order.setId(count.incrementAndGet());
//
//        // Create the Order
//        OrderDTO orderDTO = orderMapper.toDto(order);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restOrderMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Order in the database
//        List<Order> orderList = orderRepository.findAll();
//        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamOrder() throws Exception {
//        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
//        order.setId(count.incrementAndGet());
//
//        // Create the Order
//        OrderDTO orderDTO = orderMapper.toDto(order);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restOrderMockMvc
//            .perform(
//                put(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Order in the database
//        List<Order> orderList = orderRepository.findAll();
//        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdateOrderWithPatch() throws Exception {
//        // Initialize the database
//        orderRepository.saveAndFlush(order);
//
//        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
//
//        // Update the order using partial update
//        Order partialUpdatedOrder = new Order();
//        partialUpdatedOrder.setId(order.getId());
//
//        partialUpdatedOrder.orderDate(UPDATED_ORDER_DATE).description(UPDATED_DESCRIPTION);
//
//        restOrderMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Order in the database
//        List<Order> orderList = orderRepository.findAll();
//        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
//        Order testOrder = orderList.get(orderList.size() - 1);
//        assertThat(testOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
//        assertThat(testOrder.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
//        assertThat(testOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
//    }
//
//    @Test
//    @Transactional
//    void fullUpdateOrderWithPatch() throws Exception {
//        // Initialize the database
//        orderRepository.saveAndFlush(order);
//
//        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
//
//        // Update the order using partial update
//        Order partialUpdatedOrder = new Order();
//        partialUpdatedOrder.setId(order.getId());
//
//        partialUpdatedOrder.orderDate(UPDATED_ORDER_DATE).orderStatus(UPDATED_ORDER_STATUS).description(UPDATED_DESCRIPTION);
//
//        restOrderMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Order in the database
//        List<Order> orderList = orderRepository.findAll();
//        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
//        Order testOrder = orderList.get(orderList.size() - 1);
//        assertThat(testOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
//        assertThat(testOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
//        assertThat(testOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingOrder() throws Exception {
//        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
//        order.setId(count.incrementAndGet());
//
//        // Create the Order
//        OrderDTO orderDTO = orderMapper.toDto(order);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restOrderMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, orderDTO.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Order in the database
//        List<Order> orderList = orderRepository.findAll();
//        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchOrder() throws Exception {
//        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
//        order.setId(count.incrementAndGet());
//
//        // Create the Order
//        OrderDTO orderDTO = orderMapper.toDto(order);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restOrderMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Order in the database
//        List<Order> orderList = orderRepository.findAll();
//        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamOrder() throws Exception {
//        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
//        order.setId(count.incrementAndGet());
//
//        // Create the Order
//        OrderDTO orderDTO = orderMapper.toDto(order);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restOrderMockMvc
//            .perform(
//                patch(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(orderDTO))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Order in the database
//        List<Order> orderList = orderRepository.findAll();
//        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deleteOrder() throws Exception {
//        // Initialize the database
//        orderRepository.saveAndFlush(order);
//
//        int databaseSizeBeforeDelete = orderRepository.findAll().size();
//
//        // Delete the order
//        restOrderMockMvc
//            .perform(delete(ENTITY_API_URL_ID, order.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        List<Order> orderList = orderRepository.findAll();
//        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}