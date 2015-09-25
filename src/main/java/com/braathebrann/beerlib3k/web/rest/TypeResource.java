package com.braathebrann.beerlib3k.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.braathebrann.beerlib3k.domain.Type;
import com.braathebrann.beerlib3k.repository.TypeRepository;
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
 * REST controller for managing Type.
 */
@RestController
@RequestMapping("/api")
public class TypeResource {

    private final Logger log = LoggerFactory.getLogger(TypeResource.class);

    @Inject
    private TypeRepository typeRepository;

    /**
     * POST  /types -> Create a new type.
     */
    @RequestMapping(value = "/types",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Type> createType(@RequestBody Type type) throws URISyntaxException {
        log.debug("REST request to save Type : {}", type);
        if (type.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new type cannot already have an ID").body(null);
        }
        Type result = typeRepository.save(type);
        return ResponseEntity.created(new URI("/api/types/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("type", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /types -> Updates an existing type.
     */
    @RequestMapping(value = "/types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Type> updateType(@RequestBody Type type) throws URISyntaxException {
        log.debug("REST request to update Type : {}", type);
        if (type.getId() == null) {
            return createType(type);
        }
        Type result = typeRepository.save(type);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("type", type.getId().toString()))
                .body(result);
    }

    /**
     * GET  /types -> get all the types.
     */
    @RequestMapping(value = "/types",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Type>> getAllTypes(Pageable pageable)
        throws URISyntaxException {
        Page<Type> page = typeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /types/:id -> get the "id" type.
     */
    @RequestMapping(value = "/types/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Type> getType(@PathVariable String id) {
        log.debug("REST request to get Type : {}", id);
        return Optional.ofNullable(typeRepository.findOne(id))
            .map(type -> new ResponseEntity<>(
                type,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /types/:id -> delete the "id" type.
     */
    @RequestMapping(value = "/types/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteType(@PathVariable String id) {
        log.debug("REST request to delete Type : {}", id);
        typeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("type", id.toString())).build();
    }
}
