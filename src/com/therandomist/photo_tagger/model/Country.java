package com.therandomist.photo_tagger.model;

import java.util.List;

public class Country {

    Long id;
    String name;
    List<Area> areas;

    public Country(String name) {
        this.name = name;
    }

    public Country(Long id, String name){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public Long getId() {
        return id;
    }

    public String toString(){
        return name;
    }
}
