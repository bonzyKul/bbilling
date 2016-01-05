package com.barclays.bbilling.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.barclays.bbilling.domain.Holiday;
import com.barclays.bbilling.repository.HolidayRepository;
import com.barclays.bbilling.repository.search.HolidaySearchRepository;
import com.barclays.bbilling.web.rest.util.HeaderUtil;
import com.barclays.bbilling.web.rest.util.PaginationUtil;
import com.barclays.bbilling.web.rest.dto.HolidayDTO;
import com.barclays.bbilling.web.rest.mapper.HolidayMapper;
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
 * REST controller for managing Holiday.
 */
@RestController
@RequestMapping("/api")
public class HolidayResource {

    private final Logger log = LoggerFactory.getLogger(HolidayResource.class);

    @Inject
    private HolidayRepository holidayRepository;

    @Inject
    private HolidayMapper holidayMapper;

    @Inject
    private HolidaySearchRepository holidaySearchRepository;

    /**
     * POST  /holidays -> Create a new holiday.
     */
    @RequestMapping(value = "/holidays",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HolidayDTO> createHoliday(@Valid @RequestBody HolidayDTO holidayDTO) throws URISyntaxException {
        log.debug("REST request to save Holiday : {}", holidayDTO);
        if (holidayDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new holiday cannot already have an ID").body(null);
        }
        Holiday holiday = holidayMapper.holidayDTOToHoliday(holidayDTO);
        Holiday result = holidayRepository.save(holiday);
        holidaySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/holidays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("holiday", result.getId().toString()))
            .body(holidayMapper.holidayToHolidayDTO(result));
    }

    /**
     * PUT  /holidays -> Updates an existing holiday.
     */
    @RequestMapping(value = "/holidays",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HolidayDTO> updateHoliday(@Valid @RequestBody HolidayDTO holidayDTO) throws URISyntaxException {
        log.debug("REST request to update Holiday : {}", holidayDTO);
        if (holidayDTO.getId() == null) {
            return createHoliday(holidayDTO);
        }
        Holiday holiday = holidayMapper.holidayDTOToHoliday(holidayDTO);
        Holiday result = holidayRepository.save(holiday);
        holidaySearchRepository.save(holiday);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("holiday", holidayDTO.getId().toString()))
            .body(holidayMapper.holidayToHolidayDTO(result));
    }

    /**
     * GET  /holidays -> get all the holidays.
     */
    @RequestMapping(value = "/holidays",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<HolidayDTO>> getAllHolidays(Pageable pageable)
        throws URISyntaxException {
        Page<Holiday> page = holidayRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/holidays");
        return new ResponseEntity<>(page.getContent().stream()
            .map(holidayMapper::holidayToHolidayDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /holidays/:id -> get the "id" holiday.
     */
    @RequestMapping(value = "/holidays/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HolidayDTO> getHoliday(@PathVariable Long id) {
        log.debug("REST request to get Holiday : {}", id);
        return Optional.ofNullable(holidayRepository.findOne(id))
            .map(holidayMapper::holidayToHolidayDTO)
            .map(holidayDTO -> new ResponseEntity<>(
                holidayDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /holidays/:id -> delete the "id" holiday.
     */
    @RequestMapping(value = "/holidays/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHoliday(@PathVariable Long id) {
        log.debug("REST request to delete Holiday : {}", id);
        holidayRepository.delete(id);
        holidaySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("holiday", id.toString())).build();
    }

    /**
     * SEARCH  /_search/holidays/:query -> search for the holiday corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/holidays/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HolidayDTO> searchHolidays(@PathVariable String query) {
        return StreamSupport
            .stream(holidaySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(holidayMapper::holidayToHolidayDTO)
            .collect(Collectors.toList());
    }
}
