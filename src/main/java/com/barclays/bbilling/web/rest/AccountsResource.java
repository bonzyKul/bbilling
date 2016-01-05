package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.Accounts;
import com.barclays.bbilling.repository.AccountsRepository;
import com.barclays.bbilling.repository.search.AccountsSearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.AccountsDTO;
import com.barclays.bbilling.web.rest.mapper.AccountsMapper;
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
 * REST controller for managing Accounts.
 */
@RestController
@RequestMapping("/api")
public class AccountsResource {

    private final Logger log = LoggerFactory.getLogger(AccountsResource.class);

    @Inject
    private AccountsRepository accountsRepository;

    @Inject
    private AccountsMapper accountsMapper;

    @Inject
    private AccountsSearchRepository accountsSearchRepository;

    /**
     * POST  /accountss -> Create a new accounts.
     */
    @RequestMapping(value = "/accountss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountsDTO> createAccounts(@Valid @RequestBody AccountsDTO accountsDTO) throws URISyntaxException {
        log.debug("REST request to save Accounts : {}", accountsDTO);
        if (accountsDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new accounts cannot already have an ID").body(null);
        }
        Accounts accounts = accountsMapper.accountsDTOToAccounts(accountsDTO);
        Accounts result = accountsRepository.save(accounts);
        accountsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/accountss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("accounts", result.getId().toString()))
            .body(accountsMapper.accountsToAccountsDTO(result));
    }

    /**
     * PUT  /accountss -> Updates an existing accounts.
     */
    @RequestMapping(value = "/accountss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountsDTO> updateAccounts(@Valid @RequestBody AccountsDTO accountsDTO) throws URISyntaxException {
        log.debug("REST request to update Accounts : {}", accountsDTO);
        if (accountsDTO.getId() == null) {
            return createAccounts(accountsDTO);
        }
        Accounts accounts = accountsMapper.accountsDTOToAccounts(accountsDTO);
        Accounts result = accountsRepository.save(accounts);
        accountsSearchRepository.save(accounts);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("accounts", accountsDTO.getId().toString()))
            .body(accountsMapper.accountsToAccountsDTO(result));
    }

    /**
     * GET  /accountss -> get all the accountss.
     */
    @RequestMapping(value = "/accountss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<AccountsDTO>> getAllAccountss(Pageable pageable)
        throws URISyntaxException {
        Page<Accounts> page = accountsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accountss");
        return new ResponseEntity<>(page.getContent().stream()
            .map(accountsMapper::accountsToAccountsDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /accountss/:id -> get the "id" accounts.
     */
    @RequestMapping(value = "/accountss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountsDTO> getAccounts(@PathVariable Long id) {
        log.debug("REST request to get Accounts : {}", id);
        return Optional.ofNullable(accountsRepository.findOne(id))
            .map(accountsMapper::accountsToAccountsDTO)
            .map(accountsDTO -> new ResponseEntity<>(
                accountsDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /accountss/:id -> delete the "id" accounts.
     */
    @RequestMapping(value = "/accountss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAccounts(@PathVariable Long id) {
        log.debug("REST request to delete Accounts : {}", id);
        accountsRepository.delete(id);
        accountsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("accounts", id.toString())).build();
    }

    /**
     * SEARCH  /_search/accountss/:query -> search for the accounts corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/accountss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AccountsDTO> searchAccountss(@PathVariable String query) {
        return StreamSupport
            .stream(accountsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(accountsMapper::accountsToAccountsDTO)
            .collect(Collectors.toList());
    }
}
