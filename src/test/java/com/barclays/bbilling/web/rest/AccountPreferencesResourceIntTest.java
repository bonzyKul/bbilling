package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.AccountPreferences;
import com.barclays.bbilling.repository.AccountPreferencesRepository;
import com.barclays.bbilling.repository.search.AccountPreferencesSearchRepository;
import com.barclays.bbilling.web.rest.dto.AccountPreferencesDTO;
import com.barclays.bbilling.web.rest.mapper.AccountPreferencesMapper;

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
 * Test class for the AccountPreferencesResource REST controller.
 *
 * @see AccountPreferencesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AccountPreferencesResourceIntTest {


    private static final Boolean DEFAULT_ACCOUNT_ESTATEMENT = false;
    private static final Boolean UPDATED_ACCOUNT_ESTATEMENT = true;

    private static final Boolean DEFAULT_ACCOUNT_NOTIFICATION = false;
    private static final Boolean UPDATED_ACCOUNT_NOTIFICATION = true;

    private static final Boolean DEFAULT_ACCOUNT_LVL_BILLING = false;
    private static final Boolean UPDATED_ACCOUNT_LVL_BILLING = true;

    private static final Boolean DEFAULT_ACCOUNT_PRINT_STMT = false;
    private static final Boolean UPDATED_ACCOUNT_PRINT_STMT = true;

    private static final Boolean DEFAULT_ACCOUNT_REWARD_SUPP_IND = false;
    private static final Boolean UPDATED_ACCOUNT_REWARD_SUPP_IND = true;

    private static final Integer DEFAULT_ACCOUNT_PACK_CNT = 1;
    private static final Integer UPDATED_ACCOUNT_PACK_CNT = 2;

    private static final Boolean DEFAULT_ACCOUNT_CFIND = false;
    private static final Boolean UPDATED_ACCOUNT_CFIND = true;

    private static final Boolean DEFAULT_ACCOUNT_PNCS = false;
    private static final Boolean UPDATED_ACCOUNT_PNCS = true;

    private static final Boolean DEFAULT_ACCOUNT_BILLING_DEFAULT = false;
    private static final Boolean UPDATED_ACCOUNT_BILLING_DEFAULT = true;

    @Inject
    private AccountPreferencesRepository accountPreferencesRepository;

    @Inject
    private AccountPreferencesMapper accountPreferencesMapper;

    @Inject
    private AccountPreferencesSearchRepository accountPreferencesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAccountPreferencesMockMvc;

    private AccountPreferences accountPreferences;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccountPreferencesResource accountPreferencesResource = new AccountPreferencesResource();
        ReflectionTestUtils.setField(accountPreferencesResource, "accountPreferencesRepository", accountPreferencesRepository);
        ReflectionTestUtils.setField(accountPreferencesResource, "accountPreferencesMapper", accountPreferencesMapper);
        ReflectionTestUtils.setField(accountPreferencesResource, "accountPreferencesSearchRepository", accountPreferencesSearchRepository);
        this.restAccountPreferencesMockMvc = MockMvcBuilders.standaloneSetup(accountPreferencesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        accountPreferences = new AccountPreferences();
        accountPreferences.setAccountEStatement(DEFAULT_ACCOUNT_ESTATEMENT);
        accountPreferences.setAccountNotification(DEFAULT_ACCOUNT_NOTIFICATION);
        accountPreferences.setAccountLvlBilling(DEFAULT_ACCOUNT_LVL_BILLING);
        accountPreferences.setAccountPrintStmt(DEFAULT_ACCOUNT_PRINT_STMT);
        accountPreferences.setAccountRewardSuppInd(DEFAULT_ACCOUNT_REWARD_SUPP_IND);
        accountPreferences.setAccountPackCnt(DEFAULT_ACCOUNT_PACK_CNT);
        accountPreferences.setAccountCFInd(DEFAULT_ACCOUNT_CFIND);
        accountPreferences.setAccountPNCS(DEFAULT_ACCOUNT_PNCS);
        accountPreferences.setAccountBillingDefault(DEFAULT_ACCOUNT_BILLING_DEFAULT);
    }

    @Test
    @Transactional
    public void createAccountPreferences() throws Exception {
        int databaseSizeBeforeCreate = accountPreferencesRepository.findAll().size();

        // Create the AccountPreferences
        AccountPreferencesDTO accountPreferencesDTO = accountPreferencesMapper.accountPreferencesToAccountPreferencesDTO(accountPreferences);

        restAccountPreferencesMockMvc.perform(post("/api/accountPreferencess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountPreferencesDTO)))
                .andExpect(status().isCreated());

        // Validate the AccountPreferences in the database
        List<AccountPreferences> accountPreferencess = accountPreferencesRepository.findAll();
        assertThat(accountPreferencess).hasSize(databaseSizeBeforeCreate + 1);
        AccountPreferences testAccountPreferences = accountPreferencess.get(accountPreferencess.size() - 1);
        assertThat(testAccountPreferences.getAccountEStatement()).isEqualTo(DEFAULT_ACCOUNT_ESTATEMENT);
        assertThat(testAccountPreferences.getAccountNotification()).isEqualTo(DEFAULT_ACCOUNT_NOTIFICATION);
        assertThat(testAccountPreferences.getAccountLvlBilling()).isEqualTo(DEFAULT_ACCOUNT_LVL_BILLING);
        assertThat(testAccountPreferences.getAccountPrintStmt()).isEqualTo(DEFAULT_ACCOUNT_PRINT_STMT);
        assertThat(testAccountPreferences.getAccountRewardSuppInd()).isEqualTo(DEFAULT_ACCOUNT_REWARD_SUPP_IND);
        assertThat(testAccountPreferences.getAccountPackCnt()).isEqualTo(DEFAULT_ACCOUNT_PACK_CNT);
        assertThat(testAccountPreferences.getAccountCFInd()).isEqualTo(DEFAULT_ACCOUNT_CFIND);
        assertThat(testAccountPreferences.getAccountPNCS()).isEqualTo(DEFAULT_ACCOUNT_PNCS);
        assertThat(testAccountPreferences.getAccountBillingDefault()).isEqualTo(DEFAULT_ACCOUNT_BILLING_DEFAULT);
    }

    @Test
    @Transactional
    public void checkAccountEStatementIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountPreferencesRepository.findAll().size();
        // set the field null
        accountPreferences.setAccountEStatement(null);

        // Create the AccountPreferences, which fails.
        AccountPreferencesDTO accountPreferencesDTO = accountPreferencesMapper.accountPreferencesToAccountPreferencesDTO(accountPreferences);

        restAccountPreferencesMockMvc.perform(post("/api/accountPreferencess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountPreferencesDTO)))
                .andExpect(status().isBadRequest());

        List<AccountPreferences> accountPreferencess = accountPreferencesRepository.findAll();
        assertThat(accountPreferencess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountNotificationIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountPreferencesRepository.findAll().size();
        // set the field null
        accountPreferences.setAccountNotification(null);

        // Create the AccountPreferences, which fails.
        AccountPreferencesDTO accountPreferencesDTO = accountPreferencesMapper.accountPreferencesToAccountPreferencesDTO(accountPreferences);

        restAccountPreferencesMockMvc.perform(post("/api/accountPreferencess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountPreferencesDTO)))
                .andExpect(status().isBadRequest());

        List<AccountPreferences> accountPreferencess = accountPreferencesRepository.findAll();
        assertThat(accountPreferencess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountLvlBillingIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountPreferencesRepository.findAll().size();
        // set the field null
        accountPreferences.setAccountLvlBilling(null);

        // Create the AccountPreferences, which fails.
        AccountPreferencesDTO accountPreferencesDTO = accountPreferencesMapper.accountPreferencesToAccountPreferencesDTO(accountPreferences);

        restAccountPreferencesMockMvc.perform(post("/api/accountPreferencess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountPreferencesDTO)))
                .andExpect(status().isBadRequest());

        List<AccountPreferences> accountPreferencess = accountPreferencesRepository.findAll();
        assertThat(accountPreferencess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountPrintStmtIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountPreferencesRepository.findAll().size();
        // set the field null
        accountPreferences.setAccountPrintStmt(null);

        // Create the AccountPreferences, which fails.
        AccountPreferencesDTO accountPreferencesDTO = accountPreferencesMapper.accountPreferencesToAccountPreferencesDTO(accountPreferences);

        restAccountPreferencesMockMvc.perform(post("/api/accountPreferencess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountPreferencesDTO)))
                .andExpect(status().isBadRequest());

        List<AccountPreferences> accountPreferencess = accountPreferencesRepository.findAll();
        assertThat(accountPreferencess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountRewardSuppIndIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountPreferencesRepository.findAll().size();
        // set the field null
        accountPreferences.setAccountRewardSuppInd(null);

        // Create the AccountPreferences, which fails.
        AccountPreferencesDTO accountPreferencesDTO = accountPreferencesMapper.accountPreferencesToAccountPreferencesDTO(accountPreferences);

        restAccountPreferencesMockMvc.perform(post("/api/accountPreferencess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountPreferencesDTO)))
                .andExpect(status().isBadRequest());

        List<AccountPreferences> accountPreferencess = accountPreferencesRepository.findAll();
        assertThat(accountPreferencess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountPackCntIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountPreferencesRepository.findAll().size();
        // set the field null
        accountPreferences.setAccountPackCnt(null);

        // Create the AccountPreferences, which fails.
        AccountPreferencesDTO accountPreferencesDTO = accountPreferencesMapper.accountPreferencesToAccountPreferencesDTO(accountPreferences);

        restAccountPreferencesMockMvc.perform(post("/api/accountPreferencess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountPreferencesDTO)))
                .andExpect(status().isBadRequest());

        List<AccountPreferences> accountPreferencess = accountPreferencesRepository.findAll();
        assertThat(accountPreferencess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountCFIndIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountPreferencesRepository.findAll().size();
        // set the field null
        accountPreferences.setAccountCFInd(null);

        // Create the AccountPreferences, which fails.
        AccountPreferencesDTO accountPreferencesDTO = accountPreferencesMapper.accountPreferencesToAccountPreferencesDTO(accountPreferences);

        restAccountPreferencesMockMvc.perform(post("/api/accountPreferencess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountPreferencesDTO)))
                .andExpect(status().isBadRequest());

        List<AccountPreferences> accountPreferencess = accountPreferencesRepository.findAll();
        assertThat(accountPreferencess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountPNCSIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountPreferencesRepository.findAll().size();
        // set the field null
        accountPreferences.setAccountPNCS(null);

        // Create the AccountPreferences, which fails.
        AccountPreferencesDTO accountPreferencesDTO = accountPreferencesMapper.accountPreferencesToAccountPreferencesDTO(accountPreferences);

        restAccountPreferencesMockMvc.perform(post("/api/accountPreferencess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountPreferencesDTO)))
                .andExpect(status().isBadRequest());

        List<AccountPreferences> accountPreferencess = accountPreferencesRepository.findAll();
        assertThat(accountPreferencess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountBillingDefaultIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountPreferencesRepository.findAll().size();
        // set the field null
        accountPreferences.setAccountBillingDefault(null);

        // Create the AccountPreferences, which fails.
        AccountPreferencesDTO accountPreferencesDTO = accountPreferencesMapper.accountPreferencesToAccountPreferencesDTO(accountPreferences);

        restAccountPreferencesMockMvc.perform(post("/api/accountPreferencess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountPreferencesDTO)))
                .andExpect(status().isBadRequest());

        List<AccountPreferences> accountPreferencess = accountPreferencesRepository.findAll();
        assertThat(accountPreferencess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccountPreferencess() throws Exception {
        // Initialize the database
        accountPreferencesRepository.saveAndFlush(accountPreferences);

        // Get all the accountPreferencess
        restAccountPreferencesMockMvc.perform(get("/api/accountPreferencess"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(accountPreferences.getId().intValue())))
                .andExpect(jsonPath("$.[*].accountEStatement").value(hasItem(DEFAULT_ACCOUNT_ESTATEMENT.booleanValue())))
                .andExpect(jsonPath("$.[*].accountNotification").value(hasItem(DEFAULT_ACCOUNT_NOTIFICATION.booleanValue())))
                .andExpect(jsonPath("$.[*].accountLvlBilling").value(hasItem(DEFAULT_ACCOUNT_LVL_BILLING.booleanValue())))
                .andExpect(jsonPath("$.[*].accountPrintStmt").value(hasItem(DEFAULT_ACCOUNT_PRINT_STMT.booleanValue())))
                .andExpect(jsonPath("$.[*].accountRewardSuppInd").value(hasItem(DEFAULT_ACCOUNT_REWARD_SUPP_IND.booleanValue())))
                .andExpect(jsonPath("$.[*].accountPackCnt").value(hasItem(DEFAULT_ACCOUNT_PACK_CNT)))
                .andExpect(jsonPath("$.[*].accountCFInd").value(hasItem(DEFAULT_ACCOUNT_CFIND.booleanValue())))
                .andExpect(jsonPath("$.[*].accountPNCS").value(hasItem(DEFAULT_ACCOUNT_PNCS.booleanValue())))
                .andExpect(jsonPath("$.[*].accountBillingDefault").value(hasItem(DEFAULT_ACCOUNT_BILLING_DEFAULT.booleanValue())));
    }

    @Test
    @Transactional
    public void getAccountPreferences() throws Exception {
        // Initialize the database
        accountPreferencesRepository.saveAndFlush(accountPreferences);

        // Get the accountPreferences
        restAccountPreferencesMockMvc.perform(get("/api/accountPreferencess/{id}", accountPreferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(accountPreferences.getId().intValue()))
            .andExpect(jsonPath("$.accountEStatement").value(DEFAULT_ACCOUNT_ESTATEMENT.booleanValue()))
            .andExpect(jsonPath("$.accountNotification").value(DEFAULT_ACCOUNT_NOTIFICATION.booleanValue()))
            .andExpect(jsonPath("$.accountLvlBilling").value(DEFAULT_ACCOUNT_LVL_BILLING.booleanValue()))
            .andExpect(jsonPath("$.accountPrintStmt").value(DEFAULT_ACCOUNT_PRINT_STMT.booleanValue()))
            .andExpect(jsonPath("$.accountRewardSuppInd").value(DEFAULT_ACCOUNT_REWARD_SUPP_IND.booleanValue()))
            .andExpect(jsonPath("$.accountPackCnt").value(DEFAULT_ACCOUNT_PACK_CNT))
            .andExpect(jsonPath("$.accountCFInd").value(DEFAULT_ACCOUNT_CFIND.booleanValue()))
            .andExpect(jsonPath("$.accountPNCS").value(DEFAULT_ACCOUNT_PNCS.booleanValue()))
            .andExpect(jsonPath("$.accountBillingDefault").value(DEFAULT_ACCOUNT_BILLING_DEFAULT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountPreferences() throws Exception {
        // Get the accountPreferences
        restAccountPreferencesMockMvc.perform(get("/api/accountPreferencess/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountPreferences() throws Exception {
        // Initialize the database
        accountPreferencesRepository.saveAndFlush(accountPreferences);

		int databaseSizeBeforeUpdate = accountPreferencesRepository.findAll().size();

        // Update the accountPreferences
        accountPreferences.setAccountEStatement(UPDATED_ACCOUNT_ESTATEMENT);
        accountPreferences.setAccountNotification(UPDATED_ACCOUNT_NOTIFICATION);
        accountPreferences.setAccountLvlBilling(UPDATED_ACCOUNT_LVL_BILLING);
        accountPreferences.setAccountPrintStmt(UPDATED_ACCOUNT_PRINT_STMT);
        accountPreferences.setAccountRewardSuppInd(UPDATED_ACCOUNT_REWARD_SUPP_IND);
        accountPreferences.setAccountPackCnt(UPDATED_ACCOUNT_PACK_CNT);
        accountPreferences.setAccountCFInd(UPDATED_ACCOUNT_CFIND);
        accountPreferences.setAccountPNCS(UPDATED_ACCOUNT_PNCS);
        accountPreferences.setAccountBillingDefault(UPDATED_ACCOUNT_BILLING_DEFAULT);
        AccountPreferencesDTO accountPreferencesDTO = accountPreferencesMapper.accountPreferencesToAccountPreferencesDTO(accountPreferences);

        restAccountPreferencesMockMvc.perform(put("/api/accountPreferencess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountPreferencesDTO)))
                .andExpect(status().isOk());

        // Validate the AccountPreferences in the database
        List<AccountPreferences> accountPreferencess = accountPreferencesRepository.findAll();
        assertThat(accountPreferencess).hasSize(databaseSizeBeforeUpdate);
        AccountPreferences testAccountPreferences = accountPreferencess.get(accountPreferencess.size() - 1);
        assertThat(testAccountPreferences.getAccountEStatement()).isEqualTo(UPDATED_ACCOUNT_ESTATEMENT);
        assertThat(testAccountPreferences.getAccountNotification()).isEqualTo(UPDATED_ACCOUNT_NOTIFICATION);
        assertThat(testAccountPreferences.getAccountLvlBilling()).isEqualTo(UPDATED_ACCOUNT_LVL_BILLING);
        assertThat(testAccountPreferences.getAccountPrintStmt()).isEqualTo(UPDATED_ACCOUNT_PRINT_STMT);
        assertThat(testAccountPreferences.getAccountRewardSuppInd()).isEqualTo(UPDATED_ACCOUNT_REWARD_SUPP_IND);
        assertThat(testAccountPreferences.getAccountPackCnt()).isEqualTo(UPDATED_ACCOUNT_PACK_CNT);
        assertThat(testAccountPreferences.getAccountCFInd()).isEqualTo(UPDATED_ACCOUNT_CFIND);
        assertThat(testAccountPreferences.getAccountPNCS()).isEqualTo(UPDATED_ACCOUNT_PNCS);
        assertThat(testAccountPreferences.getAccountBillingDefault()).isEqualTo(UPDATED_ACCOUNT_BILLING_DEFAULT);
    }

    @Test
    @Transactional
    public void deleteAccountPreferences() throws Exception {
        // Initialize the database
        accountPreferencesRepository.saveAndFlush(accountPreferences);

		int databaseSizeBeforeDelete = accountPreferencesRepository.findAll().size();

        // Get the accountPreferences
        restAccountPreferencesMockMvc.perform(delete("/api/accountPreferencess/{id}", accountPreferences.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountPreferences> accountPreferencess = accountPreferencesRepository.findAll();
        assertThat(accountPreferencess).hasSize(databaseSizeBeforeDelete - 1);
    }
}
