package com.therandomist.photo_tagger.model;

import java.util.List;

public class Area {

    Long id;
    String name;
    List<Location> locations;
    Country country;
    Long countryId;

    public Area(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    public Area(Long id, String name, Long countryId){
        this.name = name;
        this.id = id;
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Long getId() {
        return id;
    }

    public String toString(){
        return name;
    }

    public Long getCountryId() {
        return countryId;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
