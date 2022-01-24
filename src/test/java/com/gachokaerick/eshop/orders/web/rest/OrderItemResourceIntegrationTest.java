package com.gachokaerick.eshop.orders.web.rest;

import static com.gachokaerick.eshop.orders.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gachokaerick.eshop.orders.IntegrationTest;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.BuyerMapper;
import com.gachokaerick.eshop.orders.domain.aggregates.order.*;
import com.gachokaerick.eshop.orders.domain.enumeration.OrderStatus;
import com.gachokaerick.eshop.orders.repository.OrderItemRepository;
import com.gachokaerick.eshop.orders.repository.OrderRepository;
import com.gachokaerick.eshop.orders.service.dto.OrderDTO;
import com.gachokaerick.eshop.orders.service.dto.OrderItemDTO;
import com.gachokaerick.eshop.orders.service.mapper.AddressMapper;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrderItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderItemResourceIntegrationTest {

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PICTURE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE_URL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DISCOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT = new BigDecimal(2);

    private static final Integer DEFAULT_UNITS = 1;
    private static final Integer UPDATED_UNITS = 2;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String ENTITY_API_URL = "/api/order-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final OrderDTO ORDER_DTO = new OrderDTO(1L, ZonedDateTime.now(), OrderStatus.DRAFT);

    private static OrderMapper orderMapper;

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderItemMockMvc;

    @Autowired
    private OrderMapper orderMap;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private BuyerMapper buyerMapper;

    @Autowired
    private OrderRepository orderRepository;

    private OrderItem orderItem;

    static OrderItemDTO getOrderItemDTO() {
        return new OrderItemDTO(
            null,
            DEFAULT_PRODUCT_NAME,
            DEFAULT_PICTURE_URL,
            DEFAULT_UNIT_PRICE,
            DEFAULT_DISCOUNT,
            DEFAULT_UNITS,
            DEFAULT_PRODUCT_ID,
            ORDER_DTO
        );
    }

    static OrderItemDTO getUpdatedOrderItemDTO() {
        return new OrderItemDTO(
            null,
            UPDATED_PRODUCT_NAME,
            UPDATED_PICTURE_URL,
            UPDATED_UNIT_PRICE,
            UPDATED_DISCOUNT,
            UPDATED_UNITS,
            UPDATED_PRODUCT_ID,
            ORDER_DTO
        );
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createEntity(EntityManager em, AddressMapper am, BuyerMapper bm) {
        OrderItemDTO orderItemDTO = getOrderItemDTO();

        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIntegrationTest.createEntity(em, am, bm);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).get(0);
        }

        OrderItemDomain orderItemDomain = new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build();
        OrderItem orderItem = orderItemDomain.toEntity(null);
        orderItemDomain.setOrder(orderItem, order);
        return orderItem;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createUpdatedEntity(EntityManager em) {
        OrderItemDTO orderItemDTO = getUpdatedOrderItemDTO();

        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIntegrationTest.createUpdatedEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).get(0);
        }
        orderItemDTO.setOrder(orderMapper.toDto(order));

        OrderItemDomain orderItemDomain = new OrderItemDomain.OrderItemBuilder().withDTO(orderItemDTO).build();
        return orderItemDomain.toEntity(null);
    }

    @BeforeEach
    public void initTest() {
        orderMapper = orderMap;
        orderItem = createEntity(em, addressMapper, buyerMapper);
    }

    @Test
    @Transactional
    void createOrderItem() throws Exception {
        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();
        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        restOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeCreate + 1);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testOrderItem.getPictureUrl()).isEqualTo(DEFAULT_PICTURE_URL);
        assertThat(testOrderItem.getUnitPrice()).isEqualByComparingTo(DEFAULT_UNIT_PRICE);
        assertThat(testOrderItem.getDiscount()).isEqualByComparingTo(DEFAULT_DISCOUNT);
        assertThat(testOrderItem.getUnits()).isEqualTo(DEFAULT_UNITS);
        assertThat(testOrderItem.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testOrderItem.getOrder().getOrderItems().contains(testOrderItem)).isTrue();

        Optional<Order> orderOptional = orderRepository.findById(testOrderItem.getOrder().getId());
        assertThat(orderOptional.isPresent()).isTrue();
        assertThat(orderOptional.get().getOrderItems().contains(testOrderItem)).isTrue();
    }

    @Test
    @Transactional
    void createOrderItemWithExistingId() throws Exception {
        // Create the OrderItem with an existing ID
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        orderItemDTO.setId(1L);

        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        // set the field null
        orderItemDTO.setProductName(null);

        restOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPictureUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        // set the field null
        orderItemDTO.setPictureUrl(null);

        restOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        // set the field null
        orderItemDTO.setUnitPrice(null);

        restOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscountIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        // set the field null
        orderItemDTO.setDiscount(null);

        restOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitsIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        // set the field null
        orderItemDTO.setUnits(null);

        restOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProductIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        // set the field null
        orderItemDTO.setProductId(null);

        restOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderItems() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].pictureUrl").value(hasItem(DEFAULT_PICTURE_URL)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(sameNumber(DEFAULT_DISCOUNT))))
            .andExpect(jsonPath("$.[*].units").value(hasItem(DEFAULT_UNITS)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())));
    }

    @Test
    @Transactional
    void toEntity() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get the orderItem
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, orderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderItem.getId().intValue()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME))
            .andExpect(jsonPath("$.pictureUrl").value(DEFAULT_PICTURE_URL))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.discount").value(sameNumber(DEFAULT_DISCOUNT)))
            .andExpect(jsonPath("$.units").value(DEFAULT_UNITS))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrderItem() throws Exception {
        // Get the orderItem
        restOrderItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem
        OrderItem updatedOrderItem = orderItemRepository.findById(orderItem.getId()).get();
        // Disconnect from session so that the updates on updatedOrderItem are not directly saved in db
        em.detach(updatedOrderItem);
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(updatedOrderItem);
        orderItemDTO
            .productName(UPDATED_PRODUCT_NAME)
            .pictureUrl(UPDATED_PICTURE_URL)
            .unitPrice(UPDATED_UNIT_PRICE)
            .discount(UPDATED_DISCOUNT)
            .units(UPDATED_UNITS)
            .productId(UPDATED_PRODUCT_ID);

        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testOrderItem.getPictureUrl()).isEqualTo(UPDATED_PICTURE_URL);
        assertThat(testOrderItem.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testOrderItem.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testOrderItem.getUnits()).isEqualTo(UPDATED_UNITS);
        assertThat(testOrderItem.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    void putNonExistingOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        orderItemDTO.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        orderItemDTO.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        orderItemDTO.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderItemWithPatch() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem using partial update
        OrderItemDTO partialUpdatedOrderItem = new OrderItemDTO();
        partialUpdatedOrderItem.setId(orderItem.getId());

        partialUpdatedOrderItem
            .productName(UPDATED_PRODUCT_NAME)
            .unitPrice(UPDATED_UNIT_PRICE)
            .discount(UPDATED_DISCOUNT)
            .units(UPDATED_UNITS);

        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testOrderItem.getPictureUrl()).isEqualTo(DEFAULT_PICTURE_URL);
        assertThat(testOrderItem.getUnitPrice()).isEqualByComparingTo(UPDATED_UNIT_PRICE);
        assertThat(testOrderItem.getDiscount()).isEqualByComparingTo(UPDATED_DISCOUNT);
        assertThat(testOrderItem.getUnits()).isEqualTo(UPDATED_UNITS);
        assertThat(testOrderItem.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
    }

    @Test
    @Transactional
    void fullUpdateOrderItemWithPatch() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem using partial update
        OrderItemDTO partialUpdatedOrderItem = new OrderItemDTO();
        partialUpdatedOrderItem.setId(orderItem.getId());

        partialUpdatedOrderItem
            .productName(UPDATED_PRODUCT_NAME)
            .pictureUrl(UPDATED_PICTURE_URL)
            .unitPrice(UPDATED_UNIT_PRICE)
            .discount(UPDATED_DISCOUNT)
            .units(UPDATED_UNITS)
            .productId(UPDATED_PRODUCT_ID);

        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testOrderItem.getPictureUrl()).isEqualTo(UPDATED_PICTURE_URL);
        assertThat(testOrderItem.getUnitPrice()).isEqualByComparingTo(UPDATED_UNIT_PRICE);
        assertThat(testOrderItem.getDiscount()).isEqualByComparingTo(UPDATED_DISCOUNT);
        assertThat(testOrderItem.getUnits()).isEqualTo(UPDATED_UNITS);
        assertThat(testOrderItem.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        orderItemDTO.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        orderItemDTO.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        orderItemDTO.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeDelete = orderItemRepository.findAll().size();

        // Delete the orderItem
        restOrderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderItem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
