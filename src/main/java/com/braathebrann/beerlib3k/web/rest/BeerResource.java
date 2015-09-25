package com.braathebrann.beerlib3k.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.braathebrann.beerlib3k.domain.Beer;
import com.braathebrann.beerlib3k.repository.BeerRepository;
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
 * REST controller for managing Beer.
 */
@RestController
@RequestMapping("/api")
public class BeerResource {

    private final Logger log = LoggerFactory.getLogger(BeerResource.class);

    @Inject
    private BeerRepository beerRepository;

    /**
     * POST  /beers -> Create a new beer.
     */
    @RequestMapping(value = "/beers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Beer> createBeer(@RequestBody Beer beer) throws URISyntaxException {
        log.debug("REST request to save Beer : {}", beer);
        if (beer.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new beer cannot already have an ID").body(null);
        }
        Beer result = beerRepository.save(beer);
        return ResponseEntity.created(new URI("/api/beers/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("beer", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /beers -> Updates an existing beer.
     */
    @RequestMapping(value = "/beers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Beer> updateBeer(@RequestBody Beer beer) throws URISyntaxException {
        log.debug("REST request to update Beer : {}", beer);
        if (beer.getId() == null) {
            return createBeer(beer);
        }
        Beer result = beerRepository.save(beer);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("beer", beer.getId().toString()))
                .body(result);
    }

    /**
     * GET  /beers -> get all the beers.
     */
    @RequestMapping(value = "/beers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Beer>> getAllBeers(Pageable pageable)
        throws URISyntaxException {
        Page<Beer> page = beerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/beers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /beers/:id -> get the "id" beer.
     */
    @RequestMapping(value = "/beers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Beer> getBeer(@PathVariable String id) {
        log.debug("REST request to get Beer : {}", id);
        return Optional.ofNullable(beerRepository.findOne(id))
            .map(beer -> new ResponseEntity<>(
                beer,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /beers/:id -> delete the "id" beer.
     */
    @RequestMapping(value = "/beers/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBeer(@PathVariable String id) {
        log.debug("REST request to delete Beer : {}", id);
        beerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("beer", id.toString())).build();
    }
}
