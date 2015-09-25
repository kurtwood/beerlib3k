package com.braathebrann.beerlib3k.repository;

import com.braathebrann.beerlib3k.domain.Beer;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Beer entity.
 */
public interface BeerRepository extends MongoRepository<Beer,String> {

}
