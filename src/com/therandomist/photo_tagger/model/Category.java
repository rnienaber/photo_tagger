package com.therandomist.photo_tagger.model;

import java.util.List;

public class Category {

    Long id;
    String name;
    List<Tag> tags;

    public Category(String name) {
        this.name = name;
    }

    public Category(Long id, String name){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public String toString(){
        return name;
    }
}
