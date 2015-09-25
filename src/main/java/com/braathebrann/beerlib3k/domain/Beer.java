package com.braathebrann.beerlib3k.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Beer.
 */
@Document(collection = "BEER")
public class Beer implements Serializable {

    @Id
    private String id;
    
    @Field("beer_id")
    private Integer beer_id;
    
    @Field("name")
    private String name;
    
    @Field("description")
    private String description;
    
    @Field("type_id")
    private Integer type_id;
    
    @Field("brewery_id")
    private Integer brewery_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getBeer_id() {
        return beer_id;
    }

    public void setBeer_id(Integer beer_id) {
        this.beer_id = beer_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    public Integer getBrewery_id() {
        return brewery_id;
    }

    public void setBrewery_id(Integer brewery_id) {
        this.brewery_id = brewery_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Beer beer = (Beer) o;

        if ( ! Objects.equals(id, beer.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Beer{" +
                "id=" + id +
                ", beer_id='" + beer_id + "'" +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                ", type_id='" + type_id + "'" +
                ", brewery_id='" + brewery_id + "'" +
                '}';
    }
}
