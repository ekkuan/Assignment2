package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.SalesPerson;

import com.mycompany.myapp.repository.SalesPersonRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SalesPerson.
 */
@RestController
@RequestMapping("/api")
public class SalesPersonResource {

    private final Logger log = LoggerFactory.getLogger(SalesPersonResource.class);
        
    @Inject
    private SalesPersonRepository salesPersonRepository;

    /**
     * POST  /sales-people : Create a new salesPerson.
     *
     * @param salesPerson the salesPerson to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salesPerson, or with status 400 (Bad Request) if the salesPerson has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sales-people")
    @Timed
    public ResponseEntity<SalesPerson> createSalesPerson(@RequestBody SalesPerson salesPerson) throws URISyntaxException {
        log.debug("REST request to save SalesPerson : {}", salesPerson);
        if (salesPerson.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("salesPerson", "idexists", "A new salesPerson cannot already have an ID")).body(null);
        }
        SalesPerson result = salesPersonRepository.save(salesPerson);
        return ResponseEntity.created(new URI("/api/sales-people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("salesPerson", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sales-people : Updates an existing salesPerson.
     *
     * @param salesPerson the salesPerson to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salesPerson,
     * or with status 400 (Bad Request) if the salesPerson is not valid,
     * or with status 500 (Internal Server Error) if the salesPerson couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sales-people")
    @Timed
    public ResponseEntity<SalesPerson> updateSalesPerson(@RequestBody SalesPerson salesPerson) throws URISyntaxException {
        log.debug("REST request to update SalesPerson : {}", salesPerson);
        if (salesPerson.getId() == null) {
            return createSalesPerson(salesPerson);
        }
        SalesPerson result = salesPersonRepository.save(salesPerson);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("salesPerson", salesPerson.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sales-people : get all the salesPeople.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of salesPeople in body
     */
    @GetMapping("/sales-people")
    @Timed
    public List<SalesPerson> getAllSalesPeople() {
        log.debug("REST request to get all SalesPeople");
        List<SalesPerson> salesPeople = salesPersonRepository.findAll();
        return salesPeople;
    }

    /**
     * GET  /sales-people/:id : get the "id" salesPerson.
     *
     * @param id the id of the salesPerson to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salesPerson, or with status 404 (Not Found)
     */
    @GetMapping("/sales-people/{id}")
    @Timed
    public ResponseEntity<SalesPerson> getSalesPerson(@PathVariable Long id) {
        log.debug("REST request to get SalesPerson : {}", id);
        SalesPerson salesPerson = salesPersonRepository.findOne(id);
        return Optional.ofNullable(salesPerson)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sales-people/:id : delete the "id" salesPerson.
     *
     * @param id the id of the salesPerson to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sales-people/{id}")
    @Timed
    public ResponseEntity<Void> deleteSalesPerson(@PathVariable Long id) {
        log.debug("REST request to delete SalesPerson : {}", id);
        salesPersonRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("salesPerson", id.toString())).build();
    }

}
