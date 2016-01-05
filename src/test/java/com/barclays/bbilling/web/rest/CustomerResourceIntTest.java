package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.Customer;
import com.barclays.bbilling.repository.CustomerRepository;
import com.barclays.bbilling.repository.search.CustomerSearchRepository;
import com.barclays.bbilling.web.rest.dto.CustomerDTO;
import com.barclays.bbilling.web.rest.mapper.CustomerMapper;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barclays.bbilling.domain.enumeration.CustomerStatus;
import com.barclays.bbilling.domain.enumeration.CustomerType;
import com.barclays.bbilling.domain.enumeration.CustomerTier;

/**
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerResourceIntTest {


    private static final Integer DEFAULT_CUSTOMER_ID = 1;
    private static final Integer UPDATED_CUSTOMER_ID = 2;
    private static final String DEFAULT_CUSTOMER_FNAME = "A";
    private static final String UPDATED_CUSTOMER_FNAME = "B";
    private static final String DEFAULT_CUSTOMER_MNAME = "A";
    private static final String UPDATED_CUSTOMER_MNAME = "B";
    private static final String DEFAULT_CUSTOMER_LNAME = "A";
    private static final String UPDATED_CUSTOMER_LNAME = "B";

    private static final LocalDate DEFAULT_CUSTOMER_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CUSTOMER_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CUSTOMER_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CUSTOMER_END_DATE = LocalDate.now(ZoneId.systemDefault());


private static final CustomerStatus DEFAULT_CUSTOMER_STATUS = CustomerStatus.Active;
    private static final CustomerStatus UPDATED_CUSTOMER_STATUS = CustomerStatus.Inactive;


private static final CustomerType DEFAULT_CUSTOMER_TYPE = CustomerType.STAFF;
    private static final CustomerType UPDATED_CUSTOMER_TYPE = CustomerType.PREIMERE;
    private static final String DEFAULT_CUSTOMER_EMAIL = "AAAAA";
    private static final String UPDATED_CUSTOMER_EMAIL = "BBBBB";
    private static final String DEFAULT_CUSTOMER_TEL_NO = "AAAAA";
    private static final String UPDATED_CUSTOMER_TEL_NO = "BBBBB";
    private static final String DEFAULT_CUSTOMER_MOBILE_NO = "AAAAA";
    private static final String UPDATED_CUSTOMER_MOBILE_NO = "BBBBB";


private static final CustomerTier DEFAULT_CUSTOMER_TIER = CustomerTier.TIER0;
    private static final CustomerTier UPDATED_CUSTOMER_TIER = CustomerTier.TIER1;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CustomerMapper customerMapper;

    @Inject
    private CustomerSearchRepository customerSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerResource customerResource = new CustomerResource();
        ReflectionTestUtils.setField(customerResource, "customerRepository", customerRepository);
        ReflectionTestUtils.setField(customerResource, "customerMapper", customerMapper);
        ReflectionTestUtils.setField(customerResource, "customerSearchRepository", customerSearchRepository);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        customer = new Customer();
        customer.setCustomerID(DEFAULT_CUSTOMER_ID);
        customer.setCustomerFName(DEFAULT_CUSTOMER_FNAME);
        customer.setCustomerMName(DEFAULT_CUSTOMER_MNAME);
        customer.setCustomerLName(DEFAULT_CUSTOMER_LNAME);
        customer.setCustomerStartDate(DEFAULT_CUSTOMER_START_DATE);
        customer.setCustomerEndDate(DEFAULT_CUSTOMER_END_DATE);
        customer.setCustomerStatus(DEFAULT_CUSTOMER_STATUS);
        customer.setCustomerType(DEFAULT_CUSTOMER_TYPE);
        customer.setCustomerEmail(DEFAULT_CUSTOMER_EMAIL);
        customer.setCustomerTelNo(DEFAULT_CUSTOMER_TEL_NO);
        customer.setCustomerMobileNo(DEFAULT_CUSTOMER_MOBILE_NO);
        customer.setCustomerTier(DEFAULT_CUSTOMER_TIER);
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customers.get(customers.size() - 1);
        assertThat(testCustomer.getCustomerID()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testCustomer.getCustomerFName()).isEqualTo(DEFAULT_CUSTOMER_FNAME);
        assertThat(testCustomer.getCustomerMName()).isEqualTo(DEFAULT_CUSTOMER_MNAME);
        assertThat(testCustomer.getCustomerLName()).isEqualTo(DEFAULT_CUSTOMER_LNAME);
        assertThat(testCustomer.getCustomerStartDate()).isEqualTo(DEFAULT_CUSTOMER_START_DATE);
        assertThat(testCustomer.getCustomerEndDate()).isEqualTo(DEFAULT_CUSTOMER_END_DATE);
        assertThat(testCustomer.getCustomerStatus()).isEqualTo(DEFAULT_CUSTOMER_STATUS);
        assertThat(testCustomer.getCustomerType()).isEqualTo(DEFAULT_CUSTOMER_TYPE);
        assertThat(testCustomer.getCustomerEmail()).isEqualTo(DEFAULT_CUSTOMER_EMAIL);
        assertThat(testCustomer.getCustomerTelNo()).isEqualTo(DEFAULT_CUSTOMER_TEL_NO);
        assertThat(testCustomer.getCustomerMobileNo()).isEqualTo(DEFAULT_CUSTOMER_MOBILE_NO);
        assertThat(testCustomer.getCustomerTier()).isEqualTo(DEFAULT_CUSTOMER_TIER);
    }

    @Test
    @Transactional
    public void checkCustomerIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerID(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isBadRequest());

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerFNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerFName(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isBadRequest());

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerLNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerLName(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isBadRequest());

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerStartDate(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isBadRequest());

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerEndDate(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isBadRequest());

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerStatus(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isBadRequest());

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerType(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isBadRequest());

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerEmail(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isBadRequest());

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerTelNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerTelNo(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isBadRequest());

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerMobileNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerMobileNo(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isBadRequest());

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerTierIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCustomerTier(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isBadRequest());

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customers
        restCustomerMockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
                .andExpect(jsonPath("$.[*].customerID").value(hasItem(DEFAULT_CUSTOMER_ID)))
                .andExpect(jsonPath("$.[*].customerFName").value(hasItem(DEFAULT_CUSTOMER_FNAME.toString())))
                .andExpect(jsonPath("$.[*].customerMName").value(hasItem(DEFAULT_CUSTOMER_MNAME.toString())))
                .andExpect(jsonPath("$.[*].customerLName").value(hasItem(DEFAULT_CUSTOMER_LNAME.toString())))
                .andExpect(jsonPath("$.[*].customerStartDate").value(hasItem(DEFAULT_CUSTOMER_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].customerEndDate").value(hasItem(DEFAULT_CUSTOMER_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].customerStatus").value(hasItem(DEFAULT_CUSTOMER_STATUS.toString())))
                .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE.toString())))
                .andExpect(jsonPath("$.[*].customerEmail").value(hasItem(DEFAULT_CUSTOMER_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].customerTelNo").value(hasItem(DEFAULT_CUSTOMER_TEL_NO.toString())))
                .andExpect(jsonPath("$.[*].customerMobileNo").value(hasItem(DEFAULT_CUSTOMER_MOBILE_NO.toString())))
                .andExpect(jsonPath("$.[*].customerTier").value(hasItem(DEFAULT_CUSTOMER_TIER.toString())));
    }

    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.customerID").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.customerFName").value(DEFAULT_CUSTOMER_FNAME.toString()))
            .andExpect(jsonPath("$.customerMName").value(DEFAULT_CUSTOMER_MNAME.toString()))
            .andExpect(jsonPath("$.customerLName").value(DEFAULT_CUSTOMER_LNAME.toString()))
            .andExpect(jsonPath("$.customerStartDate").value(DEFAULT_CUSTOMER_START_DATE.toString()))
            .andExpect(jsonPath("$.customerEndDate").value(DEFAULT_CUSTOMER_END_DATE.toString()))
            .andExpect(jsonPath("$.customerStatus").value(DEFAULT_CUSTOMER_STATUS.toString()))
            .andExpect(jsonPath("$.customerType").value(DEFAULT_CUSTOMER_TYPE.toString()))
            .andExpect(jsonPath("$.customerEmail").value(DEFAULT_CUSTOMER_EMAIL.toString()))
            .andExpect(jsonPath("$.customerTelNo").value(DEFAULT_CUSTOMER_TEL_NO.toString()))
            .andExpect(jsonPath("$.customerMobileNo").value(DEFAULT_CUSTOMER_MOBILE_NO.toString()))
            .andExpect(jsonPath("$.customerTier").value(DEFAULT_CUSTOMER_TIER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

		int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        customer.setCustomerID(UPDATED_CUSTOMER_ID);
        customer.setCustomerFName(UPDATED_CUSTOMER_FNAME);
        customer.setCustomerMName(UPDATED_CUSTOMER_MNAME);
        customer.setCustomerLName(UPDATED_CUSTOMER_LNAME);
        customer.setCustomerStartDate(UPDATED_CUSTOMER_START_DATE);
        customer.setCustomerEndDate(UPDATED_CUSTOMER_END_DATE);
        customer.setCustomerStatus(UPDATED_CUSTOMER_STATUS);
        customer.setCustomerType(UPDATED_CUSTOMER_TYPE);
        customer.setCustomerEmail(UPDATED_CUSTOMER_EMAIL);
        customer.setCustomerTelNo(UPDATED_CUSTOMER_TEL_NO);
        customer.setCustomerMobileNo(UPDATED_CUSTOMER_MOBILE_NO);
        customer.setCustomerTier(UPDATED_CUSTOMER_TIER);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        restCustomerMockMvc.perform(put("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customers.get(customers.size() - 1);
        assertThat(testCustomer.getCustomerID()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testCustomer.getCustomerFName()).isEqualTo(UPDATED_CUSTOMER_FNAME);
        assertThat(testCustomer.getCustomerMName()).isEqualTo(UPDATED_CUSTOMER_MNAME);
        assertThat(testCustomer.getCustomerLName()).isEqualTo(UPDATED_CUSTOMER_LNAME);
        assertThat(testCustomer.getCustomerStartDate()).isEqualTo(UPDATED_CUSTOMER_START_DATE);
        assertThat(testCustomer.getCustomerEndDate()).isEqualTo(UPDATED_CUSTOMER_END_DATE);
        assertThat(testCustomer.getCustomerStatus()).isEqualTo(UPDATED_CUSTOMER_STATUS);
        assertThat(testCustomer.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testCustomer.getCustomerEmail()).isEqualTo(UPDATED_CUSTOMER_EMAIL);
        assertThat(testCustomer.getCustomerTelNo()).isEqualTo(UPDATED_CUSTOMER_TEL_NO);
        assertThat(testCustomer.getCustomerMobileNo()).isEqualTo(UPDATED_CUSTOMER_MOBILE_NO);
        assertThat(testCustomer.getCustomerTier()).isEqualTo(UPDATED_CUSTOMER_TIER);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

		int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Get the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
