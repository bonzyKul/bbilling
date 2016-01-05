package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.Pricing;
import com.barclays.bbilling.repository.PricingRepository;
import com.barclays.bbilling.repository.search.PricingSearchRepository;
import com.barclays.bbilling.web.rest.dto.PricingDTO;
import com.barclays.bbilling.web.rest.mapper.PricingMapper;

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

import com.barclays.bbilling.domain.enumeration.PricingType;
import com.barclays.bbilling.domain.enumeration.PricingRateType;
import com.barclays.bbilling.domain.enumeration.PricingUnitType;
import com.barclays.bbilling.domain.enumeration.PricingAmountType;

/**
 * Test class for the PricingResource REST controller.
 *
 * @see PricingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PricingResourceIntTest {



private static final PricingType DEFAULT_PRICING_TYPE = PricingType.FLAT_SIMPLE;
    private static final PricingType UPDATED_PRICING_TYPE = PricingType.UNIT_SIMPLE;

    private static final BigDecimal DEFAULT_PRICING_CHARGE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICING_CHARGE_AMOUNT = new BigDecimal(2);


private static final PricingRateType DEFAULT_PRICING_RATE_TYPE = PricingRateType.FLAT;
    private static final PricingRateType UPDATED_PRICING_RATE_TYPE = PricingRateType.PERCENTAGE;

    private static final Integer DEFAULT_PRICING_UNIT = 1;
    private static final Integer UPDATED_PRICING_UNIT = 2;

    private static final LocalDate DEFAULT_PRICING_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PRICING_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PRICING_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PRICING_END_DATE = LocalDate.now(ZoneId.systemDefault());


private static final PricingUnitType DEFAULT_PRICING_UNIT_TYPE = PricingUnitType.VALUE;
    private static final PricingUnitType UPDATED_PRICING_UNIT_TYPE = PricingUnitType.COUNT;


private static final PricingAmountType DEFAULT_PRICING_AMOUNT_TYPE = PricingAmountType.CR;
    private static final PricingAmountType UPDATED_PRICING_AMOUNT_TYPE = PricingAmountType.DR;

    private static final Boolean DEFAULT_PRICING_FOR_STAFF = false;
    private static final Boolean UPDATED_PRICING_FOR_STAFF = true;

    private static final Boolean DEFAULT_PRICING_TAX_INDICATOR = false;
    private static final Boolean UPDATED_PRICING_TAX_INDICATOR = true;

    @Inject
    private PricingRepository pricingRepository;

    @Inject
    private PricingMapper pricingMapper;

    @Inject
    private PricingSearchRepository pricingSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPricingMockMvc;

    private Pricing pricing;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PricingResource pricingResource = new PricingResource();
        ReflectionTestUtils.setField(pricingResource, "pricingRepository", pricingRepository);
        ReflectionTestUtils.setField(pricingResource, "pricingMapper", pricingMapper);
        ReflectionTestUtils.setField(pricingResource, "pricingSearchRepository", pricingSearchRepository);
        this.restPricingMockMvc = MockMvcBuilders.standaloneSetup(pricingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pricing = new Pricing();
        pricing.setPricingType(DEFAULT_PRICING_TYPE);
        pricing.setPricingChargeAmount(DEFAULT_PRICING_CHARGE_AMOUNT);
        pricing.setPricingRateType(DEFAULT_PRICING_RATE_TYPE);
        pricing.setPricingUnit(DEFAULT_PRICING_UNIT);
        pricing.setPricingStartDate(DEFAULT_PRICING_START_DATE);
        pricing.setPricingEndDate(DEFAULT_PRICING_END_DATE);
        pricing.setPricingUnitType(DEFAULT_PRICING_UNIT_TYPE);
        pricing.setPricingAmountType(DEFAULT_PRICING_AMOUNT_TYPE);
        pricing.setPricingForStaff(DEFAULT_PRICING_FOR_STAFF);
        pricing.setPricingTaxIndicator(DEFAULT_PRICING_TAX_INDICATOR);
    }

    @Test
    @Transactional
    public void createPricing() throws Exception {
        int databaseSizeBeforeCreate = pricingRepository.findAll().size();

        // Create the Pricing
        PricingDTO pricingDTO = pricingMapper.pricingToPricingDTO(pricing);

        restPricingMockMvc.perform(post("/api/pricings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingDTO)))
                .andExpect(status().isCreated());

        // Validate the Pricing in the database
        List<Pricing> pricings = pricingRepository.findAll();
        assertThat(pricings).hasSize(databaseSizeBeforeCreate + 1);
        Pricing testPricing = pricings.get(pricings.size() - 1);
        assertThat(testPricing.getPricingType()).isEqualTo(DEFAULT_PRICING_TYPE);
        assertThat(testPricing.getPricingChargeAmount()).isEqualTo(DEFAULT_PRICING_CHARGE_AMOUNT);
        assertThat(testPricing.getPricingRateType()).isEqualTo(DEFAULT_PRICING_RATE_TYPE);
        assertThat(testPricing.getPricingUnit()).isEqualTo(DEFAULT_PRICING_UNIT);
        assertThat(testPricing.getPricingStartDate()).isEqualTo(DEFAULT_PRICING_START_DATE);
        assertThat(testPricing.getPricingEndDate()).isEqualTo(DEFAULT_PRICING_END_DATE);
        assertThat(testPricing.getPricingUnitType()).isEqualTo(DEFAULT_PRICING_UNIT_TYPE);
        assertThat(testPricing.getPricingAmountType()).isEqualTo(DEFAULT_PRICING_AMOUNT_TYPE);
        assertThat(testPricing.getPricingForStaff()).isEqualTo(DEFAULT_PRICING_FOR_STAFF);
        assertThat(testPricing.getPricingTaxIndicator()).isEqualTo(DEFAULT_PRICING_TAX_INDICATOR);
    }

    @Test
    @Transactional
    public void checkPricingTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingRepository.findAll().size();
        // set the field null
        pricing.setPricingType(null);

        // Create the Pricing, which fails.
        PricingDTO pricingDTO = pricingMapper.pricingToPricingDTO(pricing);

        restPricingMockMvc.perform(post("/api/pricings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingDTO)))
                .andExpect(status().isBadRequest());

        List<Pricing> pricings = pricingRepository.findAll();
        assertThat(pricings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPricingChargeAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingRepository.findAll().size();
        // set the field null
        pricing.setPricingChargeAmount(null);

        // Create the Pricing, which fails.
        PricingDTO pricingDTO = pricingMapper.pricingToPricingDTO(pricing);

        restPricingMockMvc.perform(post("/api/pricings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingDTO)))
                .andExpect(status().isBadRequest());

        List<Pricing> pricings = pricingRepository.findAll();
        assertThat(pricings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPricingRateTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingRepository.findAll().size();
        // set the field null
        pricing.setPricingRateType(null);

        // Create the Pricing, which fails.
        PricingDTO pricingDTO = pricingMapper.pricingToPricingDTO(pricing);

        restPricingMockMvc.perform(post("/api/pricings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingDTO)))
                .andExpect(status().isBadRequest());

        List<Pricing> pricings = pricingRepository.findAll();
        assertThat(pricings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPricingStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingRepository.findAll().size();
        // set the field null
        pricing.setPricingStartDate(null);

        // Create the Pricing, which fails.
        PricingDTO pricingDTO = pricingMapper.pricingToPricingDTO(pricing);

        restPricingMockMvc.perform(post("/api/pricings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingDTO)))
                .andExpect(status().isBadRequest());

        List<Pricing> pricings = pricingRepository.findAll();
        assertThat(pricings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPricingEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingRepository.findAll().size();
        // set the field null
        pricing.setPricingEndDate(null);

        // Create the Pricing, which fails.
        PricingDTO pricingDTO = pricingMapper.pricingToPricingDTO(pricing);

        restPricingMockMvc.perform(post("/api/pricings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingDTO)))
                .andExpect(status().isBadRequest());

        List<Pricing> pricings = pricingRepository.findAll();
        assertThat(pricings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPricingUnitTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingRepository.findAll().size();
        // set the field null
        pricing.setPricingUnitType(null);

        // Create the Pricing, which fails.
        PricingDTO pricingDTO = pricingMapper.pricingToPricingDTO(pricing);

        restPricingMockMvc.perform(post("/api/pricings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingDTO)))
                .andExpect(status().isBadRequest());

        List<Pricing> pricings = pricingRepository.findAll();
        assertThat(pricings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPricingAmountTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingRepository.findAll().size();
        // set the field null
        pricing.setPricingAmountType(null);

        // Create the Pricing, which fails.
        PricingDTO pricingDTO = pricingMapper.pricingToPricingDTO(pricing);

        restPricingMockMvc.perform(post("/api/pricings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingDTO)))
                .andExpect(status().isBadRequest());

        List<Pricing> pricings = pricingRepository.findAll();
        assertThat(pricings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPricingForStaffIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingRepository.findAll().size();
        // set the field null
        pricing.setPricingForStaff(null);

        // Create the Pricing, which fails.
        PricingDTO pricingDTO = pricingMapper.pricingToPricingDTO(pricing);

        restPricingMockMvc.perform(post("/api/pricings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingDTO)))
                .andExpect(status().isBadRequest());

        List<Pricing> pricings = pricingRepository.findAll();
        assertThat(pricings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPricingTaxIndicatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingRepository.findAll().size();
        // set the field null
        pricing.setPricingTaxIndicator(null);

        // Create the Pricing, which fails.
        PricingDTO pricingDTO = pricingMapper.pricingToPricingDTO(pricing);

        restPricingMockMvc.perform(post("/api/pricings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingDTO)))
                .andExpect(status().isBadRequest());

        List<Pricing> pricings = pricingRepository.findAll();
        assertThat(pricings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPricings() throws Exception {
        // Initialize the database
        pricingRepository.saveAndFlush(pricing);

        // Get all the pricings
        restPricingMockMvc.perform(get("/api/pricings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pricing.getId().intValue())))
                .andExpect(jsonPath("$.[*].pricingType").value(hasItem(DEFAULT_PRICING_TYPE.toString())))
                .andExpect(jsonPath("$.[*].pricingChargeAmount").value(hasItem(DEFAULT_PRICING_CHARGE_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].pricingRateType").value(hasItem(DEFAULT_PRICING_RATE_TYPE.toString())))
                .andExpect(jsonPath("$.[*].pricingUnit").value(hasItem(DEFAULT_PRICING_UNIT)))
                .andExpect(jsonPath("$.[*].pricingStartDate").value(hasItem(DEFAULT_PRICING_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].pricingEndDate").value(hasItem(DEFAULT_PRICING_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].pricingUnitType").value(hasItem(DEFAULT_PRICING_UNIT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].pricingAmountType").value(hasItem(DEFAULT_PRICING_AMOUNT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].pricingForStaff").value(hasItem(DEFAULT_PRICING_FOR_STAFF.booleanValue())))
                .andExpect(jsonPath("$.[*].pricingTaxIndicator").value(hasItem(DEFAULT_PRICING_TAX_INDICATOR.booleanValue())));
    }

    @Test
    @Transactional
    public void getPricing() throws Exception {
        // Initialize the database
        pricingRepository.saveAndFlush(pricing);

        // Get the pricing
        restPricingMockMvc.perform(get("/api/pricings/{id}", pricing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pricing.getId().intValue()))
            .andExpect(jsonPath("$.pricingType").value(DEFAULT_PRICING_TYPE.toString()))
            .andExpect(jsonPath("$.pricingChargeAmount").value(DEFAULT_PRICING_CHARGE_AMOUNT.intValue()))
            .andExpect(jsonPath("$.pricingRateType").value(DEFAULT_PRICING_RATE_TYPE.toString()))
            .andExpect(jsonPath("$.pricingUnit").value(DEFAULT_PRICING_UNIT))
            .andExpect(jsonPath("$.pricingStartDate").value(DEFAULT_PRICING_START_DATE.toString()))
            .andExpect(jsonPath("$.pricingEndDate").value(DEFAULT_PRICING_END_DATE.toString()))
            .andExpect(jsonPath("$.pricingUnitType").value(DEFAULT_PRICING_UNIT_TYPE.toString()))
            .andExpect(jsonPath("$.pricingAmountType").value(DEFAULT_PRICING_AMOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.pricingForStaff").value(DEFAULT_PRICING_FOR_STAFF.booleanValue()))
            .andExpect(jsonPath("$.pricingTaxIndicator").value(DEFAULT_PRICING_TAX_INDICATOR.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPricing() throws Exception {
        // Get the pricing
        restPricingMockMvc.perform(get("/api/pricings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePricing() throws Exception {
        // Initialize the database
        pricingRepository.saveAndFlush(pricing);

		int databaseSizeBeforeUpdate = pricingRepository.findAll().size();

        // Update the pricing
        pricing.setPricingType(UPDATED_PRICING_TYPE);
        pricing.setPricingChargeAmount(UPDATED_PRICING_CHARGE_AMOUNT);
        pricing.setPricingRateType(UPDATED_PRICING_RATE_TYPE);
        pricing.setPricingUnit(UPDATED_PRICING_UNIT);
        pricing.setPricingStartDate(UPDATED_PRICING_START_DATE);
        pricing.setPricingEndDate(UPDATED_PRICING_END_DATE);
        pricing.setPricingUnitType(UPDATED_PRICING_UNIT_TYPE);
        pricing.setPricingAmountType(UPDATED_PRICING_AMOUNT_TYPE);
        pricing.setPricingForStaff(UPDATED_PRICING_FOR_STAFF);
        pricing.setPricingTaxIndicator(UPDATED_PRICING_TAX_INDICATOR);
        PricingDTO pricingDTO = pricingMapper.pricingToPricingDTO(pricing);

        restPricingMockMvc.perform(put("/api/pricings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingDTO)))
                .andExpect(status().isOk());

        // Validate the Pricing in the database
        List<Pricing> pricings = pricingRepository.findAll();
        assertThat(pricings).hasSize(databaseSizeBeforeUpdate);
        Pricing testPricing = pricings.get(pricings.size() - 1);
        assertThat(testPricing.getPricingType()).isEqualTo(UPDATED_PRICING_TYPE);
        assertThat(testPricing.getPricingChargeAmount()).isEqualTo(UPDATED_PRICING_CHARGE_AMOUNT);
        assertThat(testPricing.getPricingRateType()).isEqualTo(UPDATED_PRICING_RATE_TYPE);
        assertThat(testPricing.getPricingUnit()).isEqualTo(UPDATED_PRICING_UNIT);
        assertThat(testPricing.getPricingStartDate()).isEqualTo(UPDATED_PRICING_START_DATE);
        assertThat(testPricing.getPricingEndDate()).isEqualTo(UPDATED_PRICING_END_DATE);
        assertThat(testPricing.getPricingUnitType()).isEqualTo(UPDATED_PRICING_UNIT_TYPE);
        assertThat(testPricing.getPricingAmountType()).isEqualTo(UPDATED_PRICING_AMOUNT_TYPE);
        assertThat(testPricing.getPricingForStaff()).isEqualTo(UPDATED_PRICING_FOR_STAFF);
        assertThat(testPricing.getPricingTaxIndicator()).isEqualTo(UPDATED_PRICING_TAX_INDICATOR);
    }

    @Test
    @Transactional
    public void deletePricing() throws Exception {
        // Initialize the database
        pricingRepository.saveAndFlush(pricing);

		int databaseSizeBeforeDelete = pricingRepository.findAll().size();

        // Get the pricing
        restPricingMockMvc.perform(delete("/api/pricings/{id}", pricing.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pricing> pricings = pricingRepository.findAll();
        assertThat(pricings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
