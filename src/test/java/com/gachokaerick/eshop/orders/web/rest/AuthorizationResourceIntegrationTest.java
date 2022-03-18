package com.gachokaerick.eshop.orders.web.rest;

import static com.gachokaerick.eshop.orders.web.rest.TestUtil.sameInstant;
import static com.gachokaerick.eshop.orders.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gachokaerick.eshop.orders.IntegrationTest;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.BuyerMapper;
import com.gachokaerick.eshop.orders.domain.aggregates.order.Authorization;
import com.gachokaerick.eshop.orders.domain.aggregates.order.Order;
import com.gachokaerick.eshop.orders.domain.enumeration.PaymentProvider;
import com.gachokaerick.eshop.orders.repository.AuthorizationRepository;
import com.gachokaerick.eshop.orders.service.dto.AuthorizationDTO;
import com.gachokaerick.eshop.orders.service.mapper.AddressMapper;
import com.gachokaerick.eshop.orders.service.mapper.AuthorizationMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
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
 * Integration tests for the {@link AuthorizationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AuthorizationResourceIntegrationTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_AUTH_ID = "AAAAAAAAAA";
    private static final String UPDATED_AUTH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final ZonedDateTime DEFAULT_EXPIRATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final PaymentProvider DEFAULT_PAYMENT_PROVIDER = PaymentProvider.PAYPAL;
    private static final PaymentProvider UPDATED_PAYMENT_PROVIDER = PaymentProvider.PAYPAL;

    private static final String ENTITY_API_URL = "/api/authorizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    private static AddressMapper addressMapper;
    private static BuyerMapper buyerMapper;

    @Autowired
    private AuthorizationRepository authorizationRepository;

    @Autowired
    private AuthorizationMapper authorizationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuthorizationMockMvc;

    @Autowired
    private AddressMapper addressMap;

    @Autowired
    private BuyerMapper buyerMap;

    private Authorization authorization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authorization createEntity(EntityManager em) {
        Authorization authorization = new Authorization()
            .status(DEFAULT_STATUS)
            .authId(DEFAULT_AUTH_ID)
            .currencyCode(DEFAULT_CURRENCY_CODE)
            .amount(DEFAULT_AMOUNT)
            .expirationTime(DEFAULT_EXPIRATION_TIME)
            .paymentProvider(DEFAULT_PAYMENT_PROVIDER);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIntegrationTest.createEntity(em, addressMapper, buyerMapper);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).get(0);
        }
        authorization.setOrder(order);
        return authorization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authorization createUpdatedEntity(EntityManager em) {
        Authorization authorization = new Authorization()
            .status(UPDATED_STATUS)
            .authId(UPDATED_AUTH_ID)
            .currencyCode(UPDATED_CURRENCY_CODE)
            .amount(UPDATED_AMOUNT)
            .expirationTime(UPDATED_EXPIRATION_TIME)
            .paymentProvider(UPDATED_PAYMENT_PROVIDER);
        // Add required entity
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIntegrationTest.createUpdatedEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).get(0);
        }
        authorization.setOrder(order);
        return authorization;
    }

    @BeforeEach
    public void initTest() {
        addressMapper = addressMap;
        buyerMapper = buyerMap;
        authorization = createEntity(em);
    }

    @Test
    @Transactional
    void createAuthorization() throws Exception {
        int databaseSizeBeforeCreate = authorizationRepository.findAll().size();
        // Create the Authorization
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(authorization);
        restAuthorizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Authorization in the database
        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeCreate + 1);
        Authorization testAuthorization = authorizationList.get(authorizationList.size() - 1);
        assertThat(testAuthorization.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAuthorization.getAuthId()).isEqualTo(DEFAULT_AUTH_ID);
        assertThat(testAuthorization.getCurrencyCode()).isEqualTo(DEFAULT_CURRENCY_CODE);
        assertThat(testAuthorization.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testAuthorization.getExpirationTime()).isEqualTo(DEFAULT_EXPIRATION_TIME);
        assertThat(testAuthorization.getPaymentProvider()).isEqualTo(DEFAULT_PAYMENT_PROVIDER);
    }

    @Test
    @Transactional
    void createAuthorizationWithExistingId() throws Exception {
        // Create the Authorization with an existing ID
        authorization.setId(1L);
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(authorization);

        int databaseSizeBeforeCreate = authorizationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthorizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authorization in the database
        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorizationRepository.findAll().size();
        // set the field null
        authorization.setStatus(null);

        // Create the Authorization, which fails.
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(authorization);

        restAuthorizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAuthIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorizationRepository.findAll().size();
        // set the field null
        authorization.setAuthId(null);

        // Create the Authorization, which fails.
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(authorization);

        restAuthorizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorizationRepository.findAll().size();
        // set the field null
        authorization.setCurrencyCode(null);

        // Create the Authorization, which fails.
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(authorization);

        restAuthorizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorizationRepository.findAll().size();
        // set the field null
        authorization.setAmount(null);

        // Create the Authorization, which fails.
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(authorization);

        restAuthorizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpirationTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorizationRepository.findAll().size();
        // set the field null
        authorization.setExpirationTime(null);

        // Create the Authorization, which fails.
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(authorization);

        restAuthorizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentProviderIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorizationRepository.findAll().size();
        // set the field null
        authorization.setPaymentProvider(null);

        // Create the Authorization, which fails.
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(authorization);

        restAuthorizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAuthorizations() throws Exception {
        // Initialize the database
        authorizationRepository.saveAndFlush(authorization);

        // Get all the authorizationList
        restAuthorizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authorization.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].authId").value(hasItem(DEFAULT_AUTH_ID)))
            .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].expirationTime").value(hasItem(sameInstant(DEFAULT_EXPIRATION_TIME))))
            .andExpect(jsonPath("$.[*].paymentProvider").value(hasItem(DEFAULT_PAYMENT_PROVIDER.toString())));
    }

    @Test
    @Transactional
    void getAuthorization() throws Exception {
        // Initialize the database
        authorizationRepository.saveAndFlush(authorization);

        // Get the authorization
        restAuthorizationMockMvc
            .perform(get(ENTITY_API_URL_ID, authorization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(authorization.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.authId").value(DEFAULT_AUTH_ID))
            .andExpect(jsonPath("$.currencyCode").value(DEFAULT_CURRENCY_CODE))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.expirationTime").value(sameInstant(DEFAULT_EXPIRATION_TIME)))
            .andExpect(jsonPath("$.paymentProvider").value(DEFAULT_PAYMENT_PROVIDER.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAuthorization() throws Exception {
        // Get the authorization
        restAuthorizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAuthorization() throws Exception {
        // Initialize the database
        authorizationRepository.saveAndFlush(authorization);

        int databaseSizeBeforeUpdate = authorizationRepository.findAll().size();

        // Update the authorization
        Authorization updatedAuthorization = authorizationRepository.findById(authorization.getId()).get();
        // Disconnect from session so that the updates on updatedAuthorization are not directly saved in db
        em.detach(updatedAuthorization);
        updatedAuthorization
            .status(UPDATED_STATUS)
            .authId(UPDATED_AUTH_ID)
            .currencyCode(UPDATED_CURRENCY_CODE)
            .amount(UPDATED_AMOUNT)
            .expirationTime(UPDATED_EXPIRATION_TIME)
            .paymentProvider(UPDATED_PAYMENT_PROVIDER);
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(updatedAuthorization);

        restAuthorizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, authorizationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Authorization in the database
        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeUpdate);
        Authorization testAuthorization = authorizationList.get(authorizationList.size() - 1);
        assertThat(testAuthorization.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAuthorization.getAuthId()).isEqualTo(UPDATED_AUTH_ID);
        assertThat(testAuthorization.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
        assertThat(testAuthorization.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAuthorization.getExpirationTime()).isEqualTo(UPDATED_EXPIRATION_TIME);
        assertThat(testAuthorization.getPaymentProvider()).isEqualTo(UPDATED_PAYMENT_PROVIDER);
    }

    @Test
    @Transactional
    void putNonExistingAuthorization() throws Exception {
        int databaseSizeBeforeUpdate = authorizationRepository.findAll().size();
        authorization.setId(count.incrementAndGet());

        // Create the Authorization
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(authorization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, authorizationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authorization in the database
        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuthorization() throws Exception {
        int databaseSizeBeforeUpdate = authorizationRepository.findAll().size();
        authorization.setId(count.incrementAndGet());

        // Create the Authorization
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(authorization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authorization in the database
        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuthorization() throws Exception {
        int databaseSizeBeforeUpdate = authorizationRepository.findAll().size();
        authorization.setId(count.incrementAndGet());

        // Create the Authorization
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(authorization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorizationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Authorization in the database
        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAuthorizationWithPatch() throws Exception {
        // Initialize the database
        authorizationRepository.saveAndFlush(authorization);

        int databaseSizeBeforeUpdate = authorizationRepository.findAll().size();

        // Update the authorization using partial update
        Authorization partialUpdatedAuthorization = new Authorization();
        partialUpdatedAuthorization.setId(authorization.getId());

        partialUpdatedAuthorization.currencyCode(UPDATED_CURRENCY_CODE);

        restAuthorizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthorization.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuthorization))
            )
            .andExpect(status().isOk());

        // Validate the Authorization in the database
        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeUpdate);
        Authorization testAuthorization = authorizationList.get(authorizationList.size() - 1);
        assertThat(testAuthorization.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAuthorization.getAuthId()).isEqualTo(DEFAULT_AUTH_ID);
        assertThat(testAuthorization.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
        assertThat(testAuthorization.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testAuthorization.getExpirationTime()).isEqualTo(DEFAULT_EXPIRATION_TIME);
        assertThat(testAuthorization.getPaymentProvider()).isEqualTo(DEFAULT_PAYMENT_PROVIDER);
    }

    @Test
    @Transactional
    void fullUpdateAuthorizationWithPatch() throws Exception {
        // Initialize the database
        authorizationRepository.saveAndFlush(authorization);

        int databaseSizeBeforeUpdate = authorizationRepository.findAll().size();

        // Update the authorization using partial update
        Authorization partialUpdatedAuthorization = new Authorization();
        partialUpdatedAuthorization.setId(authorization.getId());

        partialUpdatedAuthorization
            .status(UPDATED_STATUS)
            .authId(UPDATED_AUTH_ID)
            .currencyCode(UPDATED_CURRENCY_CODE)
            .amount(UPDATED_AMOUNT)
            .expirationTime(UPDATED_EXPIRATION_TIME)
            .paymentProvider(UPDATED_PAYMENT_PROVIDER);

        restAuthorizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthorization.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuthorization))
            )
            .andExpect(status().isOk());

        // Validate the Authorization in the database
        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeUpdate);
        Authorization testAuthorization = authorizationList.get(authorizationList.size() - 1);
        assertThat(testAuthorization.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAuthorization.getAuthId()).isEqualTo(UPDATED_AUTH_ID);
        assertThat(testAuthorization.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
        assertThat(testAuthorization.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testAuthorization.getExpirationTime()).isEqualTo(UPDATED_EXPIRATION_TIME);
        assertThat(testAuthorization.getPaymentProvider()).isEqualTo(UPDATED_PAYMENT_PROVIDER);
    }

    @Test
    @Transactional
    void patchNonExistingAuthorization() throws Exception {
        int databaseSizeBeforeUpdate = authorizationRepository.findAll().size();
        authorization.setId(count.incrementAndGet());

        // Create the Authorization
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(authorization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, authorizationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authorization in the database
        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuthorization() throws Exception {
        int databaseSizeBeforeUpdate = authorizationRepository.findAll().size();
        authorization.setId(count.incrementAndGet());

        // Create the Authorization
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(authorization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authorization in the database
        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuthorization() throws Exception {
        int databaseSizeBeforeUpdate = authorizationRepository.findAll().size();
        authorization.setId(count.incrementAndGet());

        // Create the Authorization
        AuthorizationDTO authorizationDTO = authorizationMapper.toDto(authorization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorizationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(authorizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Authorization in the database
        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAuthorization() throws Exception {
        // Initialize the database
        authorizationRepository.saveAndFlush(authorization);

        int databaseSizeBeforeDelete = authorizationRepository.findAll().size();

        // Delete the authorization
        restAuthorizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, authorization.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Authorization> authorizationList = authorizationRepository.findAll();
        assertThat(authorizationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
