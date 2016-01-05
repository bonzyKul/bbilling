package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.Currency;
import com.barclays.bbilling.repository.CurrencyRepository;
import com.barclays.bbilling.repository.search.CurrencySearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.CurrencyDTO;
import com.barclays.bbilling.web.rest.mapper.CurrencyMapper;
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
 * REST controller for managing Currency.
 */
@RestController
@RequestMapping("/api")
public class CurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyResource.class);

    @Inject
    private CurrencyRepository currencyRepository;

    @Inject
    private CurrencyMapper currencyMapper;

    @Inject
    private CurrencySearchRepository currencySearchRepository;

    /**
     * POST  /currencys -> Create a new currency.
     */
    @RequestMapping(value = "/currencys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CurrencyDTO> createCurrency(@Valid @RequestBody CurrencyDTO currencyDTO) throws URISyntaxException {
        log.debug("REST request to save Currency : {}", currencyDTO);
        if (currencyDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new currency cannot already have an ID").body(null);
        }
        Currency currency = currencyMapper.currencyDTOToCurrency(currencyDTO);
        Currency result = currencyRepository.save(currency);
        currencySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/currencys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("currency", result.getId().toString()))
            .body(currencyMapper.currencyToCurrencyDTO(result));
    }

    /**
     * PUT  /currencys -> Updates an existing currency.
     */
    @RequestMapping(value = "/currencys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CurrencyDTO> updateCurrency(@Valid @RequestBody CurrencyDTO currencyDTO) throws URISyntaxException {
        log.debug("REST request to update Currency : {}", currencyDTO);
        if (currencyDTO.getId() == null) {
            return createCurrency(currencyDTO);
        }
        Currency currency = currencyMapper.currencyDTOToCurrency(currencyDTO);
        Currency result = currencyRepository.save(currency);
        currencySearchRepository.save(currency);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("currency", currencyDTO.getId().toString()))
            .body(currencyMapper.currencyToCurrencyDTO(result));
    }

    /**
     * GET  /currencys -> get all the currencys.
     */
    @RequestMapping(value = "/currencys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CurrencyDTO>> getAllCurrencys(Pageable pageable)
        throws URISyntaxException {
        Page<Currency> page = currencyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/currencys");
        return new ResponseEntity<>(page.getContent().stream()
            .map(currencyMapper::currencyToCurrencyDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /currencys/:id -> get the "id" currency.
     */
    @RequestMapping(value = "/currencys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CurrencyDTO> getCurrency(@PathVariable Long id) {
        log.debug("REST request to get Currency : {}", id);
        return Optional.ofNullable(currencyRepository.findOne(id))
            .map(currencyMapper::currencyToCurrencyDTO)
            .map(currencyDTO -> new ResponseEntity<>(
                currencyDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /currencys/:id -> delete the "id" currency.
     */
    @RequestMapping(value = "/currencys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        log.debug("REST request to delete Currency : {}", id);
        currencyRepository.delete(id);
        currencySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("currency", id.toString())).build();
    }

    /**
     * SEARCH  /_search/currencys/:query -> search for the currency corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/currencys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CurrencyDTO> searchCurrencys(@PathVariable String query) {
        return StreamSupport
            .stream(currencySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(currencyMapper::currencyToCurrencyDTO)
            .collect(Collectors.toList());
    }
}
