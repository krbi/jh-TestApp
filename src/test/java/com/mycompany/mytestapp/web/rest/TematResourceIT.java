package com.mycompany.mytestapp.web.rest;

import com.mycompany.mytestapp.JhTestApp;
import com.mycompany.mytestapp.domain.Temat;
import com.mycompany.mytestapp.repository.TematRepository;
import com.mycompany.mytestapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.mytestapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TematResource} REST controller.
 */
@SpringBootTest(classes = JhTestApp.class)
public class TematResourceIT {

    private static final String DEFAULT_NAZWA = "AAAAAAAAAA";
    private static final String UPDATED_NAZWA = "BBBBBBBBBB";

    @Autowired
    private TematRepository tematRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTematMockMvc;

    private Temat temat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TematResource tematResource = new TematResource(tematRepository);
        this.restTematMockMvc = MockMvcBuilders.standaloneSetup(tematResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Temat createEntity(EntityManager em) {
        Temat temat = new Temat()
            .nazwa(DEFAULT_NAZWA);
        return temat;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Temat createUpdatedEntity(EntityManager em) {
        Temat temat = new Temat()
            .nazwa(UPDATED_NAZWA);
        return temat;
    }

    @BeforeEach
    public void initTest() {
        temat = createEntity(em);
    }

    @Test
    @Transactional
    public void createTemat() throws Exception {
        int databaseSizeBeforeCreate = tematRepository.findAll().size();

        // Create the Temat
        restTematMockMvc.perform(post("/api/temats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(temat)))
            .andExpect(status().isCreated());

        // Validate the Temat in the database
        List<Temat> tematList = tematRepository.findAll();
        assertThat(tematList).hasSize(databaseSizeBeforeCreate + 1);
        Temat testTemat = tematList.get(tematList.size() - 1);
        assertThat(testTemat.getNazwa()).isEqualTo(DEFAULT_NAZWA);
    }

    @Test
    @Transactional
    public void createTematWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tematRepository.findAll().size();

        // Create the Temat with an existing ID
        temat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTematMockMvc.perform(post("/api/temats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(temat)))
            .andExpect(status().isBadRequest());

        // Validate the Temat in the database
        List<Temat> tematList = tematRepository.findAll();
        assertThat(tematList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTemats() throws Exception {
        // Initialize the database
        tematRepository.saveAndFlush(temat);

        // Get all the tematList
        restTematMockMvc.perform(get("/api/temats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(temat.getId().intValue())))
            .andExpect(jsonPath("$.[*].nazwa").value(hasItem(DEFAULT_NAZWA)));
    }
    
    @Test
    @Transactional
    public void getTemat() throws Exception {
        // Initialize the database
        tematRepository.saveAndFlush(temat);

        // Get the temat
        restTematMockMvc.perform(get("/api/temats/{id}", temat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(temat.getId().intValue()))
            .andExpect(jsonPath("$.nazwa").value(DEFAULT_NAZWA));
    }

    @Test
    @Transactional
    public void getNonExistingTemat() throws Exception {
        // Get the temat
        restTematMockMvc.perform(get("/api/temats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTemat() throws Exception {
        // Initialize the database
        tematRepository.saveAndFlush(temat);

        int databaseSizeBeforeUpdate = tematRepository.findAll().size();

        // Update the temat
        Temat updatedTemat = tematRepository.findById(temat.getId()).get();
        // Disconnect from session so that the updates on updatedTemat are not directly saved in db
        em.detach(updatedTemat);
        updatedTemat
            .nazwa(UPDATED_NAZWA);

        restTematMockMvc.perform(put("/api/temats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTemat)))
            .andExpect(status().isOk());

        // Validate the Temat in the database
        List<Temat> tematList = tematRepository.findAll();
        assertThat(tematList).hasSize(databaseSizeBeforeUpdate);
        Temat testTemat = tematList.get(tematList.size() - 1);
        assertThat(testTemat.getNazwa()).isEqualTo(UPDATED_NAZWA);
    }

    @Test
    @Transactional
    public void updateNonExistingTemat() throws Exception {
        int databaseSizeBeforeUpdate = tematRepository.findAll().size();

        // Create the Temat

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTematMockMvc.perform(put("/api/temats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(temat)))
            .andExpect(status().isBadRequest());

        // Validate the Temat in the database
        List<Temat> tematList = tematRepository.findAll();
        assertThat(tematList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTemat() throws Exception {
        // Initialize the database
        tematRepository.saveAndFlush(temat);

        int databaseSizeBeforeDelete = tematRepository.findAll().size();

        // Delete the temat
        restTematMockMvc.perform(delete("/api/temats/{id}", temat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Temat> tematList = tematRepository.findAll();
        assertThat(tematList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
