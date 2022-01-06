//package com.gachokaerick.eshop.orders.web.rest;
//
//import static com.gachokaerick.eshop.orders.web.rest.TestUtil.sameInstant;
//import static com.gachokaerick.eshop.orders.web.rest.TestUtil.sameNumber;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.gachokaerick.eshop.orders.IntegrationTest;
//import com.gachokaerick.eshop.orders.domain.aggregates.order.Order;
//import com.gachokaerick.eshop.orders.domain.aggregates.order.Payment;
//import com.gachokaerick.eshop.orders.repository.PaymentRepository;
//import com.gachokaerick.eshop.orders.service.dto.PaymentDTO;
//import com.gachokaerick.eshop.orders.domain.aggregates.order.PaymentMapper;
//import java.math.BigDecimal;
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
// * Integration tests for the {@link PaymentResource} REST controller.
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@WithMockUser
//class PaymentResourceIT {
//
//    private static final ZonedDateTime DEFAULT_CREATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_CREATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final ZonedDateTime DEFAULT_UPDATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_UPDATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    private static final String DEFAULT_PAYMENT_STATUS = "AAAAAAAAAA";
//    private static final String UPDATED_PAYMENT_STATUS = "BBBBBBBBBB";
//
//    private static final String DEFAULT_PAYER_COUNTRY_CODE = "AAAAAAAAAA";
//    private static final String UPDATED_PAYER_COUNTRY_CODE = "BBBBBBBBBB";
//
//    private static final String DEFAULT_PAYER_EMAIL = "AAAAAAAAAA";
//    private static final String UPDATED_PAYER_EMAIL = "BBBBBBBBBB";
//
//    private static final String DEFAULT_PAYER_NAME = "AAAAAAAAAA";
//    private static final String UPDATED_PAYER_NAME = "BBBBBBBBBB";
//
//    private static final String DEFAULT_PAYER_SURNAME = "AAAAAAAAAA";
//    private static final String UPDATED_PAYER_SURNAME = "BBBBBBBBBB";
//
//    private static final String DEFAULT_PAYER_ID = "AAAAAAAAAA";
//    private static final String UPDATED_PAYER_ID = "BBBBBBBBBB";
//
//    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
//    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";
//
//    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
//    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
//
//    private static final String DEFAULT_PAYMENT_ID = "AAAAAAAAAA";
//    private static final String UPDATED_PAYMENT_ID = "BBBBBBBBBB";
//
//    private static final String ENTITY_API_URL = "/api/payments";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private PaymentRepository paymentRepository;
//
//    @Autowired
//    private PaymentMapper paymentMapper;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restPaymentMockMvc;
//
//    private Payment payment;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Payment createEntity(EntityManager em) {
//        Payment payment = new Payment()
//            .createTime(DEFAULT_CREATE_TIME)
//            .updateTime(DEFAULT_UPDATE_TIME)
//            .paymentStatus(DEFAULT_PAYMENT_STATUS)
//            .payerCountryCode(DEFAULT_PAYER_COUNTRY_CODE)
//            .payerEmail(DEFAULT_PAYER_EMAIL)
//            .payerName(DEFAULT_PAYER_NAME)
//            .payerSurname(DEFAULT_PAYER_SURNAME)
//            .payerId(DEFAULT_PAYER_ID)
//            .currency(DEFAULT_CURRENCY)
//            .amount(DEFAULT_AMOUNT)
//            .paymentId(DEFAULT_PAYMENT_ID);
//        // Add required entity
//        Order order;
//        if (TestUtil.findAll(em, Order.class).isEmpty()) {
//            order = OrderResourceIT.createEntity(em);
//            em.persist(order);
//            em.flush();
//        } else {
//            order = TestUtil.findAll(em, Order.class).get(0);
//        }
//        payment.setOrder(order);
//        return payment;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Payment createUpdatedEntity(EntityManager em) {
//        Payment payment = new Payment()
//            .createTime(UPDATED_CREATE_TIME)
//            .updateTime(UPDATED_UPDATE_TIME)
//            .paymentStatus(UPDATED_PAYMENT_STATUS)
//            .payerCountryCode(UPDATED_PAYER_COUNTRY_CODE)
//            .payerEmail(UPDATED_PAYER_EMAIL)
//            .payerName(UPDATED_PAYER_NAME)
//            .payerSurname(UPDATED_PAYER_SURNAME)
//            .payerId(UPDATED_PAYER_ID)
//            .currency(UPDATED_CURRENCY)
//            .amount(UPDATED_AMOUNT)
//            .paymentId(UPDATED_PAYMENT_ID);
//        // Add required entity
//        Order order;
//        if (TestUtil.findAll(em, Order.class).isEmpty()) {
//            order = OrderResourceIT.createUpdatedEntity(em);
//            em.persist(order);
//            em.flush();
//        } else {
//            order = TestUtil.findAll(em, Order.class).get(0);
//        }
//        payment.setOrder(order);
//        return payment;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        payment = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    void createPayment() throws Exception {
//        int databaseSizeBeforeCreate = paymentRepository.findAll().size();
//        // Create the Payment
//        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
//        restPaymentMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isCreated());
//
//        // Validate the Payment in the database
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
//        Payment testPayment = paymentList.get(paymentList.size() - 1);
//        assertThat(testPayment.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
//        assertThat(testPayment.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
//        assertThat(testPayment.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
//        assertThat(testPayment.getPayerCountryCode()).isEqualTo(DEFAULT_PAYER_COUNTRY_CODE);
//        assertThat(testPayment.getPayerEmail()).isEqualTo(DEFAULT_PAYER_EMAIL);
//        assertThat(testPayment.getPayerName()).isEqualTo(DEFAULT_PAYER_NAME);
//        assertThat(testPayment.getPayerSurname()).isEqualTo(DEFAULT_PAYER_SURNAME);
//        assertThat(testPayment.getPayerId()).isEqualTo(DEFAULT_PAYER_ID);
//        assertThat(testPayment.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
//        assertThat(testPayment.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
//        assertThat(testPayment.getPaymentId()).isEqualTo(DEFAULT_PAYMENT_ID);
//    }
//
//    @Test
//    @Transactional
//    void createPaymentWithExistingId() throws Exception {
//        // Create the Payment with an existing ID
//        payment.setId(1L);
//        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
//
//        int databaseSizeBeforeCreate = paymentRepository.findAll().size();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restPaymentMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Payment in the database
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void checkCreateTimeIsRequired() throws Exception {
//        int databaseSizeBeforeTest = paymentRepository.findAll().size();
//        // set the field null
//        payment.setCreateTime(null);
//
//        // Create the Payment, which fails.
//        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
//
//        restPaymentMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void checkUpdateTimeIsRequired() throws Exception {
//        int databaseSizeBeforeTest = paymentRepository.findAll().size();
//        // set the field null
//        payment.setUpdateTime(null);
//
//        // Create the Payment, which fails.
//        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
//
//        restPaymentMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void checkPayerNameIsRequired() throws Exception {
//        int databaseSizeBeforeTest = paymentRepository.findAll().size();
//        // set the field null
//        payment.setPayerName(null);
//
//        // Create the Payment, which fails.
//        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
//
//        restPaymentMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void checkPayerIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = paymentRepository.findAll().size();
//        // set the field null
//        payment.setPayerId(null);
//
//        // Create the Payment, which fails.
//        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
//
//        restPaymentMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void checkCurrencyIsRequired() throws Exception {
//        int databaseSizeBeforeTest = paymentRepository.findAll().size();
//        // set the field null
//        payment.setCurrency(null);
//
//        // Create the Payment, which fails.
//        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
//
//        restPaymentMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void checkAmountIsRequired() throws Exception {
//        int databaseSizeBeforeTest = paymentRepository.findAll().size();
//        // set the field null
//        payment.setAmount(null);
//
//        // Create the Payment, which fails.
//        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
//
//        restPaymentMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void getAllPayments() throws Exception {
//        // Initialize the database
//        paymentRepository.saveAndFlush(payment);
//
//        // Get all the paymentList
//        restPaymentMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
//            .andExpect(jsonPath("$.[*].createTime").value(hasItem(sameInstant(DEFAULT_CREATE_TIME))))
//            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(sameInstant(DEFAULT_UPDATE_TIME))))
//            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS)))
//            .andExpect(jsonPath("$.[*].payerCountryCode").value(hasItem(DEFAULT_PAYER_COUNTRY_CODE)))
//            .andExpect(jsonPath("$.[*].payerEmail").value(hasItem(DEFAULT_PAYER_EMAIL)))
//            .andExpect(jsonPath("$.[*].payerName").value(hasItem(DEFAULT_PAYER_NAME)))
//            .andExpect(jsonPath("$.[*].payerSurname").value(hasItem(DEFAULT_PAYER_SURNAME)))
//            .andExpect(jsonPath("$.[*].payerId").value(hasItem(DEFAULT_PAYER_ID)))
//            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
//            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
//            .andExpect(jsonPath("$.[*].paymentId").value(hasItem(DEFAULT_PAYMENT_ID)));
//    }
//
//    @Test
//    @Transactional
//    void getPayment() throws Exception {
//        // Initialize the database
//        paymentRepository.saveAndFlush(payment);
//
//        // Get the payment
//        restPaymentMockMvc
//            .perform(get(ENTITY_API_URL_ID, payment.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
//            .andExpect(jsonPath("$.createTime").value(sameInstant(DEFAULT_CREATE_TIME)))
//            .andExpect(jsonPath("$.updateTime").value(sameInstant(DEFAULT_UPDATE_TIME)))
//            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS))
//            .andExpect(jsonPath("$.payerCountryCode").value(DEFAULT_PAYER_COUNTRY_CODE))
//            .andExpect(jsonPath("$.payerEmail").value(DEFAULT_PAYER_EMAIL))
//            .andExpect(jsonPath("$.payerName").value(DEFAULT_PAYER_NAME))
//            .andExpect(jsonPath("$.payerSurname").value(DEFAULT_PAYER_SURNAME))
//            .andExpect(jsonPath("$.payerId").value(DEFAULT_PAYER_ID))
//            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
//            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
//            .andExpect(jsonPath("$.paymentId").value(DEFAULT_PAYMENT_ID));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingPayment() throws Exception {
//        // Get the payment
//        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putNewPayment() throws Exception {
//        // Initialize the database
//        paymentRepository.saveAndFlush(payment);
//
//        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
//
//        // Update the payment
//        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
//        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
//        em.detach(updatedPayment);
//        updatedPayment
//            .createTime(UPDATED_CREATE_TIME)
//            .updateTime(UPDATED_UPDATE_TIME)
//            .paymentStatus(UPDATED_PAYMENT_STATUS)
//            .payerCountryCode(UPDATED_PAYER_COUNTRY_CODE)
//            .payerEmail(UPDATED_PAYER_EMAIL)
//            .payerName(UPDATED_PAYER_NAME)
//            .payerSurname(UPDATED_PAYER_SURNAME)
//            .payerId(UPDATED_PAYER_ID)
//            .currency(UPDATED_CURRENCY)
//            .amount(UPDATED_AMOUNT)
//            .paymentId(UPDATED_PAYMENT_ID);
//        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);
//
//        restPaymentMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, paymentDTO.getId())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Payment in the database
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
//        Payment testPayment = paymentList.get(paymentList.size() - 1);
//        assertThat(testPayment.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
//        assertThat(testPayment.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
//        assertThat(testPayment.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
//        assertThat(testPayment.getPayerCountryCode()).isEqualTo(UPDATED_PAYER_COUNTRY_CODE);
//        assertThat(testPayment.getPayerEmail()).isEqualTo(UPDATED_PAYER_EMAIL);
//        assertThat(testPayment.getPayerName()).isEqualTo(UPDATED_PAYER_NAME);
//        assertThat(testPayment.getPayerSurname()).isEqualTo(UPDATED_PAYER_SURNAME);
//        assertThat(testPayment.getPayerId()).isEqualTo(UPDATED_PAYER_ID);
//        assertThat(testPayment.getCurrency()).isEqualTo(UPDATED_CURRENCY);
//        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
//        assertThat(testPayment.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingPayment() throws Exception {
//        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
//        payment.setId(count.incrementAndGet());
//
//        // Create the Payment
//        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restPaymentMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, paymentDTO.getId())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Payment in the database
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchPayment() throws Exception {
//        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
//        payment.setId(count.incrementAndGet());
//
//        // Create the Payment
//        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restPaymentMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Payment in the database
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamPayment() throws Exception {
//        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
//        payment.setId(count.incrementAndGet());
//
//        // Create the Payment
//        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restPaymentMockMvc
//            .perform(
//                put(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Payment in the database
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdatePaymentWithPatch() throws Exception {
//        // Initialize the database
//        paymentRepository.saveAndFlush(payment);
//
//        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
//
//        // Update the payment using partial update
//        Payment partialUpdatedPayment = new Payment();
//        partialUpdatedPayment.setId(payment.getId());
//
//        partialUpdatedPayment
//            .updateTime(UPDATED_UPDATE_TIME)
//            .payerEmail(UPDATED_PAYER_EMAIL)
//            .payerSurname(UPDATED_PAYER_SURNAME)
//            .amount(UPDATED_AMOUNT);
//
//        restPaymentMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Payment in the database
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
//        Payment testPayment = paymentList.get(paymentList.size() - 1);
//        assertThat(testPayment.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
//        assertThat(testPayment.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
//        assertThat(testPayment.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
//        assertThat(testPayment.getPayerCountryCode()).isEqualTo(DEFAULT_PAYER_COUNTRY_CODE);
//        assertThat(testPayment.getPayerEmail()).isEqualTo(UPDATED_PAYER_EMAIL);
//        assertThat(testPayment.getPayerName()).isEqualTo(DEFAULT_PAYER_NAME);
//        assertThat(testPayment.getPayerSurname()).isEqualTo(UPDATED_PAYER_SURNAME);
//        assertThat(testPayment.getPayerId()).isEqualTo(DEFAULT_PAYER_ID);
//        assertThat(testPayment.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
//        assertThat(testPayment.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
//        assertThat(testPayment.getPaymentId()).isEqualTo(DEFAULT_PAYMENT_ID);
//    }
//
//    @Test
//    @Transactional
//    void fullUpdatePaymentWithPatch() throws Exception {
//        // Initialize the database
//        paymentRepository.saveAndFlush(payment);
//
//        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
//
//        // Update the payment using partial update
//        Payment partialUpdatedPayment = new Payment();
//        partialUpdatedPayment.setId(payment.getId());
//
//        partialUpdatedPayment
//            .createTime(UPDATED_CREATE_TIME)
//            .updateTime(UPDATED_UPDATE_TIME)
//            .paymentStatus(UPDATED_PAYMENT_STATUS)
//            .payerCountryCode(UPDATED_PAYER_COUNTRY_CODE)
//            .payerEmail(UPDATED_PAYER_EMAIL)
//            .payerName(UPDATED_PAYER_NAME)
//            .payerSurname(UPDATED_PAYER_SURNAME)
//            .payerId(UPDATED_PAYER_ID)
//            .currency(UPDATED_CURRENCY)
//            .amount(UPDATED_AMOUNT)
//            .paymentId(UPDATED_PAYMENT_ID);
//
//        restPaymentMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Payment in the database
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
//        Payment testPayment = paymentList.get(paymentList.size() - 1);
//        assertThat(testPayment.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
//        assertThat(testPayment.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
//        assertThat(testPayment.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
//        assertThat(testPayment.getPayerCountryCode()).isEqualTo(UPDATED_PAYER_COUNTRY_CODE);
//        assertThat(testPayment.getPayerEmail()).isEqualTo(UPDATED_PAYER_EMAIL);
//        assertThat(testPayment.getPayerName()).isEqualTo(UPDATED_PAYER_NAME);
//        assertThat(testPayment.getPayerSurname()).isEqualTo(UPDATED_PAYER_SURNAME);
//        assertThat(testPayment.getPayerId()).isEqualTo(UPDATED_PAYER_ID);
//        assertThat(testPayment.getCurrency()).isEqualTo(UPDATED_CURRENCY);
//        assertThat(testPayment.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
//        assertThat(testPayment.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingPayment() throws Exception {
//        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
//        payment.setId(count.incrementAndGet());
//
//        // Create the Payment
//        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restPaymentMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, paymentDTO.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Payment in the database
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchPayment() throws Exception {
//        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
//        payment.setId(count.incrementAndGet());
//
//        // Create the Payment
//        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restPaymentMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Payment in the database
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamPayment() throws Exception {
//        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
//        payment.setId(count.incrementAndGet());
//
//        // Create the Payment
//        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restPaymentMockMvc
//            .perform(
//                patch(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Payment in the database
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deletePayment() throws Exception {
//        // Initialize the database
//        paymentRepository.saveAndFlush(payment);
//
//        int databaseSizeBeforeDelete = paymentRepository.findAll().size();
//
//        // Delete the payment
//        restPaymentMockMvc
//            .perform(delete(ENTITY_API_URL_ID, payment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        List<Payment> paymentList = paymentRepository.findAll();
//        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
