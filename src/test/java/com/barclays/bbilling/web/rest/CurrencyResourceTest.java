package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.Currency;
import com.barclays.bbilling.repository.CurrencyRepository;
import com.barclays.bbilling.repository.search.CurrencySearchRepository;
import com.barclays.bbilling.web.rest.dto.CurrencyDTO;
import com.barclays.bbilling.web.rest.mapper.CurrencyMapper;

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
 * Test class for the CurrencyResource REST controller.
 *
 * @see CurrencyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CurrencyResourceTest {

    private static final String DEFAULT_CURRENCY_CODE = "A";
    private static final String UPDATED_CURRENCY_CODE = "B";
    private static final String DEFAULT_CURRENCY_DESCRIPTION = "A";
    private static final String UPDATED_CURRENCY_DESCRIPTION = "B";

    @Inject
    private CurrencyRepository currencyRepository;

    @Inject
    private CurrencyMapper currencyMapper;

    @Inject
    private CurrencySearchRepository currencySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCurrencyMockMvc;

    private Currency currency;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CurrencyResource currencyResource = new CurrencyResource();
        ReflectionTestUtils.setField(currencyResource, "currencyRepository", currencyRepository);
        ReflectionTestUtils.setField(currencyResource, "currencyMapper", currencyMapper);
        ReflectionTestUtils.setField(currencyResource, "currencySearchRepository", currencySearchRepository);
        this.restCurrencyMockMvc = MockMvcBuilders.standaloneSetup(currencyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        currency = new Currency();
        currency.setCurrencyCode(DEFAULT_CURRENCY_CODE);
        currency.setCurrencyDescription(DEFAULT_CURRENCY_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCurrency() throws Exception {
        int databaseSizeBeforeCreate = currencyRepository.findAll().size();

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.currencyToCurrencyDTO(currency);

        restCurrencyMockMvc.perform(post("/api/currencys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
                .andExpect(status().isCreated());

        // Validate the Currency in the database
        List<Currency> currencys = currencyRepository.findAll();
        assertThat(currencys).hasSize(databaseSizeBeforeCreate + 1);
        Currency testCurrency = currencys.get(currencys.size() - 1);
        assertThat(testCurrency.getCurrencyCode()).isEqualTo(DEFAULT_CURRENCY_CODE);
        assertThat(testCurrency.getCurrencyDescription()).isEqualTo(DEFAULT_CURRENCY_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkCurrencyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setCurrencyCode(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.currencyToCurrencyDTO(currency);

        restCurrencyMockMvc.perform(post("/api/currencys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
                .andExpect(status().isBadRequest());

        List<Currency> currencys = currencyRepository.findAll();
        assertThat(currencys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCurrencys() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencys
        restCurrencyMockMvc.perform(get("/api/currencys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
                .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE.toString())))
                .andExpect(jsonPath("$.[*].currencyDescription").value(hasItem(DEFAULT_CURRENCY_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get the currency
        restCurrencyMockMvc.perform(get("/api/currencys/{id}", currency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(currency.getId().intValue()))
            .andExpect(jsonPath("$.currencyCode").value(DEFAULT_CURRENCY_CODE.toString()))
            .andExpect(jsonPath("$.currencyDescription").value(DEFAULT_CURRENCY_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCurrency() throws Exception {
        // Get the currency
        restCurrencyMockMvc.perform(get("/api/currencys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

		int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency
        currency.setCurrencyCode(UPDATED_CURRENCY_CODE);
        currency.setCurrencyDescription(UPDATED_CURRENCY_DESCRIPTION);
        CurrencyDTO currencyDTO = currencyMapper.currencyToCurrencyDTO(currency);

        restCurrencyMockMvc.perform(put("/api/currencys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
                .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencys = currencyRepository.findAll();
        assertThat(currencys).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencys.get(currencys.size() - 1);
        assertThat(testCurrency.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
        assertThat(testCurrency.getCurrencyDescription()).isEqualTo(UPDATED_CURRENCY_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

		int databaseSizeBeforeDelete = currencyRepository.findAll().size();

        // Get the currency
        restCurrencyMockMvc.perform(delete("/api/currencys/{id}", currency.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Currency> currencys = currencyRepository.findAll();
        assertThat(currencys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
