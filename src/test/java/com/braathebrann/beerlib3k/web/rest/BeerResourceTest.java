package com.braathebrann.beerlib3k.web.rest;

import com.braathebrann.beerlib3k.Application;
import com.braathebrann.beerlib3k.domain.Beer;
import com.braathebrann.beerlib3k.repository.BeerRepository;

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
 * Test class for the BeerResource REST controller.
 *
 * @see BeerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BeerResourceTest {


    private static final Integer DEFAULT_BEER_ID = 1;
    private static final Integer UPDATED_BEER_ID = 2;
    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final Integer DEFAULT_TYPE_ID = 1;
    private static final Integer UPDATED_TYPE_ID = 2;

    private static final Integer DEFAULT_BREWERY_ID = 1;
    private static final Integer UPDATED_BREWERY_ID = 2;

    @Inject
    private BeerRepository beerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBeerMockMvc;

    private Beer beer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BeerResource beerResource = new BeerResource();
        ReflectionTestUtils.setField(beerResource, "beerRepository", beerRepository);
        this.restBeerMockMvc = MockMvcBuilders.standaloneSetup(beerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        beerRepository.deleteAll();
        beer = new Beer();
        beer.setBeer_id(DEFAULT_BEER_ID);
        beer.setName(DEFAULT_NAME);
        beer.setDescription(DEFAULT_DESCRIPTION);
        beer.setType_id(DEFAULT_TYPE_ID);
        beer.setBrewery_id(DEFAULT_BREWERY_ID);
    }

    @Test
    public void createBeer() throws Exception {
        int databaseSizeBeforeCreate = beerRepository.findAll().size();

        // Create the Beer

        restBeerMockMvc.perform(post("/api/beers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(beer)))
                .andExpect(status().isCreated());

        // Validate the Beer in the database
        List<Beer> beers = beerRepository.findAll();
        assertThat(beers).hasSize(databaseSizeBeforeCreate + 1);
        Beer testBeer = beers.get(beers.size() - 1);
        assertThat(testBeer.getBeer_id()).isEqualTo(DEFAULT_BEER_ID);
        assertThat(testBeer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBeer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBeer.getType_id()).isEqualTo(DEFAULT_TYPE_ID);
        assertThat(testBeer.getBrewery_id()).isEqualTo(DEFAULT_BREWERY_ID);
    }

    @Test
    public void getAllBeers() throws Exception {
        // Initialize the database
        beerRepository.save(beer);

        // Get all the beers
        restBeerMockMvc.perform(get("/api/beers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(beer.getId())))
                .andExpect(jsonPath("$.[*].beer_id").value(hasItem(DEFAULT_BEER_ID)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].type_id").value(hasItem(DEFAULT_TYPE_ID)))
                .andExpect(jsonPath("$.[*].brewery_id").value(hasItem(DEFAULT_BREWERY_ID)));
    }

    @Test
    public void getBeer() throws Exception {
        // Initialize the database
        beerRepository.save(beer);

        // Get the beer
        restBeerMockMvc.perform(get("/api/beers/{id}", beer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(beer.getId()))
            .andExpect(jsonPath("$.beer_id").value(DEFAULT_BEER_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type_id").value(DEFAULT_TYPE_ID))
            .andExpect(jsonPath("$.brewery_id").value(DEFAULT_BREWERY_ID));
    }

    @Test
    public void getNonExistingBeer() throws Exception {
        // Get the beer
        restBeerMockMvc.perform(get("/api/beers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBeer() throws Exception {
        // Initialize the database
        beerRepository.save(beer);

		int databaseSizeBeforeUpdate = beerRepository.findAll().size();

        // Update the beer
        beer.setBeer_id(UPDATED_BEER_ID);
        beer.setName(UPDATED_NAME);
        beer.setDescription(UPDATED_DESCRIPTION);
        beer.setType_id(UPDATED_TYPE_ID);
        beer.setBrewery_id(UPDATED_BREWERY_ID);
        

        restBeerMockMvc.perform(put("/api/beers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(beer)))
                .andExpect(status().isOk());

        // Validate the Beer in the database
        List<Beer> beers = beerRepository.findAll();
        assertThat(beers).hasSize(databaseSizeBeforeUpdate);
        Beer testBeer = beers.get(beers.size() - 1);
        assertThat(testBeer.getBeer_id()).isEqualTo(UPDATED_BEER_ID);
        assertThat(testBeer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBeer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBeer.getType_id()).isEqualTo(UPDATED_TYPE_ID);
        assertThat(testBeer.getBrewery_id()).isEqualTo(UPDATED_BREWERY_ID);
    }

    @Test
    public void deleteBeer() throws Exception {
        // Initialize the database
        beerRepository.save(beer);

		int databaseSizeBeforeDelete = beerRepository.findAll().size();

        // Get the beer
        restBeerMockMvc.perform(delete("/api/beers/{id}", beer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Beer> beers = beerRepository.findAll();
        assertThat(beers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
