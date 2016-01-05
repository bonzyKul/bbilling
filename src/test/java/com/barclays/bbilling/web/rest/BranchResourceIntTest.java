package com.barclays.bbilling.web.rest;

import com.barclays.bbilling.Application;
import com.barclays.bbilling.domain.Branch;
import com.barclays.bbilling.repository.BranchRepository;
import com.barclays.bbilling.repository.search.BranchSearchRepository;
import com.barclays.bbilling.web.rest.dto.BranchDTO;
import com.barclays.bbilling.web.rest.mapper.BranchMapper;

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
 * Test class for the BranchResource REST controller.
 *
 * @see BranchResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BranchResourceIntTest {

    private static final String DEFAULT_BRANCH_CODE = "A";
    private static final String UPDATED_BRANCH_CODE = "B";
    private static final String DEFAULT_BRANCH_NAME = "A";
    private static final String UPDATED_BRANCH_NAME = "B";

    @Inject
    private BranchRepository branchRepository;

    @Inject
    private BranchMapper branchMapper;

    @Inject
    private BranchSearchRepository branchSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBranchMockMvc;

    private Branch branch;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BranchResource branchResource = new BranchResource();
        ReflectionTestUtils.setField(branchResource, "branchRepository", branchRepository);
        ReflectionTestUtils.setField(branchResource, "branchMapper", branchMapper);
        ReflectionTestUtils.setField(branchResource, "branchSearchRepository", branchSearchRepository);
        this.restBranchMockMvc = MockMvcBuilders.standaloneSetup(branchResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        branch = new Branch();
        branch.setBranchCode(DEFAULT_BRANCH_CODE);
        branch.setBranchName(DEFAULT_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void createBranch() throws Exception {
        int databaseSizeBeforeCreate = branchRepository.findAll().size();

        // Create the Branch
        BranchDTO branchDTO = branchMapper.branchToBranchDTO(branch);

        restBranchMockMvc.perform(post("/api/branchs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchDTO)))
                .andExpect(status().isCreated());

        // Validate the Branch in the database
        List<Branch> branchs = branchRepository.findAll();
        assertThat(branchs).hasSize(databaseSizeBeforeCreate + 1);
        Branch testBranch = branchs.get(branchs.size() - 1);
        assertThat(testBranch.getBranchCode()).isEqualTo(DEFAULT_BRANCH_CODE);
        assertThat(testBranch.getBranchName()).isEqualTo(DEFAULT_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void checkBranchCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branch.setBranchCode(null);

        // Create the Branch, which fails.
        BranchDTO branchDTO = branchMapper.branchToBranchDTO(branch);

        restBranchMockMvc.perform(post("/api/branchs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchDTO)))
                .andExpect(status().isBadRequest());

        List<Branch> branchs = branchRepository.findAll();
        assertThat(branchs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBranchNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branch.setBranchName(null);

        // Create the Branch, which fails.
        BranchDTO branchDTO = branchMapper.branchToBranchDTO(branch);

        restBranchMockMvc.perform(post("/api/branchs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchDTO)))
                .andExpect(status().isBadRequest());

        List<Branch> branchs = branchRepository.findAll();
        assertThat(branchs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBranchs() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchs
        restBranchMockMvc.perform(get("/api/branchs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
                .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE.toString())))
                .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME.toString())));
    }

    @Test
    @Transactional
    public void getBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get the branch
        restBranchMockMvc.perform(get("/api/branchs/{id}", branch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(branch.getId().intValue()))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE.toString()))
            .andExpect(jsonPath("$.branchName").value(DEFAULT_BRANCH_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBranch() throws Exception {
        // Get the branch
        restBranchMockMvc.perform(get("/api/branchs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

		int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch
        branch.setBranchCode(UPDATED_BRANCH_CODE);
        branch.setBranchName(UPDATED_BRANCH_NAME);
        BranchDTO branchDTO = branchMapper.branchToBranchDTO(branch);

        restBranchMockMvc.perform(put("/api/branchs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchDTO)))
                .andExpect(status().isOk());

        // Validate the Branch in the database
        List<Branch> branchs = branchRepository.findAll();
        assertThat(branchs).hasSize(databaseSizeBeforeUpdate);
        Branch testBranch = branchs.get(branchs.size() - 1);
        assertThat(testBranch.getBranchCode()).isEqualTo(UPDATED_BRANCH_CODE);
        assertThat(testBranch.getBranchName()).isEqualTo(UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void deleteBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

		int databaseSizeBeforeDelete = branchRepository.findAll().size();

        // Get the branch
        restBranchMockMvc.perform(delete("/api/branchs/{id}", branch.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Branch> branchs = branchRepository.findAll();
        assertThat(branchs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
