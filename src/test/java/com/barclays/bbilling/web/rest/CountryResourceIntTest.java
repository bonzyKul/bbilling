package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.Country;
import com.barclays.bbilling.repository.CountryRepository;
import com.barclays.bbilling.repository.search.CountrySearchRepository;
import com.barclays.bbilling.web.rest.dto.CountryDTO;
import com.barclays.bbilling.web.rest.mapper.CountryMapper;

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
 * Test class for the CountryResource REST controller.
 *
 * @see CountryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CountryResourceIntTest {

    private static final String DEFAULT_COUNTRY_CODE = "A";
    private static final String UPDATED_COUNTRY_CODE = "B";
    private static final String DEFAULT_COUNTRY_NAME = "A";
    private static final String UPDATED_COUNTRY_NAME = "B";

    @Inject
    private CountryRepository countryRepository;

    @Inject
    private CountryMapper countryMapper;

    @Inject
    private CountrySearchRepository countrySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCountryMockMvc;

    private Country country;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CountryResource countryResource = new CountryResource();
        ReflectionTestUtils.setField(countryResource, "countryRepository", countryRepository);
        ReflectionTestUtils.setField(countryResource, "countryMapper", countryMapper);
        ReflectionTestUtils.setField(countryResource, "countrySearchRepository", countrySearchRepository);
        this.restCountryMockMvc = MockMvcBuilders.standaloneSetup(countryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        country = new Country();
        country.setCountryCode(DEFAULT_COUNTRY_CODE);
        country.setCountryName(DEFAULT_COUNTRY_NAME);
    }

    @Test
    @Transactional
    public void createCountry() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().size();

        // Create the Country
        CountryDTO countryDTO = countryMapper.countryToCountryDTO(country);

        restCountryMockMvc.perform(post("/api/countrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
                .andExpect(status().isCreated());

        // Validate the Country in the database
        List<Country> countrys = countryRepository.findAll();
        assertThat(countrys).hasSize(databaseSizeBeforeCreate + 1);
        Country testCountry = countrys.get(countrys.size() - 1);
        assertThat(testCountry.getCountryCode()).isEqualTo(DEFAULT_COUNTRY_CODE);
        assertThat(testCountry.getCountryName()).isEqualTo(DEFAULT_COUNTRY_NAME);
    }

    @Test
    @Transactional
    public void checkCountryCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setCountryCode(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.countryToCountryDTO(country);

        restCountryMockMvc.perform(post("/api/countrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
                .andExpect(status().isBadRequest());

        List<Country> countrys = countryRepository.findAll();
        assertThat(countrys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setCountryName(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.countryToCountryDTO(country);

        restCountryMockMvc.perform(post("/api/countrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
                .andExpect(status().isBadRequest());

        List<Country> countrys = countryRepository.findAll();
        assertThat(countrys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCountrys() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countrys
        restCountryMockMvc.perform(get("/api/countrys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
                .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE.toString())))
                .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get the country
        restCountryMockMvc.perform(get("/api/countrys/{id}", country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(country.getId().intValue()))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE.toString()))
            .andExpect(jsonPath("$.countryName").value(DEFAULT_COUNTRY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCountry() throws Exception {
        // Get the country
        restCountryMockMvc.perform(get("/api/countrys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

		int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country
        country.setCountryCode(UPDATED_COUNTRY_CODE);
        country.setCountryName(UPDATED_COUNTRY_NAME);
        CountryDTO countryDTO = countryMapper.countryToCountryDTO(country);

        restCountryMockMvc.perform(put("/api/countrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
                .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countrys = countryRepository.findAll();
        assertThat(countrys).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countrys.get(countrys.size() - 1);
        assertThat(testCountry.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);
        assertThat(testCountry.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    public void deleteCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

		int databaseSizeBeforeDelete = countryRepository.findAll().size();

        // Get the country
        restCountryMockMvc.perform(delete("/api/countrys/{id}", country.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Country> countrys = countryRepository.findAll();
        assertThat(countrys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
