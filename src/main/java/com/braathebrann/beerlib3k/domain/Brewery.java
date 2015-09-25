package com.braathebrann.beerlib3k.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Brewery.
 */
@Document(collection = "BREWERY")
public class Brewery implements Serializable {

    @Id
    private String id;
    
    @Field("brewery_id")
    private Integer brewery_id;
    
    @Field("name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getBrewery_id() {
        return brewery_id;
    }

    public void setBrewery_id(Integer brewery_id) {
        this.brewery_id = brewery_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Brewery brewery = (Brewery) o;

        if ( ! Objects.equals(id, brewery.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Brewery{" +
                "id=" + id +
                ", brewery_id='" + brewery_id + "'" +
                ", name='" + name + "'" +
                '}';
    }
}
