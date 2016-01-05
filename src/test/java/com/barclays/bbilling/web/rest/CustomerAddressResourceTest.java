package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.CustomerAddress;
import com.barclays.bbilling.repository.CustomerAddressRepository;
import com.barclays.bbilling.repository.search.CustomerAddressSearchRepository;
import com.barclays.bbilling.web.rest.dto.CustomerAddressDTO;
import com.barclays.bbilling.web.rest.mapper.CustomerAddressMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barclays.bbilling.domain.enumeration.CustomerAddressType;

/**
 * Test class for the CustomerAddressResource REST controller.
 *
 * @see CustomerAddressResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerAddressResourceTest {



private static final CustomerAddressType DEFAULT_CUSTOMER_ADD_TYPE = CustomerAddressType.PERMANENT;
    private static final CustomerAddressType UPDATED_CUSTOMER_ADD_TYPE = CustomerAddressType.TEMPORARY;
    private static final String DEFAULT_CUSTOMER_ADD_LINE_ONE = "A";
    private static final String UPDATED_CUSTOMER_ADD_LINE_ONE = "B";
    private static final String DEFAULT_CUSTOMER_ADD_LINE_TWO = "A";
    private static final String UPDATED_CUSTOMER_ADD_LINE_TWO = "B";
    private static final String DEFAULT_CUSTOMER_CITY = "A";
    private static final String UPDATED_CUSTOMER_CITY = "B";
    private static final String DEFAULT_CUSTOMER_STATE = "A";
    private static final String UPDATED_CUSTOMER_STATE = "B";
    private static final String DEFAULT_CUSTOMER_ADD_ZIP = "A";
    private static final String UPDATED_CUSTOMER_ADD_ZIP = "B";

    @Inject
    private CustomerAddressRepository customerAddressRepository;

    @Inject
    private CustomerAddressMapper customerAddressMapper;

    @Inject
    private CustomerAddressSearchRepository customerAddressSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCustomerAddressMockMvc;

    private CustomerAddress customerAddress;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerAddressResource customerAddressResource = new CustomerAddressResource();
        ReflectionTestUtils.setField(customerAddressResource, "customerAddressRepository", customerAddressRepository);
        ReflectionTestUtils.setField(customerAddressResource, "customerAddressMapper", customerAddressMapper);
        ReflectionTestUtils.setField(customerAddressResource, "customerAddressSearchRepository", customerAddressSearchRepository);
        this.restCustomerAddressMockMvc = MockMvcBuilders.standaloneSetup(customerAddressResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        customerAddress = new CustomerAddress();
        customerAddress.setCustomerAddType(DEFAULT_CUSTOMER_ADD_TYPE);
        customerAddress.setCustomerAddLineOne(DEFAULT_CUSTOMER_ADD_LINE_ONE);
        customerAddress.setCustomerAddLineTwo(DEFAULT_CUSTOMER_ADD_LINE_TWO);
        customerAddress.setCustomerCity(DEFAULT_CUSTOMER_CITY);
        customerAddress.setCustomerState(DEFAULT_CUSTOMER_STATE);
        customerAddress.setCustomerAddZip(DEFAULT_CUSTOMER_ADD_ZIP);
    }

    @Test
    @Transactional
    public void createCustomerAddress() throws Exception {
        int databaseSizeBeforeCreate = customerAddressRepository.findAll().size();

        // Create the CustomerAddress
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(post("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isCreated());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeCreate + 1);
        CustomerAddress testCustomerAddress = customerAddresss.get(customerAddresss.size() - 1);
        assertThat(testCustomerAddress.getCustomerAddType()).isEqualTo(DEFAULT_CUSTOMER_ADD_TYPE);
        assertThat(testCustomerAddress.getCustomerAddLineOne()).isEqualTo(DEFAULT_CUSTOMER_ADD_LINE_ONE);
        assertThat(testCustomerAddress.getCustomerAddLineTwo()).isEqualTo(DEFAULT_CUSTOMER_ADD_LINE_TWO);
        assertThat(testCustomerAddress.getCustomerCity()).isEqualTo(DEFAULT_CUSTOMER_CITY);
        assertThat(testCustomerAddress.getCustomerState()).isEqualTo(DEFAULT_CUSTOMER_STATE);
        assertThat(testCustomerAddress.getCustomerAddZip()).isEqualTo(DEFAULT_CUSTOMER_ADD_ZIP);
    }

    @Test
    @Transactional
    public void checkCustomerAddTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAddressRepository.findAll().size();
        // set the field null
        customerAddress.setCustomerAddType(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(post("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerAddLineOneIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAddressRepository.findAll().size();
        // set the field null
        customerAddress.setCustomerAddLineOne(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(post("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerAddLineTwoIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAddressRepository.findAll().size();
        // set the field null
        customerAddress.setCustomerAddLineTwo(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(post("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAddressRepository.findAll().size();
        // set the field null
        customerAddress.setCustomerCity(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(post("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAddressRepository.findAll().size();
        // set the field null
        customerAddress.setCustomerState(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(post("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerAddZipIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerAddressRepository.findAll().size();
        // set the field null
        customerAddress.setCustomerAddZip(null);

        // Create the CustomerAddress, which fails.
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(post("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isBadRequest());

        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerAddresss() throws Exception {
        // Initialize the database
        customerAddressRepository.saveAndFlush(customerAddress);

        // Get all the customerAddresss
        restCustomerAddressMockMvc.perform(get("/api/customerAddresss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customerAddress.getId().intValue())))
                .andExpect(jsonPath("$.[*].customerAddType").value(hasItem(DEFAULT_CUSTOMER_ADD_TYPE.toString())))
                .andExpect(jsonPath("$.[*].customerAddLineOne").value(hasItem(DEFAULT_CUSTOMER_ADD_LINE_ONE.toString())))
                .andExpect(jsonPath("$.[*].customerAddLineTwo").value(hasItem(DEFAULT_CUSTOMER_ADD_LINE_TWO.toString())))
                .andExpect(jsonPath("$.[*].customerCity").value(hasItem(DEFAULT_CUSTOMER_CITY.toString())))
                .andExpect(jsonPath("$.[*].customerState").value(hasItem(DEFAULT_CUSTOMER_STATE.toString())))
                .andExpect(jsonPath("$.[*].customerAddZip").value(hasItem(DEFAULT_CUSTOMER_ADD_ZIP.toString())));
    }

    @Test
    @Transactional
    public void getCustomerAddress() throws Exception {
        // Initialize the database
        customerAddressRepository.saveAndFlush(customerAddress);

        // Get the customerAddress
        restCustomerAddressMockMvc.perform(get("/api/customerAddresss/{id}", customerAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customerAddress.getId().intValue()))
            .andExpect(jsonPath("$.customerAddType").value(DEFAULT_CUSTOMER_ADD_TYPE.toString()))
            .andExpect(jsonPath("$.customerAddLineOne").value(DEFAULT_CUSTOMER_ADD_LINE_ONE.toString()))
            .andExpect(jsonPath("$.customerAddLineTwo").value(DEFAULT_CUSTOMER_ADD_LINE_TWO.toString()))
            .andExpect(jsonPath("$.customerCity").value(DEFAULT_CUSTOMER_CITY.toString()))
            .andExpect(jsonPath("$.customerState").value(DEFAULT_CUSTOMER_STATE.toString()))
            .andExpect(jsonPath("$.customerAddZip").value(DEFAULT_CUSTOMER_ADD_ZIP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerAddress() throws Exception {
        // Get the customerAddress
        restCustomerAddressMockMvc.perform(get("/api/customerAddresss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerAddress() throws Exception {
        // Initialize the database
        customerAddressRepository.saveAndFlush(customerAddress);

		int databaseSizeBeforeUpdate = customerAddressRepository.findAll().size();

        // Update the customerAddress
        customerAddress.setCustomerAddType(UPDATED_CUSTOMER_ADD_TYPE);
        customerAddress.setCustomerAddLineOne(UPDATED_CUSTOMER_ADD_LINE_ONE);
        customerAddress.setCustomerAddLineTwo(UPDATED_CUSTOMER_ADD_LINE_TWO);
        customerAddress.setCustomerCity(UPDATED_CUSTOMER_CITY);
        customerAddress.setCustomerState(UPDATED_CUSTOMER_STATE);
        customerAddress.setCustomerAddZip(UPDATED_CUSTOMER_ADD_ZIP);
        CustomerAddressDTO customerAddressDTO = customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);

        restCustomerAddressMockMvc.perform(put("/api/customerAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerAddressDTO)))
                .andExpect(status().isOk());

        // Validate the CustomerAddress in the database
        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeUpdate);
        CustomerAddress testCustomerAddress = customerAddresss.get(customerAddresss.size() - 1);
        assertThat(testCustomerAddress.getCustomerAddType()).isEqualTo(UPDATED_CUSTOMER_ADD_TYPE);
        assertThat(testCustomerAddress.getCustomerAddLineOne()).isEqualTo(UPDATED_CUSTOMER_ADD_LINE_ONE);
        assertThat(testCustomerAddress.getCustomerAddLineTwo()).isEqualTo(UPDATED_CUSTOMER_ADD_LINE_TWO);
        assertThat(testCustomerAddress.getCustomerCity()).isEqualTo(UPDATED_CUSTOMER_CITY);
        assertThat(testCustomerAddress.getCustomerState()).isEqualTo(UPDATED_CUSTOMER_STATE);
        assertThat(testCustomerAddress.getCustomerAddZip()).isEqualTo(UPDATED_CUSTOMER_ADD_ZIP);
    }

    @Test
    @Transactional
    public void deleteCustomerAddress() throws Exception {
        // Initialize the database
        customerAddressRepository.saveAndFlush(customerAddress);

		int databaseSizeBeforeDelete = customerAddressRepository.findAll().size();

        // Get the customerAddress
        restCustomerAddressMockMvc.perform(delete("/api/customerAddresss/{id}", customerAddress.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerAddress> customerAddresss = customerAddressRepository.findAll();
        assertThat(customerAddresss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
