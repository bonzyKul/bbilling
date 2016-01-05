package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.Holiday;
import com.barclays.bbilling.repository.HolidayRepository;
import com.barclays.bbilling.repository.search.HolidaySearchRepository;
import com.barclays.bbilling.web.rest.dto.HolidayDTO;
import com.barclays.bbilling.web.rest.mapper.HolidayMapper;

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
 * Test class for the HolidayResource REST controller.
 *
 * @see HolidayResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HolidayResourceTest {


    private static final LocalDate DEFAULT_HOLIDAY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_HOLIDAY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_HOLIDAY_DESC = "A";
    private static final String UPDATED_HOLIDAY_DESC = "B";

    @Inject
    private HolidayRepository holidayRepository;

    @Inject
    private HolidayMapper holidayMapper;

    @Inject
    private HolidaySearchRepository holidaySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHolidayMockMvc;

    private Holiday holiday;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HolidayResource holidayResource = new HolidayResource();
        ReflectionTestUtils.setField(holidayResource, "holidayRepository", holidayRepository);
        ReflectionTestUtils.setField(holidayResource, "holidayMapper", holidayMapper);
        ReflectionTestUtils.setField(holidayResource, "holidaySearchRepository", holidaySearchRepository);
        this.restHolidayMockMvc = MockMvcBuilders.standaloneSetup(holidayResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        holiday = new Holiday();
        holiday.setHolidayDate(DEFAULT_HOLIDAY_DATE);
        holiday.setHolidayDesc(DEFAULT_HOLIDAY_DESC);
    }

    @Test
    @Transactional
    public void createHoliday() throws Exception {
        int databaseSizeBeforeCreate = holidayRepository.findAll().size();

        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.holidayToHolidayDTO(holiday);

        restHolidayMockMvc.perform(post("/api/holidays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(holidayDTO)))
                .andExpect(status().isCreated());

        // Validate the Holiday in the database
        List<Holiday> holidays = holidayRepository.findAll();
        assertThat(holidays).hasSize(databaseSizeBeforeCreate + 1);
        Holiday testHoliday = holidays.get(holidays.size() - 1);
        assertThat(testHoliday.getHolidayDate()).isEqualTo(DEFAULT_HOLIDAY_DATE);
        assertThat(testHoliday.getHolidayDesc()).isEqualTo(DEFAULT_HOLIDAY_DESC);
    }

    @Test
    @Transactional
    public void checkHolidayDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = holidayRepository.findAll().size();
        // set the field null
        holiday.setHolidayDate(null);

        // Create the Holiday, which fails.
        HolidayDTO holidayDTO = holidayMapper.holidayToHolidayDTO(holiday);

        restHolidayMockMvc.perform(post("/api/holidays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(holidayDTO)))
                .andExpect(status().isBadRequest());

        List<Holiday> holidays = holidayRepository.findAll();
        assertThat(holidays).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHolidayDescIsRequired() throws Exception {
        int databaseSizeBeforeTest = holidayRepository.findAll().size();
        // set the field null
        holiday.setHolidayDesc(null);

        // Create the Holiday, which fails.
        HolidayDTO holidayDTO = holidayMapper.holidayToHolidayDTO(holiday);

        restHolidayMockMvc.perform(post("/api/holidays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(holidayDTO)))
                .andExpect(status().isBadRequest());

        List<Holiday> holidays = holidayRepository.findAll();
        assertThat(holidays).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHolidays() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidays
        restHolidayMockMvc.perform(get("/api/holidays"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(holiday.getId().intValue())))
                .andExpect(jsonPath("$.[*].holidayDate").value(hasItem(DEFAULT_HOLIDAY_DATE.toString())))
                .andExpect(jsonPath("$.[*].holidayDesc").value(hasItem(DEFAULT_HOLIDAY_DESC.toString())));
    }

    @Test
    @Transactional
    public void getHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get the holiday
        restHolidayMockMvc.perform(get("/api/holidays/{id}", holiday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(holiday.getId().intValue()))
            .andExpect(jsonPath("$.holidayDate").value(DEFAULT_HOLIDAY_DATE.toString()))
            .andExpect(jsonPath("$.holidayDesc").value(DEFAULT_HOLIDAY_DESC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHoliday() throws Exception {
        // Get the holiday
        restHolidayMockMvc.perform(get("/api/holidays/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

		int databaseSizeBeforeUpdate = holidayRepository.findAll().size();

        // Update the holiday
        holiday.setHolidayDate(UPDATED_HOLIDAY_DATE);
        holiday.setHolidayDesc(UPDATED_HOLIDAY_DESC);
        HolidayDTO holidayDTO = holidayMapper.holidayToHolidayDTO(holiday);

        restHolidayMockMvc.perform(put("/api/holidays")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(holidayDTO)))
                .andExpect(status().isOk());

        // Validate the Holiday in the database
        List<Holiday> holidays = holidayRepository.findAll();
        assertThat(holidays).hasSize(databaseSizeBeforeUpdate);
        Holiday testHoliday = holidays.get(holidays.size() - 1);
        assertThat(testHoliday.getHolidayDate()).isEqualTo(UPDATED_HOLIDAY_DATE);
        assertThat(testHoliday.getHolidayDesc()).isEqualTo(UPDATED_HOLIDAY_DESC);
    }

    @Test
    @Transactional
    public void deleteHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

		int databaseSizeBeforeDelete = holidayRepository.findAll().size();

        // Get the holiday
        restHolidayMockMvc.perform(delete("/api/holidays/{id}", holiday.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Holiday> holidays = holidayRepository.findAll();
        assertThat(holidays).hasSize(databaseSizeBeforeDelete - 1);
    }
}
