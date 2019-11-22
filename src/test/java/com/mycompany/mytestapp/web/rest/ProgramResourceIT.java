package com.mycompany.mytestapp.web.rest;

import com.mycompany.mytestapp.JhTestApp;
import com.mycompany.mytestapp.domain.Program;
import com.mycompany.mytestapp.repository.ProgramRepository;
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
 * Integration tests for the {@link ProgramResource} REST controller.
 */
@SpringBootTest(classes = JhTestApp.class)
public class ProgramResourceIT {

    private static final String DEFAULT_NAZWA = "AAAAAAAAAA";
    private static final String UPDATED_NAZWA = "BBBBBBBBBB";

    private static final String DEFAULT_OPIS = "AAAAAAAAAA";
    private static final String UPDATED_OPIS = "BBBBBBBBBB";

    @Autowired
    private ProgramRepository programRepository;

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

    private MockMvc restProgramMockMvc;

    private Program program;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProgramResource programResource = new ProgramResource(programRepository);
        this.restProgramMockMvc = MockMvcBuilders.standaloneSetup(programResource)
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
    public static Program createEntity(EntityManager em) {
        Program program = new Program()
            .nazwa(DEFAULT_NAZWA)
            .opis(DEFAULT_OPIS);
        return program;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Program createUpdatedEntity(EntityManager em) {
        Program program = new Program()
            .nazwa(UPDATED_NAZWA)
            .opis(UPDATED_OPIS);
        return program;
    }

    @BeforeEach
    public void initTest() {
        program = createEntity(em);
    }

    @Test
    @Transactional
    public void createProgram() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // Create the Program
        restProgramMockMvc.perform(post("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isCreated());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate + 1);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getNazwa()).isEqualTo(DEFAULT_NAZWA);
        assertThat(testProgram.getOpis()).isEqualTo(DEFAULT_OPIS);
    }

    @Test
    @Transactional
    public void createProgramWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // Create the Program with an existing ID
        program.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramMockMvc.perform(post("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPrograms() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList
        restProgramMockMvc.perform(get("/api/programs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
            .andExpect(jsonPath("$.[*].nazwa").value(hasItem(DEFAULT_NAZWA)))
            .andExpect(jsonPath("$.[*].opis").value(hasItem(DEFAULT_OPIS)));
    }
    
    @Test
    @Transactional
    public void getProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", program.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(program.getId().intValue()))
            .andExpect(jsonPath("$.nazwa").value(DEFAULT_NAZWA))
            .andExpect(jsonPath("$.opis").value(DEFAULT_OPIS));
    }

    @Test
    @Transactional
    public void getNonExistingProgram() throws Exception {
        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Update the program
        Program updatedProgram = programRepository.findById(program.getId()).get();
        // Disconnect from session so that the updates on updatedProgram are not directly saved in db
        em.detach(updatedProgram);
        updatedProgram
            .nazwa(UPDATED_NAZWA)
            .opis(UPDATED_OPIS);

        restProgramMockMvc.perform(put("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProgram)))
            .andExpect(status().isOk());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getNazwa()).isEqualTo(UPDATED_NAZWA);
        assertThat(testProgram.getOpis()).isEqualTo(UPDATED_OPIS);
    }

    @Test
    @Transactional
    public void updateNonExistingProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Create the Program

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramMockMvc.perform(put("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        int databaseSizeBeforeDelete = programRepository.findAll().size();

        // Delete the program
        restProgramMockMvc.perform(delete("/api/programs/{id}", program.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
