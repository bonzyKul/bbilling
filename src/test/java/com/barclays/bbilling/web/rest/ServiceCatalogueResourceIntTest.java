package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.ServiceCatalogue;
import com.barclays.bbilling.repository.ServiceCatalogueRepository;
import com.barclays.bbilling.repository.search.ServiceCatalogueSearchRepository;
import com.barclays.bbilling.web.rest.dto.ServiceCatalogueDTO;
import com.barclays.bbilling.web.rest.mapper.ServiceCatalogueMapper;

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
 * Test class for the ServiceCatalogueResource REST controller.
 *
 * @see ServiceCatalogueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ServiceCatalogueResourceIntTest {

    private static final String DEFAULT_SERVICE_CODE = "AAAAA";
    private static final String UPDATED_SERVICE_CODE = "BBBBB";
    private static final String DEFAULT_SERVICE_DESCRIPTION = "AAAAA";
    private static final String UPDATED_SERVICE_DESCRIPTION = "BBBBB";

    private static final LocalDate DEFAULT_SERVICE_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SERVICE_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_SERVICE_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SERVICE_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ServiceCatalogueRepository serviceCatalogueRepository;

    @Inject
    private ServiceCatalogueMapper serviceCatalogueMapper;

    @Inject
    private ServiceCatalogueSearchRepository serviceCatalogueSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restServiceCatalogueMockMvc;

    private ServiceCatalogue serviceCatalogue;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiceCatalogueResource serviceCatalogueResource = new ServiceCatalogueResource();
        ReflectionTestUtils.setField(serviceCatalogueResource, "serviceCatalogueRepository", serviceCatalogueRepository);
        ReflectionTestUtils.setField(serviceCatalogueResource, "serviceCatalogueMapper", serviceCatalogueMapper);
        ReflectionTestUtils.setField(serviceCatalogueResource, "serviceCatalogueSearchRepository", serviceCatalogueSearchRepository);
        this.restServiceCatalogueMockMvc = MockMvcBuilders.standaloneSetup(serviceCatalogueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        serviceCatalogue = new ServiceCatalogue();
        serviceCatalogue.setServiceCode(DEFAULT_SERVICE_CODE);
        serviceCatalogue.setServiceDescription(DEFAULT_SERVICE_DESCRIPTION);
        serviceCatalogue.setServiceStartDate(DEFAULT_SERVICE_START_DATE);
        serviceCatalogue.setServiceEndDate(DEFAULT_SERVICE_END_DATE);
    }

    @Test
    @Transactional
    public void createServiceCatalogue() throws Exception {
        int databaseSizeBeforeCreate = serviceCatalogueRepository.findAll().size();

        // Create the ServiceCatalogue
        ServiceCatalogueDTO serviceCatalogueDTO = serviceCatalogueMapper.serviceCatalogueToServiceCatalogueDTO(serviceCatalogue);

        restServiceCatalogueMockMvc.perform(post("/api/serviceCatalogues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceCatalogueDTO)))
                .andExpect(status().isCreated());

        // Validate the ServiceCatalogue in the database
        List<ServiceCatalogue> serviceCatalogues = serviceCatalogueRepository.findAll();
        assertThat(serviceCatalogues).hasSize(databaseSizeBeforeCreate + 1);
        ServiceCatalogue testServiceCatalogue = serviceCatalogues.get(serviceCatalogues.size() - 1);
        assertThat(testServiceCatalogue.getServiceCode()).isEqualTo(DEFAULT_SERVICE_CODE);
        assertThat(testServiceCatalogue.getServiceDescription()).isEqualTo(DEFAULT_SERVICE_DESCRIPTION);
        assertThat(testServiceCatalogue.getServiceStartDate()).isEqualTo(DEFAULT_SERVICE_START_DATE);
        assertThat(testServiceCatalogue.getServiceEndDate()).isEqualTo(DEFAULT_SERVICE_END_DATE);
    }

    @Test
    @Transactional
    public void checkServiceCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceCatalogueRepository.findAll().size();
        // set the field null
        serviceCatalogue.setServiceCode(null);

        // Create the ServiceCatalogue, which fails.
        ServiceCatalogueDTO serviceCatalogueDTO = serviceCatalogueMapper.serviceCatalogueToServiceCatalogueDTO(serviceCatalogue);

        restServiceCatalogueMockMvc.perform(post("/api/serviceCatalogues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceCatalogueDTO)))
                .andExpect(status().isBadRequest());

        List<ServiceCatalogue> serviceCatalogues = serviceCatalogueRepository.findAll();
        assertThat(serviceCatalogues).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServiceDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceCatalogueRepository.findAll().size();
        // set the field null
        serviceCatalogue.setServiceDescription(null);

        // Create the ServiceCatalogue, which fails.
        ServiceCatalogueDTO serviceCatalogueDTO = serviceCatalogueMapper.serviceCatalogueToServiceCatalogueDTO(serviceCatalogue);

        restServiceCatalogueMockMvc.perform(post("/api/serviceCatalogues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceCatalogueDTO)))
                .andExpect(status().isBadRequest());

        List<ServiceCatalogue> serviceCatalogues = serviceCatalogueRepository.findAll();
        assertThat(serviceCatalogues).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServiceStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceCatalogueRepository.findAll().size();
        // set the field null
        serviceCatalogue.setServiceStartDate(null);

        // Create the ServiceCatalogue, which fails.
        ServiceCatalogueDTO serviceCatalogueDTO = serviceCatalogueMapper.serviceCatalogueToServiceCatalogueDTO(serviceCatalogue);

        restServiceCatalogueMockMvc.perform(post("/api/serviceCatalogues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceCatalogueDTO)))
                .andExpect(status().isBadRequest());

        List<ServiceCatalogue> serviceCatalogues = serviceCatalogueRepository.findAll();
        assertThat(serviceCatalogues).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServiceEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceCatalogueRepository.findAll().size();
        // set the field null
        serviceCatalogue.setServiceEndDate(null);

        // Create the ServiceCatalogue, which fails.
        ServiceCatalogueDTO serviceCatalogueDTO = serviceCatalogueMapper.serviceCatalogueToServiceCatalogueDTO(serviceCatalogue);

        restServiceCatalogueMockMvc.perform(post("/api/serviceCatalogues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceCatalogueDTO)))
                .andExpect(status().isBadRequest());

        List<ServiceCatalogue> serviceCatalogues = serviceCatalogueRepository.findAll();
        assertThat(serviceCatalogues).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServiceCatalogues() throws Exception {
        // Initialize the database
        serviceCatalogueRepository.saveAndFlush(serviceCatalogue);

        // Get all the serviceCatalogues
        restServiceCatalogueMockMvc.perform(get("/api/serviceCatalogues"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(serviceCatalogue.getId().intValue())))
                .andExpect(jsonPath("$.[*].serviceCode").value(hasItem(DEFAULT_SERVICE_CODE.toString())))
                .andExpect(jsonPath("$.[*].serviceDescription").value(hasItem(DEFAULT_SERVICE_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].serviceStartDate").value(hasItem(DEFAULT_SERVICE_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].serviceEndDate").value(hasItem(DEFAULT_SERVICE_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getServiceCatalogue() throws Exception {
        // Initialize the database
        serviceCatalogueRepository.saveAndFlush(serviceCatalogue);

        // Get the serviceCatalogue
        restServiceCatalogueMockMvc.perform(get("/api/serviceCatalogues/{id}", serviceCatalogue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(serviceCatalogue.getId().intValue()))
            .andExpect(jsonPath("$.serviceCode").value(DEFAULT_SERVICE_CODE.toString()))
            .andExpect(jsonPath("$.serviceDescription").value(DEFAULT_SERVICE_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.serviceStartDate").value(DEFAULT_SERVICE_START_DATE.toString()))
            .andExpect(jsonPath("$.serviceEndDate").value(DEFAULT_SERVICE_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceCatalogue() throws Exception {
        // Get the serviceCatalogue
        restServiceCatalogueMockMvc.perform(get("/api/serviceCatalogues/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceCatalogue() throws Exception {
        // Initialize the database
        serviceCatalogueRepository.saveAndFlush(serviceCatalogue);

		int databaseSizeBeforeUpdate = serviceCatalogueRepository.findAll().size();

        // Update the serviceCatalogue
        serviceCatalogue.setServiceCode(UPDATED_SERVICE_CODE);
        serviceCatalogue.setServiceDescription(UPDATED_SERVICE_DESCRIPTION);
        serviceCatalogue.setServiceStartDate(UPDATED_SERVICE_START_DATE);
        serviceCatalogue.setServiceEndDate(UPDATED_SERVICE_END_DATE);
        ServiceCatalogueDTO serviceCatalogueDTO = serviceCatalogueMapper.serviceCatalogueToServiceCatalogueDTO(serviceCatalogue);

        restServiceCatalogueMockMvc.perform(put("/api/serviceCatalogues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceCatalogueDTO)))
                .andExpect(status().isOk());

        // Validate the ServiceCatalogue in the database
        List<ServiceCatalogue> serviceCatalogues = serviceCatalogueRepository.findAll();
        assertThat(serviceCatalogues).hasSize(databaseSizeBeforeUpdate);
        ServiceCatalogue testServiceCatalogue = serviceCatalogues.get(serviceCatalogues.size() - 1);
        assertThat(testServiceCatalogue.getServiceCode()).isEqualTo(UPDATED_SERVICE_CODE);
        assertThat(testServiceCatalogue.getServiceDescription()).isEqualTo(UPDATED_SERVICE_DESCRIPTION);
        assertThat(testServiceCatalogue.getServiceStartDate()).isEqualTo(UPDATED_SERVICE_START_DATE);
        assertThat(testServiceCatalogue.getServiceEndDate()).isEqualTo(UPDATED_SERVICE_END_DATE);
    }

    @Test
    @Transactional
    public void deleteServiceCatalogue() throws Exception {
        // Initialize the database
        serviceCatalogueRepository.saveAndFlush(serviceCatalogue);

		int databaseSizeBeforeDelete = serviceCatalogueRepository.findAll().size();

        // Get the serviceCatalogue
        restServiceCatalogueMockMvc.perform(delete("/api/serviceCatalogues/{id}", serviceCatalogue.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ServiceCatalogue> serviceCatalogues = serviceCatalogueRepository.findAll();
        assertThat(serviceCatalogues).hasSize(databaseSizeBeforeDelete - 1);
    }
}
