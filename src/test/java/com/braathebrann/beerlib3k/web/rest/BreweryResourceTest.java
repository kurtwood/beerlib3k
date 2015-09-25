package com.braathebrann.beerlib3k.web.rest;

import com.braathebrann.beerlib3k.Application;
import com.braathebrann.beerlib3k.domain.Brewery;
import com.braathebrann.beerlib3k.repository.BreweryRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the BreweryResource REST controller.
 *
 * @see BreweryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BreweryResourceTest {


    private static final Integer DEFAULT_BREWERY_ID = 1;
    private static final Integer UPDATED_BREWERY_ID = 2;
    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private BreweryRepository breweryRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBreweryMockMvc;

    private Brewery brewery;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BreweryResource breweryResource = new BreweryResource();
        ReflectionTestUtils.setField(breweryResource, "breweryRepository", breweryRepository);
        this.restBreweryMockMvc = MockMvcBuilders.standaloneSetup(breweryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        breweryRepository.deleteAll();
        brewery = new Brewery();
        brewery.setBrewery_id(DEFAULT_BREWERY_ID);
        brewery.setName(DEFAULT_NAME);
    }

    @Test
    public void createBrewery() throws Exception {
        int databaseSizeBeforeCreate = breweryRepository.findAll().size();

        // Create the Brewery

        restBreweryMockMvc.perform(post("/api/brewerys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(brewery)))
                .andExpect(status().isCreated());

        // Validate the Brewery in the database
        List<Brewery> brewerys = breweryRepository.findAll();
        assertThat(brewerys).hasSize(databaseSizeBeforeCreate + 1);
        Brewery testBrewery = brewerys.get(brewerys.size() - 1);
        assertThat(testBrewery.getBrewery_id()).isEqualTo(DEFAULT_BREWERY_ID);
        assertThat(testBrewery.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getAllBrewerys() throws Exception {
        // Initialize the database
        breweryRepository.save(brewery);

        // Get all the brewerys
        restBreweryMockMvc.perform(get("/api/brewerys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(brewery.getId())))
                .andExpect(jsonPath("$.[*].brewery_id").value(hasItem(DEFAULT_BREWERY_ID)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    public void getBrewery() throws Exception {
        // Initialize the database
        breweryRepository.save(brewery);

        // Get the brewery
        restBreweryMockMvc.perform(get("/api/brewerys/{id}", brewery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(brewery.getId()))
            .andExpect(jsonPath("$.brewery_id").value(DEFAULT_BREWERY_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    public void getNonExistingBrewery() throws Exception {
        // Get the brewery
        restBreweryMockMvc.perform(get("/api/brewerys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBrewery() throws Exception {
        // Initialize the database
        breweryRepository.save(brewery);

		int databaseSizeBeforeUpdate = breweryRepository.findAll().size();

        // Update the brewery
        brewery.setBrewery_id(UPDATED_BREWERY_ID);
        brewery.setName(UPDATED_NAME);
        

        restBreweryMockMvc.perform(put("/api/brewerys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(brewery)))
                .andExpect(status().isOk());

        // Validate the Brewery in the database
        List<Brewery> brewerys = breweryRepository.findAll();
        assertThat(brewerys).hasSize(databaseSizeBeforeUpdate);
        Brewery testBrewery = brewerys.get(brewerys.size() - 1);
        assertThat(testBrewery.getBrewery_id()).isEqualTo(UPDATED_BREWERY_ID);
        assertThat(testBrewery.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void deleteBrewery() throws Exception {
        // Initialize the database
        breweryRepository.save(brewery);

		int databaseSizeBeforeDelete = breweryRepository.findAll().size();

        // Get the brewery
        restBreweryMockMvc.perform(delete("/api/brewerys/{id}", brewery.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Brewery> brewerys = breweryRepository.findAll();
        assertThat(brewerys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
