package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.Accounts;
import com.barclays.bbilling.repository.AccountsRepository;
import com.barclays.bbilling.repository.search.AccountsSearchRepository;
import com.barclays.bbilling.web.rest.dto.AccountsDTO;
import com.barclays.bbilling.web.rest.mapper.AccountsMapper;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barclays.bbilling.domain.enumeration.AccountType;
import com.barclays.bbilling.domain.enumeration.AccountStatus;
import com.barclays.bbilling.domain.enumeration.AccountLastBalType;
import com.barclays.bbilling.domain.enumeration.AccountTier;

/**
 * Test class for the AccountsResource REST controller.
 *
 * @see AccountsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AccountsResourceTest {

    private static final String DEFAULT_ACCOUNT_NUMBER = "A";
    private static final String UPDATED_ACCOUNT_NUMBER = "B";


private static final AccountType DEFAULT_ACCOUNT_TYPE = AccountType.CA;
    private static final AccountType UPDATED_ACCOUNT_TYPE = AccountType.SA;

    private static final LocalDate DEFAULT_ACCOUNT_OPENED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACCOUNT_OPENED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ACCOUNT_CLOSED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACCOUNT_CLOSED_DATE = LocalDate.now(ZoneId.systemDefault());


private static final AccountStatus DEFAULT_ACCOUNT_STATUS = AccountStatus.ACTIVE;
    private static final AccountStatus UPDATED_ACCOUNT_STATUS = AccountStatus.INACTIVE;

    private static final BigDecimal DEFAULT_ACCOUNT_CRTURN_OVER = new BigDecimal(50);
    private static final BigDecimal UPDATED_ACCOUNT_CRTURN_OVER = new BigDecimal(51);

    private static final BigDecimal DEFAULT_ACCOUNT_DRTURN_OVER = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNT_DRTURN_OVER = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ACCOUNT_AVAIL_BAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNT_AVAIL_BAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ACCOUNT_LEDGER_BAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNT_LEDGER_BAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ACCOUNT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNT_BALANCE = new BigDecimal(2);


private static final AccountLastBalType DEFAULT_ACCOUNT_LAST_BAL_TYPE = AccountLastBalType.CR;
    private static final AccountLastBalType UPDATED_ACCOUNT_LAST_BAL_TYPE = AccountLastBalType.DR;


private static final AccountTier DEFAULT_ACCOUNT_TIER = AccountTier.TIER0;
    private static final AccountTier UPDATED_ACCOUNT_TIER = AccountTier.TIER1;

    private static final BigDecimal DEFAULT_ACCOUNT_CHARGING_BAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNT_CHARGING_BAL = new BigDecimal(2);

    @Inject
    private AccountsRepository accountsRepository;

    @Inject
    private AccountsMapper accountsMapper;

    @Inject
    private AccountsSearchRepository accountsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAccountsMockMvc;

    private Accounts accounts;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccountsResource accountsResource = new AccountsResource();
        ReflectionTestUtils.setField(accountsResource, "accountsRepository", accountsRepository);
        ReflectionTestUtils.setField(accountsResource, "accountsMapper", accountsMapper);
        ReflectionTestUtils.setField(accountsResource, "accountsSearchRepository", accountsSearchRepository);
        this.restAccountsMockMvc = MockMvcBuilders.standaloneSetup(accountsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        accounts = new Accounts();
        accounts.setAccountNumber(DEFAULT_ACCOUNT_NUMBER);
        accounts.setAccountType(DEFAULT_ACCOUNT_TYPE);
        accounts.setAccountOpenedDate(DEFAULT_ACCOUNT_OPENED_DATE);
        accounts.setAccountClosedDate(DEFAULT_ACCOUNT_CLOSED_DATE);
        accounts.setAccountStatus(DEFAULT_ACCOUNT_STATUS);
        accounts.setAccountCRTurnOver(DEFAULT_ACCOUNT_CRTURN_OVER);
        accounts.setAccountDRTurnOver(DEFAULT_ACCOUNT_DRTURN_OVER);
        accounts.setAccountAvailBal(DEFAULT_ACCOUNT_AVAIL_BAL);
        accounts.setAccountLedgerBal(DEFAULT_ACCOUNT_LEDGER_BAL);
        accounts.setAccountBalance(DEFAULT_ACCOUNT_BALANCE);
        accounts.setAccountLastBalType(DEFAULT_ACCOUNT_LAST_BAL_TYPE);
        accounts.setAccountTier(DEFAULT_ACCOUNT_TIER);
        accounts.setAccountChargingBal(DEFAULT_ACCOUNT_CHARGING_BAL);
    }

    @Test
    @Transactional
    public void createAccounts() throws Exception {
        int databaseSizeBeforeCreate = accountsRepository.findAll().size();

        // Create the Accounts
        AccountsDTO accountsDTO = accountsMapper.accountsToAccountsDTO(accounts);

        restAccountsMockMvc.perform(post("/api/accountss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
                .andExpect(status().isCreated());

        // Validate the Accounts in the database
        List<Accounts> accountss = accountsRepository.findAll();
        assertThat(accountss).hasSize(databaseSizeBeforeCreate + 1);
        Accounts testAccounts = accountss.get(accountss.size() - 1);
        assertThat(testAccounts.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testAccounts.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testAccounts.getAccountOpenedDate()).isEqualTo(DEFAULT_ACCOUNT_OPENED_DATE);
        assertThat(testAccounts.getAccountClosedDate()).isEqualTo(DEFAULT_ACCOUNT_CLOSED_DATE);
        assertThat(testAccounts.getAccountStatus()).isEqualTo(DEFAULT_ACCOUNT_STATUS);
        assertThat(testAccounts.getAccountCRTurnOver()).isEqualTo(DEFAULT_ACCOUNT_CRTURN_OVER);
        assertThat(testAccounts.getAccountDRTurnOver()).isEqualTo(DEFAULT_ACCOUNT_DRTURN_OVER);
        assertThat(testAccounts.getAccountAvailBal()).isEqualTo(DEFAULT_ACCOUNT_AVAIL_BAL);
        assertThat(testAccounts.getAccountLedgerBal()).isEqualTo(DEFAULT_ACCOUNT_LEDGER_BAL);
        assertThat(testAccounts.getAccountBalance()).isEqualTo(DEFAULT_ACCOUNT_BALANCE);
        assertThat(testAccounts.getAccountLastBalType()).isEqualTo(DEFAULT_ACCOUNT_LAST_BAL_TYPE);
        assertThat(testAccounts.getAccountTier()).isEqualTo(DEFAULT_ACCOUNT_TIER);
        assertThat(testAccounts.getAccountChargingBal()).isEqualTo(DEFAULT_ACCOUNT_CHARGING_BAL);
    }

    @Test
    @Transactional
    public void checkAccountNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountsRepository.findAll().size();
        // set the field null
        accounts.setAccountNumber(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.accountsToAccountsDTO(accounts);

        restAccountsMockMvc.perform(post("/api/accountss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
                .andExpect(status().isBadRequest());

        List<Accounts> accountss = accountsRepository.findAll();
        assertThat(accountss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountsRepository.findAll().size();
        // set the field null
        accounts.setAccountType(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.accountsToAccountsDTO(accounts);

        restAccountsMockMvc.perform(post("/api/accountss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
                .andExpect(status().isBadRequest());

        List<Accounts> accountss = accountsRepository.findAll();
        assertThat(accountss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountOpenedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountsRepository.findAll().size();
        // set the field null
        accounts.setAccountOpenedDate(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.accountsToAccountsDTO(accounts);

        restAccountsMockMvc.perform(post("/api/accountss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
                .andExpect(status().isBadRequest());

        List<Accounts> accountss = accountsRepository.findAll();
        assertThat(accountss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountClosedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountsRepository.findAll().size();
        // set the field null
        accounts.setAccountClosedDate(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.accountsToAccountsDTO(accounts);

        restAccountsMockMvc.perform(post("/api/accountss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
                .andExpect(status().isBadRequest());

        List<Accounts> accountss = accountsRepository.findAll();
        assertThat(accountss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountsRepository.findAll().size();
        // set the field null
        accounts.setAccountStatus(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.accountsToAccountsDTO(accounts);

        restAccountsMockMvc.perform(post("/api/accountss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
                .andExpect(status().isBadRequest());

        List<Accounts> accountss = accountsRepository.findAll();
        assertThat(accountss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountCRTurnOverIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountsRepository.findAll().size();
        // set the field null
        accounts.setAccountCRTurnOver(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.accountsToAccountsDTO(accounts);

        restAccountsMockMvc.perform(post("/api/accountss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
                .andExpect(status().isBadRequest());

        List<Accounts> accountss = accountsRepository.findAll();
        assertThat(accountss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountAvailBalIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountsRepository.findAll().size();
        // set the field null
        accounts.setAccountAvailBal(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.accountsToAccountsDTO(accounts);

        restAccountsMockMvc.perform(post("/api/accountss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
                .andExpect(status().isBadRequest());

        List<Accounts> accountss = accountsRepository.findAll();
        assertThat(accountss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountLastBalTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountsRepository.findAll().size();
        // set the field null
        accounts.setAccountLastBalType(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.accountsToAccountsDTO(accounts);

        restAccountsMockMvc.perform(post("/api/accountss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
                .andExpect(status().isBadRequest());

        List<Accounts> accountss = accountsRepository.findAll();
        assertThat(accountss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountTierIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountsRepository.findAll().size();
        // set the field null
        accounts.setAccountTier(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.accountsToAccountsDTO(accounts);

        restAccountsMockMvc.perform(post("/api/accountss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
                .andExpect(status().isBadRequest());

        List<Accounts> accountss = accountsRepository.findAll();
        assertThat(accountss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccountss() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        // Get all the accountss
        restAccountsMockMvc.perform(get("/api/accountss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(accounts.getId().intValue())))
                .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].accountOpenedDate").value(hasItem(DEFAULT_ACCOUNT_OPENED_DATE.toString())))
                .andExpect(jsonPath("$.[*].accountClosedDate").value(hasItem(DEFAULT_ACCOUNT_CLOSED_DATE.toString())))
                .andExpect(jsonPath("$.[*].accountStatus").value(hasItem(DEFAULT_ACCOUNT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].accountCRTurnOver").value(hasItem(DEFAULT_ACCOUNT_CRTURN_OVER.intValue())))
                .andExpect(jsonPath("$.[*].accountDRTurnOver").value(hasItem(DEFAULT_ACCOUNT_DRTURN_OVER.intValue())))
                .andExpect(jsonPath("$.[*].accountAvailBal").value(hasItem(DEFAULT_ACCOUNT_AVAIL_BAL.intValue())))
                .andExpect(jsonPath("$.[*].accountLedgerBal").value(hasItem(DEFAULT_ACCOUNT_LEDGER_BAL.intValue())))
                .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(DEFAULT_ACCOUNT_BALANCE.intValue())))
                .andExpect(jsonPath("$.[*].accountLastBalType").value(hasItem(DEFAULT_ACCOUNT_LAST_BAL_TYPE.toString())))
                .andExpect(jsonPath("$.[*].accountTier").value(hasItem(DEFAULT_ACCOUNT_TIER.toString())))
                .andExpect(jsonPath("$.[*].accountChargingBal").value(hasItem(DEFAULT_ACCOUNT_CHARGING_BAL.intValue())));
    }

    @Test
    @Transactional
    public void getAccounts() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        // Get the accounts
        restAccountsMockMvc.perform(get("/api/accountss/{id}", accounts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(accounts.getId().intValue()))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER.toString()))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.accountOpenedDate").value(DEFAULT_ACCOUNT_OPENED_DATE.toString()))
            .andExpect(jsonPath("$.accountClosedDate").value(DEFAULT_ACCOUNT_CLOSED_DATE.toString()))
            .andExpect(jsonPath("$.accountStatus").value(DEFAULT_ACCOUNT_STATUS.toString()))
            .andExpect(jsonPath("$.accountCRTurnOver").value(DEFAULT_ACCOUNT_CRTURN_OVER.intValue()))
            .andExpect(jsonPath("$.accountDRTurnOver").value(DEFAULT_ACCOUNT_DRTURN_OVER.intValue()))
            .andExpect(jsonPath("$.accountAvailBal").value(DEFAULT_ACCOUNT_AVAIL_BAL.intValue()))
            .andExpect(jsonPath("$.accountLedgerBal").value(DEFAULT_ACCOUNT_LEDGER_BAL.intValue()))
            .andExpect(jsonPath("$.accountBalance").value(DEFAULT_ACCOUNT_BALANCE.intValue()))
            .andExpect(jsonPath("$.accountLastBalType").value(DEFAULT_ACCOUNT_LAST_BAL_TYPE.toString()))
            .andExpect(jsonPath("$.accountTier").value(DEFAULT_ACCOUNT_TIER.toString()))
            .andExpect(jsonPath("$.accountChargingBal").value(DEFAULT_ACCOUNT_CHARGING_BAL.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAccounts() throws Exception {
        // Get the accounts
        restAccountsMockMvc.perform(get("/api/accountss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

//    @Test
//    @Transactional
//    public void updateAccounts() throws Exception {
//        // Initialize the database
//        accountsRepository.saveAndFlush(accounts);
//
//		int databaseSizeBeforeUpdate = accountsRepository.findAll().size();
//
//        // Update the accounts
//        accounts.setAccountNumber(UPDATED_ACCOUNT_NUMBER);
//        accounts.setAccountType(UPDATED_ACCOUNT_TYPE);
//        accounts.setAccountOpenedDate(UPDATED_ACCOUNT_OPENED_DATE);
//        accounts.setAccountClosedDate(UPDATED_ACCOUNT_CLOSED_DATE);
//        accounts.setAccountStatus(UPDATED_ACCOUNT_STATUS);
//        accounts.setAccountCRTurnOver(UPDATED_ACCOUNT_CRTURN_OVER);
//        accounts.setAccountDRTurnOver(UPDATED_ACCOUNT_DRTURN_OVER);
//        accounts.setAccountAvailBal(UPDATED_ACCOUNT_AVAIL_BAL);
//        accounts.setAccountLedgerBal(UPDATED_ACCOUNT_LEDGER_BAL);
//        accounts.setAccountBalance(UPDATED_ACCOUNT_BALANCE);
//        accounts.setAccountLastBalType(UPDATED_ACCOUNT_LAST_BAL_TYPE);
//        accounts.setAccountTier(UPDATED_ACCOUNT_TIER);
//        accounts.setAccountChargingBal(UPDATED_ACCOUNT_CHARGING_BAL);
//        AccountsDTO accountsDTO = accountsMapper.accountsToAccountsDTO(accounts);
//
//        restAccountsMockMvc.perform(put("/api/accountss")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
//                .andExpect(status().isOk());
//
//        // Validate the Accounts in the database
//        List<Accounts> accountss = accountsRepository.findAll();
//        assertThat(accountss).hasSize(databaseSizeBeforeUpdate);
//        Accounts testAccounts = accountss.get(accountss.size() - 1);
//        assertThat(testAccounts.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
//        assertThat(testAccounts.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
//        assertThat(testAccounts.getAccountOpenedDate()).isEqualTo(UPDATED_ACCOUNT_OPENED_DATE);
//        assertThat(testAccounts.getAccountClosedDate()).isEqualTo(UPDATED_ACCOUNT_CLOSED_DATE);
//        assertThat(testAccounts.getAccountStatus()).isEqualTo(UPDATED_ACCOUNT_STATUS);
//        assertThat(testAccounts.getAccountCRTurnOver()).isEqualTo(UPDATED_ACCOUNT_CRTURN_OVER);
//        assertThat(testAccounts.getAccountDRTurnOver()).isEqualTo(UPDATED_ACCOUNT_DRTURN_OVER);
//        assertThat(testAccounts.getAccountAvailBal()).isEqualTo(UPDATED_ACCOUNT_AVAIL_BAL);
//        assertThat(testAccounts.getAccountLedgerBal()).isEqualTo(UPDATED_ACCOUNT_LEDGER_BAL);
//        assertThat(testAccounts.getAccountBalance()).isEqualTo(UPDATED_ACCOUNT_BALANCE);
//        assertThat(testAccounts.getAccountLastBalType()).isEqualTo(UPDATED_ACCOUNT_LAST_BAL_TYPE);
//        assertThat(testAccounts.getAccountTier()).isEqualTo(UPDATED_ACCOUNT_TIER);
//        assertThat(testAccounts.getAccountChargingBal()).isEqualTo(UPDATED_ACCOUNT_CHARGING_BAL);
//    }

    @Test
    @Transactional
    public void deleteAccounts() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

		int databaseSizeBeforeDelete = accountsRepository.findAll().size();

        // Get the accounts
        restAccountsMockMvc.perform(delete("/api/accountss/{id}", accounts.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Accounts> accountss = accountsRepository.findAll();
        assertThat(accountss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
