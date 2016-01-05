package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.AccountFamily;
import com.barclays.bbilling.repository.AccountFamilyRepository;
import com.barclays.bbilling.repository.search.AccountFamilySearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.AccountFamilyDTO;
import com.barclays.bbilling.web.rest.mapper.AccountFamilyMapper;
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
 * REST controller for managing AccountFamily.
 */
@RestController
@RequestMapping("/api")
public class AccountFamilyResource {

    private final Logger log = LoggerFactory.getLogger(AccountFamilyResource.class);

    @Inject
    private AccountFamilyRepository accountFamilyRepository;

    @Inject
    private AccountFamilyMapper accountFamilyMapper;

    @Inject
    private AccountFamilySearchRepository accountFamilySearchRepository;

    /**
     * POST  /accountFamilys -> Create a new accountFamily.
     */
    @RequestMapping(value = "/accountFamilys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountFamilyDTO> createAccountFamily(@Valid @RequestBody AccountFamilyDTO accountFamilyDTO) throws URISyntaxException {
        log.debug("REST request to save AccountFamily : {}", accountFamilyDTO);
        if (accountFamilyDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new accountFamily cannot already have an ID").body(null);
        }
        AccountFamily accountFamily = accountFamilyMapper.accountFamilyDTOToAccountFamily(accountFamilyDTO);
        AccountFamily result = accountFamilyRepository.save(accountFamily);
        accountFamilySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/accountFamilys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("accountFamily", result.getId().toString()))
            .body(accountFamilyMapper.accountFamilyToAccountFamilyDTO(result));
    }

    /**
     * PUT  /accountFamilys -> Updates an existing accountFamily.
     */
    @RequestMapping(value = "/accountFamilys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountFamilyDTO> updateAccountFamily(@Valid @RequestBody AccountFamilyDTO accountFamilyDTO) throws URISyntaxException {
        log.debug("REST request to update AccountFamily : {}", accountFamilyDTO);
        if (accountFamilyDTO.getId() == null) {
            return createAccountFamily(accountFamilyDTO);
        }
        AccountFamily accountFamily = accountFamilyMapper.accountFamilyDTOToAccountFamily(accountFamilyDTO);
        AccountFamily result = accountFamilyRepository.save(accountFamily);
        accountFamilySearchRepository.save(accountFamily);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("accountFamily", accountFamilyDTO.getId().toString()))
            .body(accountFamilyMapper.accountFamilyToAccountFamilyDTO(result));
    }

    /**
     * GET  /accountFamilys -> get all the accountFamilys.
     */
    @RequestMapping(value = "/accountFamilys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<AccountFamilyDTO>> getAllAccountFamilys(Pageable pageable)
        throws URISyntaxException {
        Page<AccountFamily> page = accountFamilyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accountFamilys");
        return new ResponseEntity<>(page.getContent().stream()
            .map(accountFamilyMapper::accountFamilyToAccountFamilyDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /accountFamilys/:id -> get the "id" accountFamily.
     */
    @RequestMapping(value = "/accountFamilys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountFamilyDTO> getAccountFamily(@PathVariable Long id) {
        log.debug("REST request to get AccountFamily : {}", id);
        return Optional.ofNullable(accountFamilyRepository.findOne(id))
            .map(accountFamilyMapper::accountFamilyToAccountFamilyDTO)
            .map(accountFamilyDTO -> new ResponseEntity<>(
                accountFamilyDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /accountFamilys/:id -> delete the "id" accountFamily.
     */
    @RequestMapping(value = "/accountFamilys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAccountFamily(@PathVariable Long id) {
        log.debug("REST request to delete AccountFamily : {}", id);
        accountFamilyRepository.delete(id);
        accountFamilySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("accountFamily", id.toString())).build();
    }

    /**
     * SEARCH  /_search/accountFamilys/:query -> search for the accountFamily corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/accountFamilys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AccountFamilyDTO> searchAccountFamilys(@PathVariable String query) {
        return StreamSupport
            .stream(accountFamilySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(accountFamilyMapper::accountFamilyToAccountFamilyDTO)
            .collect(Collectors.toList());
    }
}
