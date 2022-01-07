//package com.gachokaerick.eshop.orders.web.rest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.gachokaerick.eshop.orders.IntegrationTest;
//import com.gachokaerick.eshop.orders.domain.Address;
//import com.gachokaerick.eshop.orders.domain.aggregates.buyer.Buyer;
//import com.gachokaerick.eshop.orders.repository.AddressRepository;
//import com.gachokaerick.eshop.orders.service.dto.AddressDTO;
//import com.gachokaerick.eshop.orders.service.mapper.AddressMapper;
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
// * Integration tests for the {@link AddressResource} REST controller.
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@WithMockUser
//class AddressResourceIT {
//
//    private static final String DEFAULT_STREET = "AAAAAAAAAA";
//    private static final String UPDATED_STREET = "BBBBBBBBBB";
//
//    private static final String DEFAULT_CITY = "AAAAAAAAAA";
//    private static final String UPDATED_CITY = "BBBBBBBBBB";
//
//    private static final String DEFAULT_TOWN = "AAAAAAAAAA";
//    private static final String UPDATED_TOWN = "BBBBBBBBBB";
//
//    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
//    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";
//
//    private static final String DEFAULT_ZIPCODE = "AAAAAAAAAA";
//    private static final String UPDATED_ZIPCODE = "BBBBBBBBBB";
//
//    private static final String ENTITY_API_URL = "/api/addresses";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private AddressRepository addressRepository;
//
//    @Autowired
//    private AddressMapper addressMapper;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restAddressMockMvc;
//
//    private Address address;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Address createEntity(EntityManager em) {
//        Address address = new Address()
//            .street(DEFAULT_STREET)
//            .city(DEFAULT_CITY)
//            .town(DEFAULT_TOWN)
//            .country(DEFAULT_COUNTRY)
//            .zipcode(DEFAULT_ZIPCODE);
//        // Add required entity
//        Buyer buyer;
//        if (TestUtil.findAll(em, Buyer.class).isEmpty()) {
//            buyer = BuyerResourceIT.createEntity(em);
//            em.persist(buyer);
//            em.flush();
//        } else {
//            buyer = TestUtil.findAll(em, Buyer.class).get(0);
//        }
//        address.setBuyer(buyer);
//        return address;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Address createUpdatedEntity(EntityManager em) {
//        Address address = new Address()
//            .street(UPDATED_STREET)
//            .city(UPDATED_CITY)
//            .town(UPDATED_TOWN)
//            .country(UPDATED_COUNTRY)
//            .zipcode(UPDATED_ZIPCODE);
//        // Add required entity
//        Buyer buyer;
//        if (TestUtil.findAll(em, Buyer.class).isEmpty()) {
//            buyer = BuyerResourceIT.createUpdatedEntity(em);
//            em.persist(buyer);
//            em.flush();
//        } else {
//            buyer = TestUtil.findAll(em, Buyer.class).get(0);
//        }
//        address.setBuyer(buyer);
//        return address;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        address = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    void createAddress() throws Exception {
//        int databaseSizeBeforeCreate = addressRepository.findAll().size();
//        // Create the Address
//        AddressDTO addressDTO = addressMapper.toDto(address);
//        restAddressMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
//            )
//            .andExpect(status().isCreated());
//
//        // Validate the Address in the database
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeCreate + 1);
//        Address testAddress = addressList.get(addressList.size() - 1);
//        assertThat(testAddress.getStreet()).isEqualTo(DEFAULT_STREET);
//        assertThat(testAddress.getCity()).isEqualTo(DEFAULT_CITY);
//        assertThat(testAddress.getTown()).isEqualTo(DEFAULT_TOWN);
//        assertThat(testAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
//        assertThat(testAddress.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
//    }
//
//    @Test
//    @Transactional
//    void createAddressWithExistingId() throws Exception {
//        // Create the Address with an existing ID
//        address.setId(1L);
//        AddressDTO addressDTO = addressMapper.toDto(address);
//
//        int databaseSizeBeforeCreate = addressRepository.findAll().size();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restAddressMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Address in the database
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void checkCityIsRequired() throws Exception {
//        int databaseSizeBeforeTest = addressRepository.findAll().size();
//        // set the field null
//        address.setCity(null);
//
//        // Create the Address, which fails.
//        AddressDTO addressDTO = addressMapper.toDto(address);
//
//        restAddressMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void checkTownIsRequired() throws Exception {
//        int databaseSizeBeforeTest = addressRepository.findAll().size();
//        // set the field null
//        address.setTown(null);
//
//        // Create the Address, which fails.
//        AddressDTO addressDTO = addressMapper.toDto(address);
//
//        restAddressMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void checkCountryIsRequired() throws Exception {
//        int databaseSizeBeforeTest = addressRepository.findAll().size();
//        // set the field null
//        address.setCountry(null);
//
//        // Create the Address, which fails.
//        AddressDTO addressDTO = addressMapper.toDto(address);
//
//        restAddressMockMvc
//            .perform(
//                post(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void getAllAddresses() throws Exception {
//        // Initialize the database
//        addressRepository.saveAndFlush(address);
//
//        // Get all the addressList
//        restAddressMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
//            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
//            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
//            .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN)))
//            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
//            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE)));
//    }
//
//    @Test
//    @Transactional
//    void getAddress() throws Exception {
//        // Initialize the database
//        addressRepository.saveAndFlush(address);
//
//        // Get the address
//        restAddressMockMvc
//            .perform(get(ENTITY_API_URL_ID, address.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(address.getId().intValue()))
//            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
//            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
//            .andExpect(jsonPath("$.town").value(DEFAULT_TOWN))
//            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
//            .andExpect(jsonPath("$.zipcode").value(DEFAULT_ZIPCODE));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingAddress() throws Exception {
//        // Get the address
//        restAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putNewAddress() throws Exception {
//        // Initialize the database
//        addressRepository.saveAndFlush(address);
//
//        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
//
//        // Update the address
//        Address updatedAddress = addressRepository.findById(address.getId()).get();
//        // Disconnect from session so that the updates on updatedAddress are not directly saved in db
//        em.detach(updatedAddress);
//        updatedAddress.street(UPDATED_STREET).city(UPDATED_CITY).town(UPDATED_TOWN).country(UPDATED_COUNTRY).zipcode(UPDATED_ZIPCODE);
//        AddressDTO addressDTO = addressMapper.toDto(updatedAddress);
//
//        restAddressMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, addressDTO.getId())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Address in the database
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
//        Address testAddress = addressList.get(addressList.size() - 1);
//        assertThat(testAddress.getStreet()).isEqualTo(UPDATED_STREET);
//        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
//        assertThat(testAddress.getTown()).isEqualTo(UPDATED_TOWN);
//        assertThat(testAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
//        assertThat(testAddress.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingAddress() throws Exception {
//        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
//        address.setId(count.incrementAndGet());
//
//        // Create the Address
//        AddressDTO addressDTO = addressMapper.toDto(address);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restAddressMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, addressDTO.getId())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Address in the database
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchAddress() throws Exception {
//        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
//        address.setId(count.incrementAndGet());
//
//        // Create the Address
//        AddressDTO addressDTO = addressMapper.toDto(address);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restAddressMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Address in the database
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamAddress() throws Exception {
//        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
//        address.setId(count.incrementAndGet());
//
//        // Create the Address
//        AddressDTO addressDTO = addressMapper.toDto(address);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restAddressMockMvc
//            .perform(
//                put(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Address in the database
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdateAddressWithPatch() throws Exception {
//        // Initialize the database
//        addressRepository.saveAndFlush(address);
//
//        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
//
//        // Update the address using partial update
//        Address partialUpdatedAddress = new Address();
//        partialUpdatedAddress.setId(address.getId());
//
//        partialUpdatedAddress.city(UPDATED_CITY).town(UPDATED_TOWN).country(UPDATED_COUNTRY).zipcode(UPDATED_ZIPCODE);
//
//        restAddressMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedAddress.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddress))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Address in the database
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
//        Address testAddress = addressList.get(addressList.size() - 1);
//        assertThat(testAddress.getStreet()).isEqualTo(DEFAULT_STREET);
//        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
//        assertThat(testAddress.getTown()).isEqualTo(UPDATED_TOWN);
//        assertThat(testAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
//        assertThat(testAddress.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
//    }
//
//    @Test
//    @Transactional
//    void fullUpdateAddressWithPatch() throws Exception {
//        // Initialize the database
//        addressRepository.saveAndFlush(address);
//
//        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
//
//        // Update the address using partial update
//        Address partialUpdatedAddress = new Address();
//        partialUpdatedAddress.setId(address.getId());
//
//        partialUpdatedAddress
//            .street(UPDATED_STREET)
//            .city(UPDATED_CITY)
//            .town(UPDATED_TOWN)
//            .country(UPDATED_COUNTRY)
//            .zipcode(UPDATED_ZIPCODE);
//
//        restAddressMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedAddress.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddress))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Address in the database
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
//        Address testAddress = addressList.get(addressList.size() - 1);
//        assertThat(testAddress.getStreet()).isEqualTo(UPDATED_STREET);
//        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
//        assertThat(testAddress.getTown()).isEqualTo(UPDATED_TOWN);
//        assertThat(testAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
//        assertThat(testAddress.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingAddress() throws Exception {
//        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
//        address.setId(count.incrementAndGet());
//
//        // Create the Address
//        AddressDTO addressDTO = addressMapper.toDto(address);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restAddressMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, addressDTO.getId())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Address in the database
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchAddress() throws Exception {
//        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
//        address.setId(count.incrementAndGet());
//
//        // Create the Address
//        AddressDTO addressDTO = addressMapper.toDto(address);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restAddressMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Address in the database
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamAddress() throws Exception {
//        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
//        address.setId(count.incrementAndGet());
//
//        // Create the Address
//        AddressDTO addressDTO = addressMapper.toDto(address);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restAddressMockMvc
//            .perform(
//                patch(ENTITY_API_URL)
//                    .with(csrf())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Address in the database
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deleteAddress() throws Exception {
//        // Initialize the database
//        addressRepository.saveAndFlush(address);
//
//        int databaseSizeBeforeDelete = addressRepository.findAll().size();
//
//        // Delete the address
//        restAddressMockMvc
//            .perform(delete(ENTITY_API_URL_ID, address.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        List<Address> addressList = addressRepository.findAll();
//        assertThat(addressList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
