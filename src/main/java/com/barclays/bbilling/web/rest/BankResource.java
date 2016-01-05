package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.Bank;
import com.barclays.bbilling.repository.BankRepository;
import com.barclays.bbilling.repository.search.BankSearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.BankDTO;
import com.barclays.bbilling.web.rest.mapper.BankMapper;
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
 * REST controller for managing Bank.
 */
@RestController
@RequestMapping("/api")
public class BankResource {

    private final Logger log = LoggerFactory.getLogger(BankResource.class);

    @Inject
    private BankRepository bankRepository;

    @Inject
    private BankMapper bankMapper;

    @Inject
    private BankSearchRepository bankSearchRepository;

    /**
     * POST  /banks -> Create a new bank.
     */
    @RequestMapping(value = "/banks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BankDTO> createBank(@Valid @RequestBody BankDTO bankDTO) throws URISyntaxException {
        log.debug("REST request to save Bank : {}", bankDTO);
        if (bankDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new bank cannot already have an ID").body(null);
        }
        Bank bank = bankMapper.bankDTOToBank(bankDTO);
        Bank result = bankRepository.save(bank);
        bankSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/banks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bank", result.getId().toString()))
            .body(bankMapper.bankToBankDTO(result));
    }

    /**
     * PUT  /banks -> Updates an existing bank.
     */
    @RequestMapping(value = "/banks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BankDTO> updateBank(@Valid @RequestBody BankDTO bankDTO) throws URISyntaxException {
        log.debug("REST request to update Bank : {}", bankDTO);
        if (bankDTO.getId() == null) {
            return createBank(bankDTO);
        }
        Bank bank = bankMapper.bankDTOToBank(bankDTO);
        Bank result = bankRepository.save(bank);
        bankSearchRepository.save(bank);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bank", bankDTO.getId().toString()))
            .body(bankMapper.bankToBankDTO(result));
    }

    /**
     * GET  /banks -> get all the banks.
     */
    @RequestMapping(value = "/banks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<BankDTO>> getAllBanks(Pageable pageable)
        throws URISyntaxException {
        Page<Bank> page = bankRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/banks");
        return new ResponseEntity<>(page.getContent().stream()
            .map(bankMapper::bankToBankDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /banks/:id -> get the "id" bank.
     */
    @RequestMapping(value = "/banks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BankDTO> getBank(@PathVariable Long id) {
        log.debug("REST request to get Bank : {}", id);
        return Optional.ofNullable(bankRepository.findOne(id))
            .map(bankMapper::bankToBankDTO)
            .map(bankDTO -> new ResponseEntity<>(
                bankDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /banks/:id -> delete the "id" bank.
     */
    @RequestMapping(value = "/banks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBank(@PathVariable Long id) {
        log.debug("REST request to delete Bank : {}", id);
        bankRepository.delete(id);
        bankSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bank", id.toString())).build();
    }

    /**
     * SEARCH  /_search/banks/:query -> search for the bank corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/banks/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BankDTO> searchBanks(@PathVariable String query) {
        return StreamSupport
            .stream(bankSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(bankMapper::bankToBankDTO)
            .collect(Collectors.toList());
    }
}
