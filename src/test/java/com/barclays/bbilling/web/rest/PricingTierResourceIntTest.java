package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.PricingTier;
import com.barclays.bbilling.repository.PricingTierRepository;
import com.barclays.bbilling.repository.search.PricingTierSearchRepository;
import com.barclays.bbilling.web.rest.dto.PricingTierDTO;
import com.barclays.bbilling.web.rest.mapper.PricingTierMapper;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PricingTierResource REST controller.
 *
 * @see PricingTierResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PricingTierResourceIntTest {


    private static final Long DEFAULT_PRICING_TIER_FROM = 1L;
    private static final Long UPDATED_PRICING_TIER_FROM = 2L;

    private static final Long DEFAULT_PRICING_TIER_TO = 1L;
    private static final Long UPDATED_PRICING_TIER_TO = 2L;

    private static final BigDecimal DEFAULT_PRICING_TIER_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICING_TIER_VALUE = new BigDecimal(2);

    @Inject
    private PricingTierRepository pricingTierRepository;

    @Inject
    private PricingTierMapper pricingTierMapper;

    @Inject
    private PricingTierSearchRepository pricingTierSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPricingTierMockMvc;

    private PricingTier pricingTier;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PricingTierResource pricingTierResource = new PricingTierResource();
        ReflectionTestUtils.setField(pricingTierResource, "pricingTierRepository", pricingTierRepository);
        ReflectionTestUtils.setField(pricingTierResource, "pricingTierMapper", pricingTierMapper);
        ReflectionTestUtils.setField(pricingTierResource, "pricingTierSearchRepository", pricingTierSearchRepository);
        this.restPricingTierMockMvc = MockMvcBuilders.standaloneSetup(pricingTierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pricingTier = new PricingTier();
        pricingTier.setPricingTierFrom(DEFAULT_PRICING_TIER_FROM);
        pricingTier.setPricingTierTo(DEFAULT_PRICING_TIER_TO);
        pricingTier.setPricingTierValue(DEFAULT_PRICING_TIER_VALUE);
    }

    @Test
    @Transactional
    public void createPricingTier() throws Exception {
        int databaseSizeBeforeCreate = pricingTierRepository.findAll().size();

        // Create the PricingTier
        PricingTierDTO pricingTierDTO = pricingTierMapper.pricingTierToPricingTierDTO(pricingTier);

        restPricingTierMockMvc.perform(post("/api/pricingTiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingTierDTO)))
                .andExpect(status().isCreated());

        // Validate the PricingTier in the database
        List<PricingTier> pricingTiers = pricingTierRepository.findAll();
        assertThat(pricingTiers).hasSize(databaseSizeBeforeCreate + 1);
        PricingTier testPricingTier = pricingTiers.get(pricingTiers.size() - 1);
        assertThat(testPricingTier.getPricingTierFrom()).isEqualTo(DEFAULT_PRICING_TIER_FROM);
        assertThat(testPricingTier.getPricingTierTo()).isEqualTo(DEFAULT_PRICING_TIER_TO);
        assertThat(testPricingTier.getPricingTierValue()).isEqualTo(DEFAULT_PRICING_TIER_VALUE);
    }

    @Test
    @Transactional
    public void checkPricingTierFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingTierRepository.findAll().size();
        // set the field null
        pricingTier.setPricingTierFrom(null);

        // Create the PricingTier, which fails.
        PricingTierDTO pricingTierDTO = pricingTierMapper.pricingTierToPricingTierDTO(pricingTier);

        restPricingTierMockMvc.perform(post("/api/pricingTiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingTierDTO)))
                .andExpect(status().isBadRequest());

        List<PricingTier> pricingTiers = pricingTierRepository.findAll();
        assertThat(pricingTiers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPricingTierToIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingTierRepository.findAll().size();
        // set the field null
        pricingTier.setPricingTierTo(null);

        // Create the PricingTier, which fails.
        PricingTierDTO pricingTierDTO = pricingTierMapper.pricingTierToPricingTierDTO(pricingTier);

        restPricingTierMockMvc.perform(post("/api/pricingTiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingTierDTO)))
                .andExpect(status().isBadRequest());

        List<PricingTier> pricingTiers = pricingTierRepository.findAll();
        assertThat(pricingTiers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPricingTierValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingTierRepository.findAll().size();
        // set the field null
        pricingTier.setPricingTierValue(null);

        // Create the PricingTier, which fails.
        PricingTierDTO pricingTierDTO = pricingTierMapper.pricingTierToPricingTierDTO(pricingTier);

        restPricingTierMockMvc.perform(post("/api/pricingTiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingTierDTO)))
                .andExpect(status().isBadRequest());

        List<PricingTier> pricingTiers = pricingTierRepository.findAll();
        assertThat(pricingTiers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPricingTiers() throws Exception {
        // Initialize the database
        pricingTierRepository.saveAndFlush(pricingTier);

        // Get all the pricingTiers
        restPricingTierMockMvc.perform(get("/api/pricingTiers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pricingTier.getId().intValue())))
                .andExpect(jsonPath("$.[*].pricingTierFrom").value(hasItem(DEFAULT_PRICING_TIER_FROM.intValue())))
                .andExpect(jsonPath("$.[*].pricingTierTo").value(hasItem(DEFAULT_PRICING_TIER_TO.intValue())))
                .andExpect(jsonPath("$.[*].pricingTierValue").value(hasItem(DEFAULT_PRICING_TIER_VALUE.intValue())));
    }

    @Test
    @Transactional
    public void getPricingTier() throws Exception {
        // Initialize the database
        pricingTierRepository.saveAndFlush(pricingTier);

        // Get the pricingTier
        restPricingTierMockMvc.perform(get("/api/pricingTiers/{id}", pricingTier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pricingTier.getId().intValue()))
            .andExpect(jsonPath("$.pricingTierFrom").value(DEFAULT_PRICING_TIER_FROM.intValue()))
            .andExpect(jsonPath("$.pricingTierTo").value(DEFAULT_PRICING_TIER_TO.intValue()))
            .andExpect(jsonPath("$.pricingTierValue").value(DEFAULT_PRICING_TIER_VALUE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPricingTier() throws Exception {
        // Get the pricingTier
        restPricingTierMockMvc.perform(get("/api/pricingTiers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePricingTier() throws Exception {
        // Initialize the database
        pricingTierRepository.saveAndFlush(pricingTier);

		int databaseSizeBeforeUpdate = pricingTierRepository.findAll().size();

        // Update the pricingTier
        pricingTier.setPricingTierFrom(UPDATED_PRICING_TIER_FROM);
        pricingTier.setPricingTierTo(UPDATED_PRICING_TIER_TO);
        pricingTier.setPricingTierValue(UPDATED_PRICING_TIER_VALUE);
        PricingTierDTO pricingTierDTO = pricingTierMapper.pricingTierToPricingTierDTO(pricingTier);

        restPricingTierMockMvc.perform(put("/api/pricingTiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pricingTierDTO)))
                .andExpect(status().isOk());

        // Validate the PricingTier in the database
        List<PricingTier> pricingTiers = pricingTierRepository.findAll();
        assertThat(pricingTiers).hasSize(databaseSizeBeforeUpdate);
        PricingTier testPricingTier = pricingTiers.get(pricingTiers.size() - 1);
        assertThat(testPricingTier.getPricingTierFrom()).isEqualTo(UPDATED_PRICING_TIER_FROM);
        assertThat(testPricingTier.getPricingTierTo()).isEqualTo(UPDATED_PRICING_TIER_TO);
        assertThat(testPricingTier.getPricingTierValue()).isEqualTo(UPDATED_PRICING_TIER_VALUE);
    }

    @Test
    @Transactional
    public void deletePricingTier() throws Exception {
        // Initialize the database
        pricingTierRepository.saveAndFlush(pricingTier);

		int databaseSizeBeforeDelete = pricingTierRepository.findAll().size();

        // Get the pricingTier
        restPricingTierMockMvc.perform(delete("/api/pricingTiers/{id}", pricingTier.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PricingTier> pricingTiers = pricingTierRepository.findAll();
        assertThat(pricingTiers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
