package com.gachokaerick.eshop.orders.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gachokaerick.eshop.orders.IntegrationTest;
import com.gachokaerick.eshop.orders.domain.User;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.Buyer;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.BuyerDomain;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.BuyerMapper;
import com.gachokaerick.eshop.orders.domain.enumeration.Gender;
import com.gachokaerick.eshop.orders.repository.BuyerRepository;
import com.gachokaerick.eshop.orders.service.dto.BuyerDTO;
import com.gachokaerick.eshop.orders.service.dto.UserDTO;
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
 * Integration tests for the {@link BuyerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BuyerResourceIntegrationTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_EMAIL = "u*\"L]h@$F./";
    private static final String UPDATED_EMAIL = "-tS{k*@ZXZ:.tmC";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final UserDTO USER_DTO = new UserDTO("1", "johndoe@example.com");

    private static final String ENTITY_API_URL = "/api/buyers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private BuyerMapper buyerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBuyerMockMvc;

    private Buyer buyer;

    static BuyerDTO getBuyerDTO() {
        return new BuyerDTO(null, DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, DEFAULT_GENDER, DEFAULT_EMAIL, DEFAULT_PHONE, USER_DTO);
    }

    static BuyerDTO getUpdatedBuyerDTO() {
        return new BuyerDTO(null, UPDATED_FIRST_NAME, UPDATED_LAST_NAME, UPDATED_GENDER, UPDATED_EMAIL, UPDATED_PHONE, USER_DTO);
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Buyer createEntity(EntityManager em) {
        BuyerDTO buyerDTO = getBuyerDTO();
        BuyerDomain buyerDomain = new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build();
        Buyer buyer = buyerDomain.toEntity(null);
        // Add required entity
        User user = UserResourceIntegrationTest.createEntity(em);
        em.persist(user);
        em.flush();
        buyerDomain.setUser(buyer, user);
        return buyer;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Buyer createUpdatedEntity(EntityManager em) {
        BuyerDTO buyerDTO = getUpdatedBuyerDTO();
        BuyerDomain buyerDomain = new BuyerDomain.BuyerBuilder().withDTO(buyerDTO).build();
        Buyer buyer = buyerDomain.toEntity(null);
        // Add required entity
        User user = UserResourceIntegrationTest.createEntity(em);
        em.persist(user);
        em.flush();
        buyerDomain.setUser(buyer, user);
        return buyer;
    }

    @BeforeEach
    public void initTest() {
        buyer = createEntity(em);
    }

    @Test
    @Transactional
    void createBuyer() throws Exception {
        int databaseSizeBeforeCreate = buyerRepository.findAll().size();
        // Create the Buyer
        BuyerDTO buyerDTO = buyerMapper.toDto(buyer);
        restBuyerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buyerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Buyer in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeCreate + 1);
        Buyer testBuyer = buyerList.get(buyerList.size() - 1);
        assertThat(testBuyer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testBuyer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testBuyer.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testBuyer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testBuyer.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void createBuyerWithExistingId() throws Exception {
        // Create the Buyer with an existing ID
        BuyerDTO buyerDTO = buyerMapper.toDto(buyer);
        buyerDTO.setId(1L);

        int databaseSizeBeforeCreate = buyerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBuyerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buyerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Buyer in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = buyerRepository.findAll().size();

        // Create the Buyer, which fails.
        BuyerDTO buyerDTO = buyerMapper.toDto(buyer);
        // set the field null
        buyerDTO.setFirstName(null);

        restBuyerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buyerDTO))
            )
            .andExpect(status().isBadRequest());

        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = buyerRepository.findAll().size();

        // Create the Buyer, which fails.
        BuyerDTO buyerDTO = buyerMapper.toDto(buyer);
        // set the field null
        buyerDTO.setLastName(null);

        restBuyerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buyerDTO))
            )
            .andExpect(status().isBadRequest());

        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = buyerRepository.findAll().size();

        // Create the Buyer, which fails.
        BuyerDTO buyerDTO = buyerMapper.toDto(buyer);
        // set the field null
        buyerDTO.setGender(null);

        restBuyerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buyerDTO))
            )
            .andExpect(status().isBadRequest());

        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = buyerRepository.findAll().size();

        // Create the Buyer, which fails.
        BuyerDTO buyerDTO = buyerMapper.toDto(buyer);
        // set the field null
        buyerDTO.setEmail(null);

        restBuyerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buyerDTO))
            )
            .andExpect(status().isBadRequest());

        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = buyerRepository.findAll().size();

        // Create the Buyer, which fails.
        BuyerDTO buyerDTO = buyerMapper.toDto(buyer);
        // set the field null
        buyerDTO.setPhone(null);

        restBuyerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buyerDTO))
            )
            .andExpect(status().isBadRequest());

        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBuyers() throws Exception {
        // Initialize the database
        buyerRepository.saveAndFlush(buyer);

        // Get all the buyerList
        restBuyerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(buyer.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    void toEntity() throws Exception {
        // Initialize the database
        buyerRepository.saveAndFlush(buyer);

        // Get the buyer
        restBuyerMockMvc
            .perform(get(ENTITY_API_URL_ID, buyer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(buyer.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    void getNonExistingBuyer() throws Exception {
        // Get the buyer
        restBuyerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBuyer() throws Exception {
        // Initialize the database
        buyerRepository.saveAndFlush(buyer);

        int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

        // Update the buyer
        Buyer updatedBuyer = buyerRepository.findById(buyer.getId()).get();
        // Disconnect from session so that the updates on updatedBuyer are not directly saved in db
        em.detach(updatedBuyer);

        BuyerDTO buyerDTO = buyerMapper.toDto(updatedBuyer);
        buyerDTO.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).gender(UPDATED_GENDER).email(UPDATED_EMAIL).phone(UPDATED_PHONE);

        restBuyerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, buyerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buyerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Buyer in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
        Buyer testBuyer = buyerList.get(buyerList.size() - 1);
        assertThat(testBuyer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testBuyer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testBuyer.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testBuyer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testBuyer.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingBuyer() throws Exception {
        int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

        // Create the Buyer
        BuyerDTO buyerDTO = buyerMapper.toDto(buyer);
        buyerDTO.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBuyerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, buyerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buyerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Buyer in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBuyer() throws Exception {
        int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

        // Create the Buyer
        BuyerDTO buyerDTO = buyerMapper.toDto(buyer);
        buyerDTO.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuyerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buyerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Buyer in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBuyer() throws Exception {
        int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

        // Create the Buyer
        BuyerDTO buyerDTO = buyerMapper.toDto(buyer);
        buyerDTO.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuyerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buyerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Buyer in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBuyerWithPatch() throws Exception {
        // Initialize the database
        buyerRepository.saveAndFlush(buyer);

        int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

        // Update the buyer using partial update
        BuyerDTO partialUpdatedBuyer = new BuyerDTO();
        partialUpdatedBuyer.setId(buyer.getId());

        partialUpdatedBuyer.lastName(UPDATED_LAST_NAME).gender(UPDATED_GENDER).email(UPDATED_EMAIL).phone(UPDATED_PHONE);

        restBuyerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBuyer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBuyer))
            )
            .andExpect(status().isOk());

        // Validate the Buyer in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
        Buyer testBuyer = buyerList.get(buyerList.size() - 1);
        assertThat(testBuyer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testBuyer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testBuyer.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testBuyer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testBuyer.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateBuyerWithPatch() throws Exception {
        // Initialize the database
        buyerRepository.saveAndFlush(buyer);

        int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

        // Update the buyer using partial update
        BuyerDTO partialUpdatedBuyer = new BuyerDTO();
        partialUpdatedBuyer.setId(buyer.getId());

        partialUpdatedBuyer
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE);

        restBuyerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBuyer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBuyer))
            )
            .andExpect(status().isOk());

        // Validate the Buyer in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
        Buyer testBuyer = buyerList.get(buyerList.size() - 1);
        assertThat(testBuyer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testBuyer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testBuyer.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testBuyer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testBuyer.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingBuyer() throws Exception {
        int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

        // Create the Buyer
        BuyerDTO buyerDTO = buyerMapper.toDto(buyer);
        buyerDTO.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBuyerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, buyerDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(buyerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Buyer in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBuyer() throws Exception {
        int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

        // Create the Buyer
        BuyerDTO buyerDTO = buyerMapper.toDto(buyer);
        buyerDTO.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuyerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(buyerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Buyer in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBuyer() throws Exception {
        int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

        // Create the Buyer
        BuyerDTO buyerDTO = buyerMapper.toDto(buyer);
        buyerDTO.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuyerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(buyerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Buyer in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBuyer() throws Exception {
        // Initialize the database
        buyerRepository.saveAndFlush(buyer);

        int databaseSizeBeforeDelete = buyerRepository.findAll().size();

        // Delete the buyer
        restBuyerMockMvc
            .perform(delete(ENTITY_API_URL_ID, buyer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
