package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.ServiceCatalogue;
import com.barclays.bbilling.repository.ServiceCatalogueRepository;
import com.barclays.bbilling.repository.search.ServiceCatalogueSearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.ServiceCatalogueDTO;
import com.barclays.bbilling.web.rest.mapper.ServiceCatalogueMapper;
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
 * REST controller for managing ServiceCatalogue.
 */
@RestController
@RequestMapping("/api")
public class ServiceCatalogueResource {

    private final Logger log = LoggerFactory.getLogger(ServiceCatalogueResource.class);

    @Inject
    private ServiceCatalogueRepository serviceCatalogueRepository;

    @Inject
    private ServiceCatalogueMapper serviceCatalogueMapper;

    @Inject
    private ServiceCatalogueSearchRepository serviceCatalogueSearchRepository;

    /**
     * POST  /serviceCatalogues -> Create a new serviceCatalogue.
     */
    @RequestMapping(value = "/serviceCatalogues",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceCatalogueDTO> createServiceCatalogue(@Valid @RequestBody ServiceCatalogueDTO serviceCatalogueDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceCatalogue : {}", serviceCatalogueDTO);
        if (serviceCatalogueDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new serviceCatalogue cannot already have an ID").body(null);
        }
        ServiceCatalogue serviceCatalogue = serviceCatalogueMapper.serviceCatalogueDTOToServiceCatalogue(serviceCatalogueDTO);
        ServiceCatalogue result = serviceCatalogueRepository.save(serviceCatalogue);
        serviceCatalogueSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/serviceCatalogues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serviceCatalogue", result.getId().toString()))
            .body(serviceCatalogueMapper.serviceCatalogueToServiceCatalogueDTO(result));
    }

    /**
     * PUT  /serviceCatalogues -> Updates an existing serviceCatalogue.
     */
    @RequestMapping(value = "/serviceCatalogues",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceCatalogueDTO> updateServiceCatalogue(@Valid @RequestBody ServiceCatalogueDTO serviceCatalogueDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceCatalogue : {}", serviceCatalogueDTO);
        if (serviceCatalogueDTO.getId() == null) {
            return createServiceCatalogue(serviceCatalogueDTO);
        }
        ServiceCatalogue serviceCatalogue = serviceCatalogueMapper.serviceCatalogueDTOToServiceCatalogue(serviceCatalogueDTO);
        ServiceCatalogue result = serviceCatalogueRepository.save(serviceCatalogue);
        serviceCatalogueSearchRepository.save(serviceCatalogue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serviceCatalogue", serviceCatalogueDTO.getId().toString()))
            .body(serviceCatalogueMapper.serviceCatalogueToServiceCatalogueDTO(result));
    }

    /**
     * GET  /serviceCatalogues -> get all the serviceCatalogues.
     */
    @RequestMapping(value = "/serviceCatalogues",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceCatalogueDTO>> getAllServiceCatalogues(Pageable pageable)
        throws URISyntaxException {
        Page<ServiceCatalogue> page = serviceCatalogueRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/serviceCatalogues");
        return new ResponseEntity<>(page.getContent().stream()
            .map(serviceCatalogueMapper::serviceCatalogueToServiceCatalogueDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /serviceCatalogues/:id -> get the "id" serviceCatalogue.
     */
    @RequestMapping(value = "/serviceCatalogues/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceCatalogueDTO> getServiceCatalogue(@PathVariable Long id) {
        log.debug("REST request to get ServiceCatalogue : {}", id);
        return Optional.ofNullable(serviceCatalogueRepository.findOne(id))
            .map(serviceCatalogueMapper::serviceCatalogueToServiceCatalogueDTO)
            .map(serviceCatalogueDTO -> new ResponseEntity<>(
                serviceCatalogueDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /serviceCatalogues/:id -> delete the "id" serviceCatalogue.
     */
    @RequestMapping(value = "/serviceCatalogues/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteServiceCatalogue(@PathVariable Long id) {
        log.debug("REST request to delete ServiceCatalogue : {}", id);
        serviceCatalogueRepository.delete(id);
        serviceCatalogueSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serviceCatalogue", id.toString())).build();
    }

    /**
     * SEARCH  /_search/serviceCatalogues/:query -> search for the serviceCatalogue corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/serviceCatalogues/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ServiceCatalogueDTO> searchServiceCatalogues(@PathVariable String query) {
        return StreamSupport
            .stream(serviceCatalogueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(serviceCatalogueMapper::serviceCatalogueToServiceCatalogueDTO)
            .collect(Collectors.toList());
    }
}
