package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.AccountPreferences;
import com.barclays.bbilling.repository.AccountPreferencesRepository;
import com.barclays.bbilling.repository.search.AccountPreferencesSearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.AccountPreferencesDTO;
import com.barclays.bbilling.web.rest.mapper.AccountPreferencesMapper;
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
 * REST controller for managing AccountPreferences.
 */
@RestController
@RequestMapping("/api")
public class AccountPreferencesResource {

    private final Logger log = LoggerFactory.getLogger(AccountPreferencesResource.class);

    @Inject
    private AccountPreferencesRepository accountPreferencesRepository;

    @Inject
    private AccountPreferencesMapper accountPreferencesMapper;

    @Inject
    private AccountPreferencesSearchRepository accountPreferencesSearchRepository;

    /**
     * POST  /accountPreferencess -> Create a new accountPreferences.
     */
    @RequestMapping(value = "/accountPreferencess",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountPreferencesDTO> createAccountPreferences(@Valid @RequestBody AccountPreferencesDTO accountPreferencesDTO) throws URISyntaxException {
        log.debug("REST request to save AccountPreferences : {}", accountPreferencesDTO);
        if (accountPreferencesDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new accountPreferences cannot already have an ID").body(null);
        }
        AccountPreferences accountPreferences = accountPreferencesMapper.accountPreferencesDTOToAccountPreferences(accountPreferencesDTO);
        AccountPreferences result = accountPreferencesRepository.save(accountPreferences);
        accountPreferencesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/accountPreferencess/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("accountPreferences", result.getId().toString()))
            .body(accountPreferencesMapper.accountPreferencesToAccountPreferencesDTO(result));
    }

    /**
     * PUT  /accountPreferencess -> Updates an existing accountPreferences.
     */
    @RequestMapping(value = "/accountPreferencess",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountPreferencesDTO> updateAccountPreferences(@Valid @RequestBody AccountPreferencesDTO accountPreferencesDTO) throws URISyntaxException {
        log.debug("REST request to update AccountPreferences : {}", accountPreferencesDTO);
        if (accountPreferencesDTO.getId() == null) {
            return createAccountPreferences(accountPreferencesDTO);
        }
        AccountPreferences accountPreferences = accountPreferencesMapper.accountPreferencesDTOToAccountPreferences(accountPreferencesDTO);
        AccountPreferences result = accountPreferencesRepository.save(accountPreferences);
        accountPreferencesSearchRepository.save(accountPreferences);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("accountPreferences", accountPreferencesDTO.getId().toString()))
            .body(accountPreferencesMapper.accountPreferencesToAccountPreferencesDTO(result));
    }

    /**
     * GET  /accountPreferencess -> get all the accountPreferencess.
     */
    @RequestMapping(value = "/accountPreferencess",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<AccountPreferencesDTO>> getAllAccountPreferencess(Pageable pageable)
        throws URISyntaxException {
        Page<AccountPreferences> page = accountPreferencesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accountPreferencess");
        return new ResponseEntity<>(page.getContent().stream()
            .map(accountPreferencesMapper::accountPreferencesToAccountPreferencesDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /accountPreferencess/:id -> get the "id" accountPreferences.
     */
    @RequestMapping(value = "/accountPreferencess/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountPreferencesDTO> getAccountPreferences(@PathVariable Long id) {
        log.debug("REST request to get AccountPreferences : {}", id);
        return Optional.ofNullable(accountPreferencesRepository.findOne(id))
            .map(accountPreferencesMapper::accountPreferencesToAccountPreferencesDTO)
            .map(accountPreferencesDTO -> new ResponseEntity<>(
                accountPreferencesDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /accountPreferencess/:id -> delete the "id" accountPreferences.
     */
    @RequestMapping(value = "/accountPreferencess/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAccountPreferences(@PathVariable Long id) {
        log.debug("REST request to delete AccountPreferences : {}", id);
        accountPreferencesRepository.delete(id);
        accountPreferencesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("accountPreferences", id.toString())).build();
    }

    /**
     * SEARCH  /_search/accountPreferencess/:query -> search for the accountPreferences corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/accountPreferencess/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AccountPreferencesDTO> searchAccountPreferencess(@PathVariable String query) {
        return StreamSupport
            .stream(accountPreferencesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(accountPreferencesMapper::accountPreferencesToAccountPreferencesDTO)
            .collect(Collectors.toList());
    }
}
