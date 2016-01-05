package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.PricingTier;
import com.barclays.bbilling.repository.PricingTierRepository;
import com.barclays.bbilling.repository.search.PricingTierSearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.PricingTierDTO;
import com.barclays.bbilling.web.rest.mapper.PricingTierMapper;
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
 * REST controller for managing PricingTier.
 */
@RestController
@RequestMapping("/api")
public class PricingTierResource {

    private final Logger log = LoggerFactory.getLogger(PricingTierResource.class);

    @Inject
    private PricingTierRepository pricingTierRepository;

    @Inject
    private PricingTierMapper pricingTierMapper;

    @Inject
    private PricingTierSearchRepository pricingTierSearchRepository;

    /**
     * POST  /pricingTiers -> Create a new pricingTier.
     */
    @RequestMapping(value = "/pricingTiers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PricingTierDTO> createPricingTier(@Valid @RequestBody PricingTierDTO pricingTierDTO) throws URISyntaxException {
        log.debug("REST request to save PricingTier : {}", pricingTierDTO);
        if (pricingTierDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new pricingTier cannot already have an ID").body(null);
        }
        PricingTier pricingTier = pricingTierMapper.pricingTierDTOToPricingTier(pricingTierDTO);
        PricingTier result = pricingTierRepository.save(pricingTier);
        pricingTierSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pricingTiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pricingTier", result.getId().toString()))
            .body(pricingTierMapper.pricingTierToPricingTierDTO(result));
    }

    /**
     * PUT  /pricingTiers -> Updates an existing pricingTier.
     */
    @RequestMapping(value = "/pricingTiers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PricingTierDTO> updatePricingTier(@Valid @RequestBody PricingTierDTO pricingTierDTO) throws URISyntaxException {
        log.debug("REST request to update PricingTier : {}", pricingTierDTO);
        if (pricingTierDTO.getId() == null) {
            return createPricingTier(pricingTierDTO);
        }
        PricingTier pricingTier = pricingTierMapper.pricingTierDTOToPricingTier(pricingTierDTO);
        PricingTier result = pricingTierRepository.save(pricingTier);
        pricingTierSearchRepository.save(pricingTier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pricingTier", pricingTierDTO.getId().toString()))
            .body(pricingTierMapper.pricingTierToPricingTierDTO(result));
    }

    /**
     * GET  /pricingTiers -> get all the pricingTiers.
     */
    @RequestMapping(value = "/pricingTiers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PricingTierDTO>> getAllPricingTiers(Pageable pageable)
        throws URISyntaxException {
        Page<PricingTier> page = pricingTierRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pricingTiers");
        return new ResponseEntity<>(page.getContent().stream()
            .map(pricingTierMapper::pricingTierToPricingTierDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /pricingTiers/:id -> get the "id" pricingTier.
     */
    @RequestMapping(value = "/pricingTiers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PricingTierDTO> getPricingTier(@PathVariable Long id) {
        log.debug("REST request to get PricingTier : {}", id);
        return Optional.ofNullable(pricingTierRepository.findOne(id))
            .map(pricingTierMapper::pricingTierToPricingTierDTO)
            .map(pricingTierDTO -> new ResponseEntity<>(
                pricingTierDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pricingTiers/:id -> delete the "id" pricingTier.
     */
    @RequestMapping(value = "/pricingTiers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePricingTier(@PathVariable Long id) {
        log.debug("REST request to delete PricingTier : {}", id);
        pricingTierRepository.delete(id);
        pricingTierSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pricingTier", id.toString())).build();
    }

    /**
     * SEARCH  /_search/pricingTiers/:query -> search for the pricingTier corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/pricingTiers/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PricingTierDTO> searchPricingTiers(@PathVariable String query) {
        return StreamSupport
            .stream(pricingTierSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(pricingTierMapper::pricingTierToPricingTierDTO)
            .collect(Collectors.toList());
    }
}
