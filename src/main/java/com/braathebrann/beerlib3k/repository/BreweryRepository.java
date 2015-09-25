package com.braathebrann.beerlib3k.repository;

import com.braathebrann.beerlib3k.domain.Brewery;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Brewery entity.
 */
public interface BreweryRepository extends MongoRepository<Brewery,String> {

}
