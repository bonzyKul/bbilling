package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.Products;
import com.barclays.bbilling.repository.ProductsRepository;
import com.barclays.bbilling.repository.search.ProductsSearchRepository;
import com.barclays.bbilling.web.rest.dto.ProductsDTO;
import com.barclays.bbilling.web.rest.mapper.ProductsMapper;

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

import com.barclays.bbilling.domain.enumeration.ProductStatus;

/**
 * Test class for the ProductsResource REST controller.
 *
 * @see ProductsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProductsResourceTest {

    private static final String DEFAULT_PRODUCT_CODE = "A";
    private static final String UPDATED_PRODUCT_CODE = "B";
    private static final String DEFAULT_PRODUCT_SHORT_NAME = "A";
    private static final String UPDATED_PRODUCT_SHORT_NAME = "B";
    private static final String DEFAULT_PRODUCT_NAME = "A";
    private static final String UPDATED_PRODUCT_NAME = "B";

    private static final LocalDate DEFAULT_PRODUCT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PRODUCT_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PRODUCT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PRODUCT_END_DATE = LocalDate.now(ZoneId.systemDefault());


private static final ProductStatus DEFAULT_PRODUCT_STATUS = ProductStatus.Active;
    private static final ProductStatus UPDATED_PRODUCT_STATUS = ProductStatus.Inactive;

    @Inject
    private ProductsRepository productsRepository;

    @Inject
    private ProductsMapper productsMapper;

    @Inject
    private ProductsSearchRepository productsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProductsMockMvc;

    private Products products;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductsResource productsResource = new ProductsResource();
        ReflectionTestUtils.setField(productsResource, "productsRepository", productsRepository);
        ReflectionTestUtils.setField(productsResource, "productsMapper", productsMapper);
        ReflectionTestUtils.setField(productsResource, "productsSearchRepository", productsSearchRepository);
        this.restProductsMockMvc = MockMvcBuilders.standaloneSetup(productsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        products = new Products();
        products.setProductCode(DEFAULT_PRODUCT_CODE);
        products.setProductShortName(DEFAULT_PRODUCT_SHORT_NAME);
        products.setProductName(DEFAULT_PRODUCT_NAME);
        products.setProductStartDate(DEFAULT_PRODUCT_START_DATE);
        products.setProductEndDate(DEFAULT_PRODUCT_END_DATE);
        products.setProductStatus(DEFAULT_PRODUCT_STATUS);
    }

    @Test
    @Transactional
    public void createProducts() throws Exception {
        int databaseSizeBeforeCreate = productsRepository.findAll().size();

        // Create the Products
        ProductsDTO productsDTO = productsMapper.productsToProductsDTO(products);

        restProductsMockMvc.perform(post("/api/productss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
                .andExpect(status().isCreated());

        // Validate the Products in the database
        List<Products> productss = productsRepository.findAll();
        assertThat(productss).hasSize(databaseSizeBeforeCreate + 1);
        Products testProducts = productss.get(productss.size() - 1);
        assertThat(testProducts.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testProducts.getProductShortName()).isEqualTo(DEFAULT_PRODUCT_SHORT_NAME);
        assertThat(testProducts.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProducts.getProductStartDate()).isEqualTo(DEFAULT_PRODUCT_START_DATE);
        assertThat(testProducts.getProductEndDate()).isEqualTo(DEFAULT_PRODUCT_END_DATE);
        assertThat(testProducts.getProductStatus()).isEqualTo(DEFAULT_PRODUCT_STATUS);
    }

    @Test
    @Transactional
    public void checkProductCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setProductCode(null);

        // Create the Products, which fails.
        ProductsDTO productsDTO = productsMapper.productsToProductsDTO(products);

        restProductsMockMvc.perform(post("/api/productss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
                .andExpect(status().isBadRequest());

        List<Products> productss = productsRepository.findAll();
        assertThat(productss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductShortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setProductShortName(null);

        // Create the Products, which fails.
        ProductsDTO productsDTO = productsMapper.productsToProductsDTO(products);

        restProductsMockMvc.perform(post("/api/productss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
                .andExpect(status().isBadRequest());

        List<Products> productss = productsRepository.findAll();
        assertThat(productss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setProductName(null);

        // Create the Products, which fails.
        ProductsDTO productsDTO = productsMapper.productsToProductsDTO(products);

        restProductsMockMvc.perform(post("/api/productss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
                .andExpect(status().isBadRequest());

        List<Products> productss = productsRepository.findAll();
        assertThat(productss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setProductStartDate(null);

        // Create the Products, which fails.
        ProductsDTO productsDTO = productsMapper.productsToProductsDTO(products);

        restProductsMockMvc.perform(post("/api/productss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
                .andExpect(status().isBadRequest());

        List<Products> productss = productsRepository.findAll();
        assertThat(productss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setProductEndDate(null);

        // Create the Products, which fails.
        ProductsDTO productsDTO = productsMapper.productsToProductsDTO(products);

        restProductsMockMvc.perform(post("/api/productss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
                .andExpect(status().isBadRequest());

        List<Products> productss = productsRepository.findAll();
        assertThat(productss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setProductStatus(null);

        // Create the Products, which fails.
        ProductsDTO productsDTO = productsMapper.productsToProductsDTO(products);

        restProductsMockMvc.perform(post("/api/productss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
                .andExpect(status().isBadRequest());

        List<Products> productss = productsRepository.findAll();
        assertThat(productss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductss() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the productss
        restProductsMockMvc.perform(get("/api/productss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(products.getId().intValue())))
                .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE.toString())))
                .andExpect(jsonPath("$.[*].productShortName").value(hasItem(DEFAULT_PRODUCT_SHORT_NAME.toString())))
                .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())))
                .andExpect(jsonPath("$.[*].productStartDate").value(hasItem(DEFAULT_PRODUCT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].productEndDate").value(hasItem(DEFAULT_PRODUCT_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].productStatus").value(hasItem(DEFAULT_PRODUCT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get the products
        restProductsMockMvc.perform(get("/api/productss/{id}", products.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(products.getId().intValue()))
            .andExpect(jsonPath("$.productCode").value(DEFAULT_PRODUCT_CODE.toString()))
            .andExpect(jsonPath("$.productShortName").value(DEFAULT_PRODUCT_SHORT_NAME.toString()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME.toString()))
            .andExpect(jsonPath("$.productStartDate").value(DEFAULT_PRODUCT_START_DATE.toString()))
            .andExpect(jsonPath("$.productEndDate").value(DEFAULT_PRODUCT_END_DATE.toString()))
            .andExpect(jsonPath("$.productStatus").value(DEFAULT_PRODUCT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProducts() throws Exception {
        // Get the products
        restProductsMockMvc.perform(get("/api/productss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

		int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Update the products
        products.setProductCode(UPDATED_PRODUCT_CODE);
        products.setProductShortName(UPDATED_PRODUCT_SHORT_NAME);
        products.setProductName(UPDATED_PRODUCT_NAME);
        products.setProductStartDate(UPDATED_PRODUCT_START_DATE);
        products.setProductEndDate(UPDATED_PRODUCT_END_DATE);
        products.setProductStatus(UPDATED_PRODUCT_STATUS);
        ProductsDTO productsDTO = productsMapper.productsToProductsDTO(products);

        restProductsMockMvc.perform(put("/api/productss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productsDTO)))
                .andExpect(status().isOk());

        // Validate the Products in the database
        List<Products> productss = productsRepository.findAll();
        assertThat(productss).hasSize(databaseSizeBeforeUpdate);
        Products testProducts = productss.get(productss.size() - 1);
        assertThat(testProducts.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testProducts.getProductShortName()).isEqualTo(UPDATED_PRODUCT_SHORT_NAME);
        assertThat(testProducts.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProducts.getProductStartDate()).isEqualTo(UPDATED_PRODUCT_START_DATE);
        assertThat(testProducts.getProductEndDate()).isEqualTo(UPDATED_PRODUCT_END_DATE);
        assertThat(testProducts.getProductStatus()).isEqualTo(UPDATED_PRODUCT_STATUS);
    }

    @Test
    @Transactional
    public void deleteProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

		int databaseSizeBeforeDelete = productsRepository.findAll().size();

        // Get the products
        restProductsMockMvc.perform(delete("/api/productss/{id}", products.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Products> productss = productsRepository.findAll();
        assertThat(productss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
