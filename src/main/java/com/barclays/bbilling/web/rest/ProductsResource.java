package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.Products;
import com.barclays.bbilling.repository.ProductsRepository;
import com.barclays.bbilling.repository.search.ProductsSearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.ProductsDTO;
import com.barclays.bbilling.web.rest.mapper.ProductsMapper;
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
 * REST controller for managing Products.
 */
@RestController
@RequestMapping("/api")
public class ProductsResource {

    private final Logger log = LoggerFactory.getLogger(ProductsResource.class);

    @Inject
    private ProductsRepository productsRepository;

    @Inject
    private ProductsMapper productsMapper;

    @Inject
    private ProductsSearchRepository productsSearchRepository;

    /**
     * POST  /productss -> Create a new products.
     */
    @RequestMapping(value = "/productss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductsDTO> createProducts(@Valid @RequestBody ProductsDTO productsDTO) throws URISyntaxException {
        log.debug("REST request to save Products : {}", productsDTO);
        if (productsDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new products cannot already have an ID").body(null);
        }
        Products products = productsMapper.productsDTOToProducts(productsDTO);
        Products result = productsRepository.save(products);
        productsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/productss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("products", result.getId().toString()))
            .body(productsMapper.productsToProductsDTO(result));
    }

    /**
     * PUT  /productss -> Updates an existing products.
     */
    @RequestMapping(value = "/productss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductsDTO> updateProducts(@Valid @RequestBody ProductsDTO productsDTO) throws URISyntaxException {
        log.debug("REST request to update Products : {}", productsDTO);
        if (productsDTO.getId() == null) {
            return createProducts(productsDTO);
        }
        Products products = productsMapper.productsDTOToProducts(productsDTO);
        Products result = productsRepository.save(products);
        productsSearchRepository.save(products);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("products", productsDTO.getId().toString()))
            .body(productsMapper.productsToProductsDTO(result));
    }

    /**
     * GET  /productss -> get all the productss.
     */
    @RequestMapping(value = "/productss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ProductsDTO>> getAllProductss(Pageable pageable)
        throws URISyntaxException {
        Page<Products> page = productsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/productss");
        return new ResponseEntity<>(page.getContent().stream()
            .map(productsMapper::productsToProductsDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /productss/:id -> get the "id" products.
     */
    @RequestMapping(value = "/productss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductsDTO> getProducts(@PathVariable Long id) {
        log.debug("REST request to get Products : {}", id);
        return Optional.ofNullable(productsRepository.findOne(id))
            .map(productsMapper::productsToProductsDTO)
            .map(productsDTO -> new ResponseEntity<>(
                productsDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /productss/:id -> delete the "id" products.
     */
    @RequestMapping(value = "/productss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProducts(@PathVariable Long id) {
        log.debug("REST request to delete Products : {}", id);
        productsRepository.delete(id);
        productsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("products", id.toString())).build();
    }

    /**
     * SEARCH  /_search/productss/:query -> search for the products corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/productss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductsDTO> searchProductss(@PathVariable String query) {
        return StreamSupport
            .stream(productsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(productsMapper::productsToProductsDTO)
            .collect(Collectors.toList());
    }
}
