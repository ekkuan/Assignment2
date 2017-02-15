package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Assignment2App;

import com.mycompany.myapp.domain.SalesPerson;
import com.mycompany.myapp.repository.SalesPersonRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SalesPersonResource REST controller.
 *
 * @see SalesPersonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Assignment2App.class)
public class SalesPersonResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CUST_ID = 1;
    private static final Integer UPDATED_CUST_ID = 2;

    @Inject
    private SalesPersonRepository salesPersonRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSalesPersonMockMvc;

    private SalesPerson salesPerson;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SalesPersonResource salesPersonResource = new SalesPersonResource();
        ReflectionTestUtils.setField(salesPersonResource, "salesPersonRepository", salesPersonRepository);
        this.restSalesPersonMockMvc = MockMvcBuilders.standaloneSetup(salesPersonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesPerson createEntity(EntityManager em) {
        SalesPerson salesPerson = new SalesPerson()
                .name(DEFAULT_NAME)
                .cust_id(DEFAULT_CUST_ID);
        return salesPerson;
    }

    @Before
    public void initTest() {
        salesPerson = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalesPerson() throws Exception {
        int databaseSizeBeforeCreate = salesPersonRepository.findAll().size();

        // Create the SalesPerson

        restSalesPersonMockMvc.perform(post("/api/sales-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesPerson)))
            .andExpect(status().isCreated());

        // Validate the SalesPerson in the database
        List<SalesPerson> salesPersonList = salesPersonRepository.findAll();
        assertThat(salesPersonList).hasSize(databaseSizeBeforeCreate + 1);
        SalesPerson testSalesPerson = salesPersonList.get(salesPersonList.size() - 1);
        assertThat(testSalesPerson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSalesPerson.getCust_id()).isEqualTo(DEFAULT_CUST_ID);
    }

    @Test
    @Transactional
    public void createSalesPersonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salesPersonRepository.findAll().size();

        // Create the SalesPerson with an existing ID
        SalesPerson existingSalesPerson = new SalesPerson();
        existingSalesPerson.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesPersonMockMvc.perform(post("/api/sales-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSalesPerson)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SalesPerson> salesPersonList = salesPersonRepository.findAll();
        assertThat(salesPersonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSalesPeople() throws Exception {
        // Initialize the database
        salesPersonRepository.saveAndFlush(salesPerson);

        // Get all the salesPersonList
        restSalesPersonMockMvc.perform(get("/api/sales-people?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesPerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].cust_id").value(hasItem(DEFAULT_CUST_ID)));
    }

    @Test
    @Transactional
    public void getSalesPerson() throws Exception {
        // Initialize the database
        salesPersonRepository.saveAndFlush(salesPerson);

        // Get the salesPerson
        restSalesPersonMockMvc.perform(get("/api/sales-people/{id}", salesPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(salesPerson.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.cust_id").value(DEFAULT_CUST_ID));
    }

    @Test
    @Transactional
    public void getNonExistingSalesPerson() throws Exception {
        // Get the salesPerson
        restSalesPersonMockMvc.perform(get("/api/sales-people/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalesPerson() throws Exception {
        // Initialize the database
        salesPersonRepository.saveAndFlush(salesPerson);
        int databaseSizeBeforeUpdate = salesPersonRepository.findAll().size();

        // Update the salesPerson
        SalesPerson updatedSalesPerson = salesPersonRepository.findOne(salesPerson.getId());
        updatedSalesPerson
                .name(UPDATED_NAME)
                .cust_id(UPDATED_CUST_ID);

        restSalesPersonMockMvc.perform(put("/api/sales-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSalesPerson)))
            .andExpect(status().isOk());

        // Validate the SalesPerson in the database
        List<SalesPerson> salesPersonList = salesPersonRepository.findAll();
        assertThat(salesPersonList).hasSize(databaseSizeBeforeUpdate);
        SalesPerson testSalesPerson = salesPersonList.get(salesPersonList.size() - 1);
        assertThat(testSalesPerson.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSalesPerson.getCust_id()).isEqualTo(UPDATED_CUST_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingSalesPerson() throws Exception {
        int databaseSizeBeforeUpdate = salesPersonRepository.findAll().size();

        // Create the SalesPerson

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalesPersonMockMvc.perform(put("/api/sales-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesPerson)))
            .andExpect(status().isCreated());

        // Validate the SalesPerson in the database
        List<SalesPerson> salesPersonList = salesPersonRepository.findAll();
        assertThat(salesPersonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSalesPerson() throws Exception {
        // Initialize the database
        salesPersonRepository.saveAndFlush(salesPerson);
        int databaseSizeBeforeDelete = salesPersonRepository.findAll().size();

        // Get the salesPerson
        restSalesPersonMockMvc.perform(delete("/api/sales-people/{id}", salesPerson.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SalesPerson> salesPersonList = salesPersonRepository.findAll();
        assertThat(salesPersonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
