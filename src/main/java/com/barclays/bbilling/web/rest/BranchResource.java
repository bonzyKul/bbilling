package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.Branch;
import com.barclays.bbilling.repository.BranchRepository;
import com.barclays.bbilling.repository.search.BranchSearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.BranchDTO;
import com.barclays.bbilling.web.rest.mapper.BranchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Branch.
 */
@RestController
@RequestMapping("/api")
public class BranchResource {

    private final Logger log = LoggerFactory.getLogger(BranchResource.class);

    @Inject
    private BranchRepository branchRepository;

    @Inject
    private BranchMapper branchMapper;

    @Inject
    private BranchSearchRepository branchSearchRepository;

    /**
     * POST  /branchs -> Create a new branch.
     */
    @RequestMapping(value = "/branchs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BranchDTO> createBranch(@Valid @RequestBody BranchDTO branchDTO) throws URISyntaxException {
        log.debug("REST request to save Branch : {}", branchDTO);
        if (branchDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new branch cannot already have an ID").body(null);
        }
        Branch branch = branchMapper.branchDTOToBranch(branchDTO);
        Branch result = branchRepository.save(branch);
        branchSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/branchs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("branch", result.getId().toString()))
            .body(branchMapper.branchToBranchDTO(result));
    }

    /**
     * PUT  /branchs -> Updates an existing branch.
     */
    @RequestMapping(value = "/branchs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BranchDTO> updateBranch(@Valid @RequestBody BranchDTO branchDTO) throws URISyntaxException {
        log.debug("REST request to update Branch : {}", branchDTO);
        if (branchDTO.getId() == null) {
            return createBranch(branchDTO);
        }
        Branch branch = branchMapper.branchDTOToBranch(branchDTO);
        Branch result = branchRepository.save(branch);
        branchSearchRepository.save(branch);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("branch", branchDTO.getId().toString()))
            .body(branchMapper.branchToBranchDTO(result));
    }

    /**
     * GET  /branchs -> get all the branchs.
     */
    @RequestMapping(value = "/branchs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<BranchDTO>> getAllBranchs(Pageable pageable)
        throws URISyntaxException {
        Page<Branch> page = branchRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/branchs");
        return new ResponseEntity<>(page.getContent().stream()
            .map(branchMapper::branchToBranchDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /branchs/:id -> get the "id" branch.
     */
    @RequestMapping(value = "/branchs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BranchDTO> getBranch(@PathVariable Long id) {
        log.debug("REST request to get Branch : {}", id);
        return Optional.ofNullable(branchRepository.findOne(id))
            .map(branchMapper::branchToBranchDTO)
            .map(branchDTO -> new ResponseEntity<>(
                branchDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /branchs/:id -> delete the "id" branch.
     */
    @RequestMapping(value = "/branchs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        log.debug("REST request to delete Branch : {}", id);
        branchRepository.delete(id);
        branchSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("branch", id.toString())).build();
    }

    /**
     * SEARCH  /_search/branchs/:query -> search for the branch corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/branchs/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BranchDTO> searchBranchs(@PathVariable String query) {
        return StreamSupport
            .stream(branchSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(branchMapper::branchToBranchDTO)
            .collect(Collectors.toList());
    }
}
