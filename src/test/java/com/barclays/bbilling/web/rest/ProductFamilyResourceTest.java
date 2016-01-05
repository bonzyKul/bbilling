package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.ProductFamily;
import com.barclays.bbilling.repository.ProductFamilyRepository;
import com.barclays.bbilling.repository.search.ProductFamilySearchRepository;
import com.barclays.bbilling.web.rest.dto.ProductFamilyDTO;
import com.barclays.bbilling.web.rest.mapper.ProductFamilyMapper;

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
 * Test class for the ProductFamilyResource REST controller.
 *
 * @see ProductFamilyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProductFamilyResourceTest {

    private static final String DEFAULT_PRODUCT_FAMILY_CODE = "A";
    private static final String UPDATED_PRODUCT_FAMILY_CODE = "B";
    private static final String DEFAULT_PRODUCT_FAMILY_DESC = "A";
    private static final String UPDATED_PRODUCT_FAMILY_DESC = "B";

    @Inject
    private ProductFamilyRepository productFamilyRepository;

    @Inject
    private ProductFamilyMapper productFamilyMapper;

    @Inject
    private ProductFamilySearchRepository productFamilySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProductFamilyMockMvc;

    private ProductFamily productFamily;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductFamilyResource productFamilyResource = new ProductFamilyResource();
        ReflectionTestUtils.setField(productFamilyResource, "productFamilyRepository", productFamilyRepository);
        ReflectionTestUtils.setField(productFamilyResource, "productFamilyMapper", productFamilyMapper);
        ReflectionTestUtils.setField(productFamilyResource, "productFamilySearchRepository", productFamilySearchRepository);
        this.restProductFamilyMockMvc = MockMvcBuilders.standaloneSetup(productFamilyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        productFamily = new ProductFamily();
        productFamily.setProductFamilyCode(DEFAULT_PRODUCT_FAMILY_CODE);
        productFamily.setProductFamilyDesc(DEFAULT_PRODUCT_FAMILY_DESC);
    }

    @Test
    @Transactional
    public void createProductFamily() throws Exception {
        int databaseSizeBeforeCreate = productFamilyRepository.findAll().size();

        // Create the ProductFamily
        ProductFamilyDTO productFamilyDTO = productFamilyMapper.productFamilyToProductFamilyDTO(productFamily);

        restProductFamilyMockMvc.perform(post("/api/productFamilys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productFamilyDTO)))
                .andExpect(status().isCreated());

        // Validate the ProductFamily in the database
        List<ProductFamily> productFamilys = productFamilyRepository.findAll();
        assertThat(productFamilys).hasSize(databaseSizeBeforeCreate + 1);
        ProductFamily testProductFamily = productFamilys.get(productFamilys.size() - 1);
        assertThat(testProductFamily.getProductFamilyCode()).isEqualTo(DEFAULT_PRODUCT_FAMILY_CODE);
        assertThat(testProductFamily.getProductFamilyDesc()).isEqualTo(DEFAULT_PRODUCT_FAMILY_DESC);
    }

    @Test
    @Transactional
    public void checkProductFamilyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productFamilyRepository.findAll().size();
        // set the field null
        productFamily.setProductFamilyCode(null);

        // Create the ProductFamily, which fails.
        ProductFamilyDTO productFamilyDTO = productFamilyMapper.productFamilyToProductFamilyDTO(productFamily);

        restProductFamilyMockMvc.perform(post("/api/productFamilys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productFamilyDTO)))
                .andExpect(status().isBadRequest());

        List<ProductFamily> productFamilys = productFamilyRepository.findAll();
        assertThat(productFamilys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductFamilyDescIsRequired() throws Exception {
        int databaseSizeBeforeTest = productFamilyRepository.findAll().size();
        // set the field null
        productFamily.setProductFamilyDesc(null);

        // Create the ProductFamily, which fails.
        ProductFamilyDTO productFamilyDTO = productFamilyMapper.productFamilyToProductFamilyDTO(productFamily);

        restProductFamilyMockMvc.perform(post("/api/productFamilys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productFamilyDTO)))
                .andExpect(status().isBadRequest());

        List<ProductFamily> productFamilys = productFamilyRepository.findAll();
        assertThat(productFamilys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductFamilys() throws Exception {
        // Initialize the database
        productFamilyRepository.saveAndFlush(productFamily);

        // Get all the productFamilys
        restProductFamilyMockMvc.perform(get("/api/productFamilys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(productFamily.getId().intValue())))
                .andExpect(jsonPath("$.[*].productFamilyCode").value(hasItem(DEFAULT_PRODUCT_FAMILY_CODE.toString())))
                .andExpect(jsonPath("$.[*].productFamilyDesc").value(hasItem(DEFAULT_PRODUCT_FAMILY_DESC.toString())));
    }

    @Test
    @Transactional
    public void getProductFamily() throws Exception {
        // Initialize the database
        productFamilyRepository.saveAndFlush(productFamily);

        // Get the productFamily
        restProductFamilyMockMvc.perform(get("/api/productFamilys/{id}", productFamily.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(productFamily.getId().intValue()))
            .andExpect(jsonPath("$.productFamilyCode").value(DEFAULT_PRODUCT_FAMILY_CODE.toString()))
            .andExpect(jsonPath("$.productFamilyDesc").value(DEFAULT_PRODUCT_FAMILY_DESC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductFamily() throws Exception {
        // Get the productFamily
        restProductFamilyMockMvc.perform(get("/api/productFamilys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductFamily() throws Exception {
        // Initialize the database
        productFamilyRepository.saveAndFlush(productFamily);

		int databaseSizeBeforeUpdate = productFamilyRepository.findAll().size();

        // Update the productFamily
        productFamily.setProductFamilyCode(UPDATED_PRODUCT_FAMILY_CODE);
        productFamily.setProductFamilyDesc(UPDATED_PRODUCT_FAMILY_DESC);
        ProductFamilyDTO productFamilyDTO = productFamilyMapper.productFamilyToProductFamilyDTO(productFamily);

        restProductFamilyMockMvc.perform(put("/api/productFamilys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productFamilyDTO)))
                .andExpect(status().isOk());

        // Validate the ProductFamily in the database
        List<ProductFamily> productFamilys = productFamilyRepository.findAll();
        assertThat(productFamilys).hasSize(databaseSizeBeforeUpdate);
        ProductFamily testProductFamily = productFamilys.get(productFamilys.size() - 1);
        assertThat(testProductFamily.getProductFamilyCode()).isEqualTo(UPDATED_PRODUCT_FAMILY_CODE);
        assertThat(testProductFamily.getProductFamilyDesc()).isEqualTo(UPDATED_PRODUCT_FAMILY_DESC);
    }

    @Test
    @Transactional
    public void deleteProductFamily() throws Exception {
        // Initialize the database
        productFamilyRepository.saveAndFlush(productFamily);

		int databaseSizeBeforeDelete = productFamilyRepository.findAll().size();

        // Get the productFamily
        restProductFamilyMockMvc.perform(delete("/api/productFamilys/{id}", productFamily.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductFamily> productFamilys = productFamilyRepository.findAll();
        assertThat(productFamilys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
