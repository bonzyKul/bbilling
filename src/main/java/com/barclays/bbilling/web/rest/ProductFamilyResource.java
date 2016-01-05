package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.ProductFamily;
import com.barclays.bbilling.repository.ProductFamilyRepository;
import com.barclays.bbilling.repository.search.ProductFamilySearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.ProductFamilyDTO;
import com.barclays.bbilling.web.rest.mapper.ProductFamilyMapper;
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
 * REST controller for managing ProductFamily.
 */
@RestController
@RequestMapping("/api")
public class ProductFamilyResource {

    private final Logger log = LoggerFactory.getLogger(ProductFamilyResource.class);

    @Inject
    private ProductFamilyRepository productFamilyRepository;

    @Inject
    private ProductFamilyMapper productFamilyMapper;

    @Inject
    private ProductFamilySearchRepository productFamilySearchRepository;

    /**
     * POST  /productFamilys -> Create a new productFamily.
     */
    @RequestMapping(value = "/productFamilys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductFamilyDTO> createProductFamily(@Valid @RequestBody ProductFamilyDTO productFamilyDTO) throws URISyntaxException {
        log.debug("REST request to save ProductFamily : {}", productFamilyDTO);
        if (productFamilyDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new productFamily cannot already have an ID").body(null);
        }
        ProductFamily productFamily = productFamilyMapper.productFamilyDTOToProductFamily(productFamilyDTO);
        ProductFamily result = productFamilyRepository.save(productFamily);
        productFamilySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/productFamilys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("productFamily", result.getId().toString()))
            .body(productFamilyMapper.productFamilyToProductFamilyDTO(result));
    }

    /**
     * PUT  /productFamilys -> Updates an existing productFamily.
     */
    @RequestMapping(value = "/productFamilys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductFamilyDTO> updateProductFamily(@Valid @RequestBody ProductFamilyDTO productFamilyDTO) throws URISyntaxException {
        log.debug("REST request to update ProductFamily : {}", productFamilyDTO);
        if (productFamilyDTO.getId() == null) {
            return createProductFamily(productFamilyDTO);
        }
        ProductFamily productFamily = productFamilyMapper.productFamilyDTOToProductFamily(productFamilyDTO);
        ProductFamily result = productFamilyRepository.save(productFamily);
        productFamilySearchRepository.save(productFamily);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("productFamily", productFamilyDTO.getId().toString()))
            .body(productFamilyMapper.productFamilyToProductFamilyDTO(result));
    }

    /**
     * GET  /productFamilys -> get all the productFamilys.
     */
    @RequestMapping(value = "/productFamilys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ProductFamilyDTO>> getAllProductFamilys(Pageable pageable)
        throws URISyntaxException {
        Page<ProductFamily> page = productFamilyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/productFamilys");
        return new ResponseEntity<>(page.getContent().stream()
            .map(productFamilyMapper::productFamilyToProductFamilyDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /productFamilys/:id -> get the "id" productFamily.
     */
    @RequestMapping(value = "/productFamilys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductFamilyDTO> getProductFamily(@PathVariable Long id) {
        log.debug("REST request to get ProductFamily : {}", id);
        return Optional.ofNullable(productFamilyRepository.findOne(id))
            .map(productFamilyMapper::productFamilyToProductFamilyDTO)
            .map(productFamilyDTO -> new ResponseEntity<>(
                productFamilyDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /productFamilys/:id -> delete the "id" productFamily.
     */
    @RequestMapping(value = "/productFamilys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProductFamily(@PathVariable Long id) {
        log.debug("REST request to delete ProductFamily : {}", id);
        productFamilyRepository.delete(id);
        productFamilySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("productFamily", id.toString())).build();
    }

    /**
     * SEARCH  /_search/productFamilys/:query -> search for the productFamily corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/productFamilys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductFamilyDTO> searchProductFamilys(@PathVariable String query) {
        return StreamSupport
            .stream(productFamilySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(productFamilyMapper::productFamilyToProductFamilyDTO)
            .collect(Collectors.toList());
    }
}
