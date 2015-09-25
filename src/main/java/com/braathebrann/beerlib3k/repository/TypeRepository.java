package com.braathebrann.beerlib3k.repository;

import com.braathebrann.beerlib3k.domain.Type;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Type entity.
 */
public interface TypeRepository extends MongoRepository<Type,String> {

}
