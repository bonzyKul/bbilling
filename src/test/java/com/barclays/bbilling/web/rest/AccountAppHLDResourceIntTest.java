package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.AccountAppHLD;
import com.barclays.bbilling.repository.AccountAppHLDRepository;
import com.barclays.bbilling.repository.search.AccountAppHLDSearchRepository;
import com.barclays.bbilling.web.rest.dto.AccountAppHLDDTO;
import com.barclays.bbilling.web.rest.mapper.AccountAppHLDMapper;

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


/**
 * Test class for the AccountAppHLDResource REST controller.
 *
 * @see AccountAppHLDResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AccountAppHLDResourceIntTest {


    private static final LocalDate DEFAULT_APP_HLDSTART_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APP_HLDSTART_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_APP_HLDEND_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APP_HLDEND_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private AccountAppHLDRepository accountAppHLDRepository;

    @Inject
    private AccountAppHLDMapper accountAppHLDMapper;

    @Inject
    private AccountAppHLDSearchRepository accountAppHLDSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAccountAppHLDMockMvc;

    private AccountAppHLD accountAppHLD;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccountAppHLDResource accountAppHLDResource = new AccountAppHLDResource();
        ReflectionTestUtils.setField(accountAppHLDResource, "accountAppHLDRepository", accountAppHLDRepository);
        ReflectionTestUtils.setField(accountAppHLDResource, "accountAppHLDMapper", accountAppHLDMapper);
        ReflectionTestUtils.setField(accountAppHLDResource, "accountAppHLDSearchRepository", accountAppHLDSearchRepository);
        this.restAccountAppHLDMockMvc = MockMvcBuilders.standaloneSetup(accountAppHLDResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        accountAppHLD = new AccountAppHLD();
        accountAppHLD.setAppHLDStartDate(DEFAULT_APP_HLDSTART_DATE);
        accountAppHLD.setAppHLDEndDate(DEFAULT_APP_HLDEND_DATE);
    }

    @Test
    @Transactional
    public void createAccountAppHLD() throws Exception {
        int databaseSizeBeforeCreate = accountAppHLDRepository.findAll().size();

        // Create the AccountAppHLD
        AccountAppHLDDTO accountAppHLDDTO = accountAppHLDMapper.accountAppHLDToAccountAppHLDDTO(accountAppHLD);

        restAccountAppHLDMockMvc.perform(post("/api/accountAppHLDs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountAppHLDDTO)))
                .andExpect(status().isCreated());

        // Validate the AccountAppHLD in the database
        List<AccountAppHLD> accountAppHLDs = accountAppHLDRepository.findAll();
        assertThat(accountAppHLDs).hasSize(databaseSizeBeforeCreate + 1);
        AccountAppHLD testAccountAppHLD = accountAppHLDs.get(accountAppHLDs.size() - 1);
        assertThat(testAccountAppHLD.getAppHLDStartDate()).isEqualTo(DEFAULT_APP_HLDSTART_DATE);
        assertThat(testAccountAppHLD.getAppHLDEndDate()).isEqualTo(DEFAULT_APP_HLDEND_DATE);
    }

    @Test
    @Transactional
    public void checkAppHLDStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAppHLDRepository.findAll().size();
        // set the field null
        accountAppHLD.setAppHLDStartDate(null);

        // Create the AccountAppHLD, which fails.
        AccountAppHLDDTO accountAppHLDDTO = accountAppHLDMapper.accountAppHLDToAccountAppHLDDTO(accountAppHLD);

        restAccountAppHLDMockMvc.perform(post("/api/accountAppHLDs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountAppHLDDTO)))
                .andExpect(status().isBadRequest());

        List<AccountAppHLD> accountAppHLDs = accountAppHLDRepository.findAll();
        assertThat(accountAppHLDs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAppHLDEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAppHLDRepository.findAll().size();
        // set the field null
        accountAppHLD.setAppHLDEndDate(null);

        // Create the AccountAppHLD, which fails.
        AccountAppHLDDTO accountAppHLDDTO = accountAppHLDMapper.accountAppHLDToAccountAppHLDDTO(accountAppHLD);

        restAccountAppHLDMockMvc.perform(post("/api/accountAppHLDs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountAppHLDDTO)))
                .andExpect(status().isBadRequest());

        List<AccountAppHLD> accountAppHLDs = accountAppHLDRepository.findAll();
        assertThat(accountAppHLDs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccountAppHLDs() throws Exception {
        // Initialize the database
        accountAppHLDRepository.saveAndFlush(accountAppHLD);

        // Get all the accountAppHLDs
        restAccountAppHLDMockMvc.perform(get("/api/accountAppHLDs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(accountAppHLD.getId().intValue())))
                .andExpect(jsonPath("$.[*].appHLDStartDate").value(hasItem(DEFAULT_APP_HLDSTART_DATE.toString())))
                .andExpect(jsonPath("$.[*].appHLDEndDate").value(hasItem(DEFAULT_APP_HLDEND_DATE.toString())));
    }

    @Test
    @Transactional
    public void getAccountAppHLD() throws Exception {
        // Initialize the database
        accountAppHLDRepository.saveAndFlush(accountAppHLD);

        // Get the accountAppHLD
        restAccountAppHLDMockMvc.perform(get("/api/accountAppHLDs/{id}", accountAppHLD.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(accountAppHLD.getId().intValue()))
            .andExpect(jsonPath("$.appHLDStartDate").value(DEFAULT_APP_HLDSTART_DATE.toString()))
            .andExpect(jsonPath("$.appHLDEndDate").value(DEFAULT_APP_HLDEND_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountAppHLD() throws Exception {
        // Get the accountAppHLD
        restAccountAppHLDMockMvc.perform(get("/api/accountAppHLDs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountAppHLD() throws Exception {
        // Initialize the database
        accountAppHLDRepository.saveAndFlush(accountAppHLD);

		int databaseSizeBeforeUpdate = accountAppHLDRepository.findAll().size();

        // Update the accountAppHLD
        accountAppHLD.setAppHLDStartDate(UPDATED_APP_HLDSTART_DATE);
        accountAppHLD.setAppHLDEndDate(UPDATED_APP_HLDEND_DATE);
        AccountAppHLDDTO accountAppHLDDTO = accountAppHLDMapper.accountAppHLDToAccountAppHLDDTO(accountAppHLD);

        restAccountAppHLDMockMvc.perform(put("/api/accountAppHLDs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountAppHLDDTO)))
                .andExpect(status().isOk());

        // Validate the AccountAppHLD in the database
        List<AccountAppHLD> accountAppHLDs = accountAppHLDRepository.findAll();
        assertThat(accountAppHLDs).hasSize(databaseSizeBeforeUpdate);
        AccountAppHLD testAccountAppHLD = accountAppHLDs.get(accountAppHLDs.size() - 1);
        assertThat(testAccountAppHLD.getAppHLDStartDate()).isEqualTo(UPDATED_APP_HLDSTART_DATE);
        assertThat(testAccountAppHLD.getAppHLDEndDate()).isEqualTo(UPDATED_APP_HLDEND_DATE);
    }

    @Test
    @Transactional
    public void deleteAccountAppHLD() throws Exception {
        // Initialize the database
        accountAppHLDRepository.saveAndFlush(accountAppHLD);

		int databaseSizeBeforeDelete = accountAppHLDRepository.findAll().size();

        // Get the accountAppHLD
        restAccountAppHLDMockMvc.perform(delete("/api/accountAppHLDs/{id}", accountAppHLD.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountAppHLD> accountAppHLDs = accountAppHLDRepository.findAll();
        assertThat(accountAppHLDs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
