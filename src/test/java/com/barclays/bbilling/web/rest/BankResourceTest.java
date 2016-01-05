package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.Bank;
import com.barclays.bbilling.repository.BankRepository;
import com.barclays.bbilling.repository.search.BankSearchRepository;
import com.barclays.bbilling.web.rest.dto.BankDTO;
import com.barclays.bbilling.web.rest.mapper.BankMapper;

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


/**
 * Test class for the BankResource REST controller.
 *
 * @see BankResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BankResourceTest {

    private static final String DEFAULT_BANK_CODE = "A";
    private static final String UPDATED_BANK_CODE = "B";
    private static final String DEFAULT_BANK_NAME = "A";
    private static final String UPDATED_BANK_NAME = "B";

    @Inject
    private BankRepository bankRepository;

    @Inject
    private BankMapper bankMapper;

    @Inject
    private BankSearchRepository bankSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBankMockMvc;

    private Bank bank;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BankResource bankResource = new BankResource();
        ReflectionTestUtils.setField(bankResource, "bankRepository", bankRepository);
        ReflectionTestUtils.setField(bankResource, "bankMapper", bankMapper);
        ReflectionTestUtils.setField(bankResource, "bankSearchRepository", bankSearchRepository);
        this.restBankMockMvc = MockMvcBuilders.standaloneSetup(bankResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bank = new Bank();
        bank.setBankCode(DEFAULT_BANK_CODE);
        bank.setBankName(DEFAULT_BANK_NAME);
    }

    @Test
    @Transactional
    public void createBank() throws Exception {
        int databaseSizeBeforeCreate = bankRepository.findAll().size();

        // Create the Bank
        BankDTO bankDTO = bankMapper.bankToBankDTO(bank);

        restBankMockMvc.perform(post("/api/banks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bankDTO)))
                .andExpect(status().isCreated());

        // Validate the Bank in the database
        List<Bank> banks = bankRepository.findAll();
        assertThat(banks).hasSize(databaseSizeBeforeCreate + 1);
        Bank testBank = banks.get(banks.size() - 1);
        assertThat(testBank.getBankCode()).isEqualTo(DEFAULT_BANK_CODE);
        assertThat(testBank.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
    }

    @Test
    @Transactional
    public void checkBankCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankRepository.findAll().size();
        // set the field null
        bank.setBankCode(null);

        // Create the Bank, which fails.
        BankDTO bankDTO = bankMapper.bankToBankDTO(bank);

        restBankMockMvc.perform(post("/api/banks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bankDTO)))
                .andExpect(status().isBadRequest());

        List<Bank> banks = bankRepository.findAll();
        assertThat(banks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBankNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankRepository.findAll().size();
        // set the field null
        bank.setBankName(null);

        // Create the Bank, which fails.
        BankDTO bankDTO = bankMapper.bankToBankDTO(bank);

        restBankMockMvc.perform(post("/api/banks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bankDTO)))
                .andExpect(status().isBadRequest());

        List<Bank> banks = bankRepository.findAll();
        assertThat(banks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBanks() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the banks
        restBankMockMvc.perform(get("/api/banks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bank.getId().intValue())))
                .andExpect(jsonPath("$.[*].bankCode").value(hasItem(DEFAULT_BANK_CODE.toString())))
                .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME.toString())));
    }

    @Test
    @Transactional
    public void getBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get the bank
        restBankMockMvc.perform(get("/api/banks/{id}", bank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bank.getId().intValue()))
            .andExpect(jsonPath("$.bankCode").value(DEFAULT_BANK_CODE.toString()))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBank() throws Exception {
        // Get the bank
        restBankMockMvc.perform(get("/api/banks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

		int databaseSizeBeforeUpdate = bankRepository.findAll().size();

        // Update the bank
        bank.setBankCode(UPDATED_BANK_CODE);
        bank.setBankName(UPDATED_BANK_NAME);
        BankDTO bankDTO = bankMapper.bankToBankDTO(bank);

        restBankMockMvc.perform(put("/api/banks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bankDTO)))
                .andExpect(status().isOk());

        // Validate the Bank in the database
        List<Bank> banks = bankRepository.findAll();
        assertThat(banks).hasSize(databaseSizeBeforeUpdate);
        Bank testBank = banks.get(banks.size() - 1);
        assertThat(testBank.getBankCode()).isEqualTo(UPDATED_BANK_CODE);
        assertThat(testBank.getBankName()).isEqualTo(UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    public void deleteBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

		int databaseSizeBeforeDelete = bankRepository.findAll().size();

        // Get the bank
        restBankMockMvc.perform(delete("/api/banks/{id}", bank.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Bank> banks = bankRepository.findAll();
        assertThat(banks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
