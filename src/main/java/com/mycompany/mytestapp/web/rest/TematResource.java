package com.mycompany.mytestapp.web.rest;

import com.mycompany.mytestapp.domain.Temat;
import com.mycompany.mytestapp.repository.TematRepository;
import com.mycompany.mytestapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.mytestapp.domain.Temat}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TematResource {

    private final Logger log = LoggerFactory.getLogger(TematResource.class);

    private static final String ENTITY_NAME = "temat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TematRepository tematRepository;

    public TematResource(TematRepository tematRepository) {
        this.tematRepository = tematRepository;
    }

    /**
     * {@code POST  /temats} : Create a new temat.
     *
     * @param temat the temat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new temat, or with status {@code 400 (Bad Request)} if the temat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/temats")
    public ResponseEntity<Temat> createTemat(@RequestBody Temat temat) throws URISyntaxException {
        log.debug("REST request to save Temat : {}", temat);
        if (temat.getId() != null) {
            throw new BadRequestAlertException("A new temat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Temat result = tematRepository.save(temat);
        return ResponseEntity.created(new URI("/api/temats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /temats} : Updates an existing temat.
     *
     * @param temat the temat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated temat,
     * or with status {@code 400 (Bad Request)} if the temat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the temat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/temats")
    public ResponseEntity<Temat> updateTemat(@RequestBody Temat temat) throws URISyntaxException {
        log.debug("REST request to update Temat : {}", temat);
        if (temat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Temat result = tematRepository.save(temat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, temat.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /temats} : get all the temats.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of temats in body.
     */
    @GetMapping("/temats")
    public ResponseEntity<List<Temat>> getAllTemats(Pageable pageable) {
        log.debug("REST request to get a page of Temats");
        Page<Temat> page = tematRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /temats/:id} : get the "id" temat.
     *
     * @param id the id of the temat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the temat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/temats/{id}")
    public ResponseEntity<Temat> getTemat(@PathVariable Long id) {
        log.debug("REST request to get Temat : {}", id);
        Optional<Temat> temat = tematRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(temat);
    }

    /**
     * {@code DELETE  /temats/:id} : delete the "id" temat.
     *
     * @param id the id of the temat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/temats/{id}")
    public ResponseEntity<Void> deleteTemat(@PathVariable Long id) {
        log.debug("REST request to delete Temat : {}", id);
        tematRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
