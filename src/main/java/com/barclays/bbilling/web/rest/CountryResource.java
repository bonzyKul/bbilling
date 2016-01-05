package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.Country;
import com.barclays.bbilling.repository.CountryRepository;
import com.barclays.bbilling.repository.search.CountrySearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.CountryDTO;
import com.barclays.bbilling.web.rest.mapper.CountryMapper;
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
 * REST controller for managing Country.
 */
@RestController
@RequestMapping("/api")
public class CountryResource {

    private final Logger log = LoggerFactory.getLogger(CountryResource.class);

    @Inject
    private CountryRepository countryRepository;

    @Inject
    private CountryMapper countryMapper;

    @Inject
    private CountrySearchRepository countrySearchRepository;

    /**
     * POST  /countrys -> Create a new country.
     */
    @RequestMapping(value = "/countrys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CountryDTO> createCountry(@Valid @RequestBody CountryDTO countryDTO) throws URISyntaxException {
        log.debug("REST request to save Country : {}", countryDTO);
        if (countryDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new country cannot already have an ID").body(null);
        }
        Country country = countryMapper.countryDTOToCountry(countryDTO);
        Country result = countryRepository.save(country);
        countrySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/countrys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("country", result.getId().toString()))
            .body(countryMapper.countryToCountryDTO(result));
    }

    /**
     * PUT  /countrys -> Updates an existing country.
     */
    @RequestMapping(value = "/countrys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CountryDTO> updateCountry(@Valid @RequestBody CountryDTO countryDTO) throws URISyntaxException {
        log.debug("REST request to update Country : {}", countryDTO);
        if (countryDTO.getId() == null) {
            return createCountry(countryDTO);
        }
        Country country = countryMapper.countryDTOToCountry(countryDTO);
        Country result = countryRepository.save(country);
        countrySearchRepository.save(country);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("country", countryDTO.getId().toString()))
            .body(countryMapper.countryToCountryDTO(result));
    }

    /**
     * GET  /countrys -> get all the countrys.
     */
    @RequestMapping(value = "/countrys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CountryDTO>> getAllCountrys(Pageable pageable)
        throws URISyntaxException {
        Page<Country> page = countryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/countrys");
        return new ResponseEntity<>(page.getContent().stream()
            .map(countryMapper::countryToCountryDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /countrys/:id -> get the "id" country.
     */
    @RequestMapping(value = "/countrys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CountryDTO> getCountry(@PathVariable Long id) {
        log.debug("REST request to get Country : {}", id);
        return Optional.ofNullable(countryRepository.findOne(id))
            .map(countryMapper::countryToCountryDTO)
            .map(countryDTO -> new ResponseEntity<>(
                countryDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /countrys/:id -> delete the "id" country.
     */
    @RequestMapping(value = "/countrys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        log.debug("REST request to delete Country : {}", id);
        countryRepository.delete(id);
        countrySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("country", id.toString())).build();
    }

    /**
     * SEARCH  /_search/countrys/:query -> search for the country corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/countrys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CountryDTO> searchCountrys(@PathVariable String query) {
        return StreamSupport
            .stream(countrySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(countryMapper::countryToCountryDTO)
            .collect(Collectors.toList());
    }
}
