package com.braathebrann.beerlib3k.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.braathebrann.beerlib3k.domain.Brewery;
import com.braathebrann.beerlib3k.repository.BreweryRepository;
import com.braathebrann.beerlib3k.web.rest.util.HeaderUtil;
import com.braathebrann.beerlib3k.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Brewery.
 */
@RestController
@RequestMapping("/api")
public class BreweryResource {

    private final Logger log = LoggerFactory.getLogger(BreweryResource.class);

    @Inject
    private BreweryRepository breweryRepository;

    /**
     * POST  /brewerys -> Create a new brewery.
     */
    @RequestMapping(value = "/brewerys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Brewery> createBrewery(@RequestBody Brewery brewery) throws URISyntaxException {
        log.debug("REST request to save Brewery : {}", brewery);
        if (brewery.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new brewery cannot already have an ID").body(null);
        }
        Brewery result = breweryRepository.save(brewery);
        return ResponseEntity.created(new URI("/api/brewerys/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("brewery", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /brewerys -> Updates an existing brewery.
     */
    @RequestMapping(value = "/brewerys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Brewery> updateBrewery(@RequestBody Brewery brewery) throws URISyntaxException {
        log.debug("REST request to update Brewery : {}", brewery);
        if (brewery.getId() == null) {
            return createBrewery(brewery);
        }
        Brewery result = breweryRepository.save(brewery);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("brewery", brewery.getId().toString()))
                .body(result);
    }

    /**
     * GET  /brewerys -> get all the brewerys.
     */
    @RequestMapping(value = "/brewerys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Brewery>> getAllBrewerys(Pageable pageable)
        throws URISyntaxException {
        Page<Brewery> page = breweryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/brewerys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /brewerys/:id -> get the "id" brewery.
     */
    @RequestMapping(value = "/brewerys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Brewery> getBrewery(@PathVariable String id) {
        log.debug("REST request to get Brewery : {}", id);
        return Optional.ofNullable(breweryRepository.findOne(id))
            .map(brewery -> new ResponseEntity<>(
                brewery,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /brewerys/:id -> delete the "id" brewery.
     */
    @RequestMapping(value = "/brewerys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBrewery(@PathVariable String id) {
        log.debug("REST request to delete Brewery : {}", id);
        breweryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("brewery", id.toString())).build();
    }
}
