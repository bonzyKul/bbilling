package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.AccountFamily;
import com.barclays.bbilling.repository.AccountFamilyRepository;
import com.barclays.bbilling.repository.search.AccountFamilySearchRepository;
import com.barclays.bbilling.web.rest.dto.AccountFamilyDTO;
import com.barclays.bbilling.web.rest.mapper.AccountFamilyMapper;

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
 * Test class for the AccountFamilyResource REST controller.
 *
 * @see AccountFamilyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AccountFamilyResourceIntTest {

    private static final String DEFAULT_ACCOUNT_FAMILY_CODE = "A";
    private static final String UPDATED_ACCOUNT_FAMILY_CODE = "B";
    private static final String DEFAULT_ACCOUNT_FAMILY_DESC = "A";
    private static final String UPDATED_ACCOUNT_FAMILY_DESC = "B";

    @Inject
    private AccountFamilyRepository accountFamilyRepository;

    @Inject
    private AccountFamilyMapper accountFamilyMapper;

    @Inject
    private AccountFamilySearchRepository accountFamilySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAccountFamilyMockMvc;

    private AccountFamily accountFamily;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccountFamilyResource accountFamilyResource = new AccountFamilyResource();
        ReflectionTestUtils.setField(accountFamilyResource, "accountFamilyRepository", accountFamilyRepository);
        ReflectionTestUtils.setField(accountFamilyResource, "accountFamilyMapper", accountFamilyMapper);
        ReflectionTestUtils.setField(accountFamilyResource, "accountFamilySearchRepository", accountFamilySearchRepository);
        this.restAccountFamilyMockMvc = MockMvcBuilders.standaloneSetup(accountFamilyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        accountFamily = new AccountFamily();
        accountFamily.setAccountFamilyCode(DEFAULT_ACCOUNT_FAMILY_CODE);
        accountFamily.setAccountFamilyDesc(DEFAULT_ACCOUNT_FAMILY_DESC);
    }

    @Test
    @Transactional
    public void createAccountFamily() throws Exception {
        int databaseSizeBeforeCreate = accountFamilyRepository.findAll().size();

        // Create the AccountFamily
        AccountFamilyDTO accountFamilyDTO = accountFamilyMapper.accountFamilyToAccountFamilyDTO(accountFamily);

        restAccountFamilyMockMvc.perform(post("/api/accountFamilys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountFamilyDTO)))
                .andExpect(status().isCreated());

        // Validate the AccountFamily in the database
        List<AccountFamily> accountFamilys = accountFamilyRepository.findAll();
        assertThat(accountFamilys).hasSize(databaseSizeBeforeCreate + 1);
        AccountFamily testAccountFamily = accountFamilys.get(accountFamilys.size() - 1);
        assertThat(testAccountFamily.getAccountFamilyCode()).isEqualTo(DEFAULT_ACCOUNT_FAMILY_CODE);
        assertThat(testAccountFamily.getAccountFamilyDesc()).isEqualTo(DEFAULT_ACCOUNT_FAMILY_DESC);
    }

    @Test
    @Transactional
    public void checkAccountFamilyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountFamilyRepository.findAll().size();
        // set the field null
        accountFamily.setAccountFamilyCode(null);

        // Create the AccountFamily, which fails.
        AccountFamilyDTO accountFamilyDTO = accountFamilyMapper.accountFamilyToAccountFamilyDTO(accountFamily);

        restAccountFamilyMockMvc.perform(post("/api/accountFamilys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountFamilyDTO)))
                .andExpect(status().isBadRequest());

        List<AccountFamily> accountFamilys = accountFamilyRepository.findAll();
        assertThat(accountFamilys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountFamilyDescIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountFamilyRepository.findAll().size();
        // set the field null
        accountFamily.setAccountFamilyDesc(null);

        // Create the AccountFamily, which fails.
        AccountFamilyDTO accountFamilyDTO = accountFamilyMapper.accountFamilyToAccountFamilyDTO(accountFamily);

        restAccountFamilyMockMvc.perform(post("/api/accountFamilys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountFamilyDTO)))
                .andExpect(status().isBadRequest());

        List<AccountFamily> accountFamilys = accountFamilyRepository.findAll();
        assertThat(accountFamilys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccountFamilys() throws Exception {
        // Initialize the database
        accountFamilyRepository.saveAndFlush(accountFamily);

        // Get all the accountFamilys
        restAccountFamilyMockMvc.perform(get("/api/accountFamilys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(accountFamily.getId().intValue())))
                .andExpect(jsonPath("$.[*].accountFamilyCode").value(hasItem(DEFAULT_ACCOUNT_FAMILY_CODE.toString())))
                .andExpect(jsonPath("$.[*].accountFamilyDesc").value(hasItem(DEFAULT_ACCOUNT_FAMILY_DESC.toString())));
    }

    @Test
    @Transactional
    public void getAccountFamily() throws Exception {
        // Initialize the database
        accountFamilyRepository.saveAndFlush(accountFamily);

        // Get the accountFamily
        restAccountFamilyMockMvc.perform(get("/api/accountFamilys/{id}", accountFamily.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(accountFamily.getId().intValue()))
            .andExpect(jsonPath("$.accountFamilyCode").value(DEFAULT_ACCOUNT_FAMILY_CODE.toString()))
            .andExpect(jsonPath("$.accountFamilyDesc").value(DEFAULT_ACCOUNT_FAMILY_DESC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountFamily() throws Exception {
        // Get the accountFamily
        restAccountFamilyMockMvc.perform(get("/api/accountFamilys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountFamily() throws Exception {
        // Initialize the database
        accountFamilyRepository.saveAndFlush(accountFamily);

		int databaseSizeBeforeUpdate = accountFamilyRepository.findAll().size();

        // Update the accountFamily
        accountFamily.setAccountFamilyCode(UPDATED_ACCOUNT_FAMILY_CODE);
        accountFamily.setAccountFamilyDesc(UPDATED_ACCOUNT_FAMILY_DESC);
        AccountFamilyDTO accountFamilyDTO = accountFamilyMapper.accountFamilyToAccountFamilyDTO(accountFamily);

        restAccountFamilyMockMvc.perform(put("/api/accountFamilys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountFamilyDTO)))
                .andExpect(status().isOk());

        // Validate the AccountFamily in the database
        List<AccountFamily> accountFamilys = accountFamilyRepository.findAll();
        assertThat(accountFamilys).hasSize(databaseSizeBeforeUpdate);
        AccountFamily testAccountFamily = accountFamilys.get(accountFamilys.size() - 1);
        assertThat(testAccountFamily.getAccountFamilyCode()).isEqualTo(UPDATED_ACCOUNT_FAMILY_CODE);
        assertThat(testAccountFamily.getAccountFamilyDesc()).isEqualTo(UPDATED_ACCOUNT_FAMILY_DESC);
    }

    @Test
    @Transactional
    public void deleteAccountFamily() throws Exception {
        // Initialize the database
        accountFamilyRepository.saveAndFlush(accountFamily);

		int databaseSizeBeforeDelete = accountFamilyRepository.findAll().size();

        // Get the accountFamily
        restAccountFamilyMockMvc.perform(delete("/api/accountFamilys/{id}", accountFamily.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountFamily> accountFamilys = accountFamilyRepository.findAll();
        assertThat(accountFamilys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
