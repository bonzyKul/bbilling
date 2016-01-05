package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.CustomerAddress;
import com.barclays.bbilling.repository.CustomerAddressRepository;
import com.barclays.bbilling.repository.search.CustomerAddressSearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.CustomerAddressDTO;
import com.barclays.bbilling.web.rest.mapper.CustomerAddressMapper;
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
 * REST controller for managing CustomerAddress.
 */
@RestController
@RequestMapping("/api")
public class CustomerAddressResource {

    private final Logger log = LoggerFactory.getLogger(CustomerAddressResource.class);

    @Inject
    private CustomerAddressRepository customerAddressRepository;

    @Inject
    private CustomerAddressMapper customerAddressMapper;

    @Inject
    private CustomerAddressSearchRepository customerAddressSearchRepository;

    /**
     * POST  /customerAddresss -> Create a new customerAddress.
     */
    @RequestMapping(value = "/customerAddresss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerAddressDTO> createCustomerAddress(@Valid @RequestBody CustomerAddressDTO customerAddressDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerAddress : {}", customerAddressDTO);
        if (customerAddressDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new customerAddress cannot already have an ID").body(null);
        }
        CustomerAddress customerAddress = customerAddressMapper.customerAddressDTOToCustomerAddress(customerAddressDTO);
        CustomerAddress result = customerAddressRepository.save(customerAddress);
        customerAddressSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/customerAddresss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("customerAddress", result.getId().toString()))
            .body(customerAddressMapper.customerAddressToCustomerAddressDTO(result));
    }

    /**
     * PUT  /customerAddresss -> Updates an existing customerAddress.
     */
    @RequestMapping(value = "/customerAddresss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerAddressDTO> updateCustomerAddress(@Valid @RequestBody CustomerAddressDTO customerAddressDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerAddress : {}", customerAddressDTO);
        if (customerAddressDTO.getId() == null) {
            return createCustomerAddress(customerAddressDTO);
        }
        CustomerAddress customerAddress = customerAddressMapper.customerAddressDTOToCustomerAddress(customerAddressDTO);
        CustomerAddress result = customerAddressRepository.save(customerAddress);
        customerAddressSearchRepository.save(customerAddress);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("customerAddress", customerAddressDTO.getId().toString()))
            .body(customerAddressMapper.customerAddressToCustomerAddressDTO(result));
    }

    /**
     * GET  /customerAddresss -> get all the customerAddresss.
     */
    @RequestMapping(value = "/customerAddresss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CustomerAddressDTO>> getAllCustomerAddresss(Pageable pageable)
        throws URISyntaxException {
        Page<CustomerAddress> page = customerAddressRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customerAddresss");
        return new ResponseEntity<>(page.getContent().stream()
            .map(customerAddressMapper::customerAddressToCustomerAddressDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /customerAddresss/:id -> get the "id" customerAddress.
     */
    @RequestMapping(value = "/customerAddresss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerAddressDTO> getCustomerAddress(@PathVariable Long id) {
        log.debug("REST request to get CustomerAddress : {}", id);
        return Optional.ofNullable(customerAddressRepository.findOne(id))
            .map(customerAddressMapper::customerAddressToCustomerAddressDTO)
            .map(customerAddressDTO -> new ResponseEntity<>(
                customerAddressDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customerAddresss/:id -> delete the "id" customerAddress.
     */
    @RequestMapping(value = "/customerAddresss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCustomerAddress(@PathVariable Long id) {
        log.debug("REST request to delete CustomerAddress : {}", id);
        customerAddressRepository.delete(id);
        customerAddressSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customerAddress", id.toString())).build();
    }

    /**
     * SEARCH  /_search/customerAddresss/:query -> search for the customerAddress corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/customerAddresss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CustomerAddressDTO> searchCustomerAddresss(@PathVariable String query) {
        return StreamSupport
            .stream(customerAddressSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(customerAddressMapper::customerAddressToCustomerAddressDTO)
            .collect(Collectors.toList());
    }
}
