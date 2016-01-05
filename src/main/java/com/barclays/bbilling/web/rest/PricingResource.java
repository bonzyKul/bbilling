package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.Pricing;
import com.barclays.bbilling.repository.PricingRepository;
import com.barclays.bbilling.repository.search.PricingSearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.PricingDTO;
import com.barclays.bbilling.web.rest.mapper.PricingMapper;
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
 * REST controller for managing Pricing.
 */
@RestController
@RequestMapping("/api")
public class PricingResource {

    private final Logger log = LoggerFactory.getLogger(PricingResource.class);

    @Inject
    private PricingRepository pricingRepository;

    @Inject
    private PricingMapper pricingMapper;

    @Inject
    private PricingSearchRepository pricingSearchRepository;

    /**
     * POST  /pricings -> Create a new pricing.
     */
    @RequestMapping(value = "/pricings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PricingDTO> createPricing(@Valid @RequestBody PricingDTO pricingDTO) throws URISyntaxException {
        log.debug("REST request to save Pricing : {}", pricingDTO);
        if (pricingDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new pricing cannot already have an ID").body(null);
        }
        Pricing pricing = pricingMapper.pricingDTOToPricing(pricingDTO);
        Pricing result = pricingRepository.save(pricing);
        pricingSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pricings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pricing", result.getId().toString()))
            .body(pricingMapper.pricingToPricingDTO(result));
    }

    /**
     * PUT  /pricings -> Updates an existing pricing.
     */
    @RequestMapping(value = "/pricings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PricingDTO> updatePricing(@Valid @RequestBody PricingDTO pricingDTO) throws URISyntaxException {
        log.debug("REST request to update Pricing : {}", pricingDTO);
        if (pricingDTO.getId() == null) {
            return createPricing(pricingDTO);
        }
        Pricing pricing = pricingMapper.pricingDTOToPricing(pricingDTO);
        Pricing result = pricingRepository.save(pricing);
        pricingSearchRepository.save(pricing);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pricing", pricingDTO.getId().toString()))
            .body(pricingMapper.pricingToPricingDTO(result));
    }

    /**
     * GET  /pricings -> get all the pricings.
     */
    @RequestMapping(value = "/pricings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PricingDTO>> getAllPricings(Pageable pageable)
        throws URISyntaxException {
        Page<Pricing> page = pricingRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pricings");
        return new ResponseEntity<>(page.getContent().stream()
            .map(pricingMapper::pricingToPricingDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /pricings/:id -> get the "id" pricing.
     */
    @RequestMapping(value = "/pricings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PricingDTO> getPricing(@PathVariable Long id) {
        log.debug("REST request to get Pricing : {}", id);
        return Optional.ofNullable(pricingRepository.findOne(id))
            .map(pricingMapper::pricingToPricingDTO)
            .map(pricingDTO -> new ResponseEntity<>(
                pricingDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pricings/:id -> delete the "id" pricing.
     */
    @RequestMapping(value = "/pricings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePricing(@PathVariable Long id) {
        log.debug("REST request to delete Pricing : {}", id);
        pricingRepository.delete(id);
        pricingSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pricing", id.toString())).build();
    }

    /**
     * SEARCH  /_search/pricings/:query -> search for the pricing corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/pricings/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PricingDTO> searchPricings(@PathVariable String query) {
        return StreamSupport
            .stream(pricingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(pricingMapper::pricingToPricingDTO)
            .collect(Collectors.toList());
    }
}
