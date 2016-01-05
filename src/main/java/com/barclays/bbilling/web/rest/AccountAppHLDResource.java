package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.AccountAppHLD;
import com.barclays.bbilling.repository.AccountAppHLDRepository;
import com.barclays.bbilling.repository.search.AccountAppHLDSearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.AccountAppHLDDTO;
import com.barclays.bbilling.web.rest.mapper.AccountAppHLDMapper;
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
 * REST controller for managing AccountAppHLD.
 */
@RestController
@RequestMapping("/api")
public class AccountAppHLDResource {

    private final Logger log = LoggerFactory.getLogger(AccountAppHLDResource.class);

    @Inject
    private AccountAppHLDRepository accountAppHLDRepository;

    @Inject
    private AccountAppHLDMapper accountAppHLDMapper;

    @Inject
    private AccountAppHLDSearchRepository accountAppHLDSearchRepository;

    /**
     * POST  /accountAppHLDs -> Create a new accountAppHLD.
     */
    @RequestMapping(value = "/accountAppHLDs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountAppHLDDTO> createAccountAppHLD(@Valid @RequestBody AccountAppHLDDTO accountAppHLDDTO) throws URISyntaxException {
        log.debug("REST request to save AccountAppHLD : {}", accountAppHLDDTO);
        if (accountAppHLDDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new accountAppHLD cannot already have an ID").body(null);
        }
        AccountAppHLD accountAppHLD = accountAppHLDMapper.accountAppHLDDTOToAccountAppHLD(accountAppHLDDTO);
        AccountAppHLD result = accountAppHLDRepository.save(accountAppHLD);
        accountAppHLDSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/accountAppHLDs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("accountAppHLD", result.getId().toString()))
            .body(accountAppHLDMapper.accountAppHLDToAccountAppHLDDTO(result));
    }

    /**
     * PUT  /accountAppHLDs -> Updates an existing accountAppHLD.
     */
    @RequestMapping(value = "/accountAppHLDs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountAppHLDDTO> updateAccountAppHLD(@Valid @RequestBody AccountAppHLDDTO accountAppHLDDTO) throws URISyntaxException {
        log.debug("REST request to update AccountAppHLD : {}", accountAppHLDDTO);
        if (accountAppHLDDTO.getId() == null) {
            return createAccountAppHLD(accountAppHLDDTO);
        }
        AccountAppHLD accountAppHLD = accountAppHLDMapper.accountAppHLDDTOToAccountAppHLD(accountAppHLDDTO);
        AccountAppHLD result = accountAppHLDRepository.save(accountAppHLD);
        accountAppHLDSearchRepository.save(accountAppHLD);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("accountAppHLD", accountAppHLDDTO.getId().toString()))
            .body(accountAppHLDMapper.accountAppHLDToAccountAppHLDDTO(result));
    }

    /**
     * GET  /accountAppHLDs -> get all the accountAppHLDs.
     */
    @RequestMapping(value = "/accountAppHLDs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<AccountAppHLDDTO>> getAllAccountAppHLDs(Pageable pageable)
        throws URISyntaxException {
        Page<AccountAppHLD> page = accountAppHLDRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accountAppHLDs");
        return new ResponseEntity<>(page.getContent().stream()
            .map(accountAppHLDMapper::accountAppHLDToAccountAppHLDDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /accountAppHLDs/:id -> get the "id" accountAppHLD.
     */
    @RequestMapping(value = "/accountAppHLDs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountAppHLDDTO> getAccountAppHLD(@PathVariable Long id) {
        log.debug("REST request to get AccountAppHLD : {}", id);
        return Optional.ofNullable(accountAppHLDRepository.findOne(id))
            .map(accountAppHLDMapper::accountAppHLDToAccountAppHLDDTO)
            .map(accountAppHLDDTO -> new ResponseEntity<>(
                accountAppHLDDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /accountAppHLDs/:id -> delete the "id" accountAppHLD.
     */
    @RequestMapping(value = "/accountAppHLDs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAccountAppHLD(@PathVariable Long id) {
        log.debug("REST request to delete AccountAppHLD : {}", id);
        accountAppHLDRepository.delete(id);
        accountAppHLDSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("accountAppHLD", id.toString())).build();
    }

    /**
     * SEARCH  /_search/accountAppHLDs/:query -> search for the accountAppHLD corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/accountAppHLDs/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AccountAppHLDDTO> searchAccountAppHLDs(@PathVariable String query) {
        return StreamSupport
            .stream(accountAppHLDSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(accountAppHLDMapper::accountAppHLDToAccountAppHLDDTO)
            .collect(Collectors.toList());
    }
}
