package com.therandomist.photo_tagger.model;

import java.util.Collections;
import java.util.List;

public class Tag implements Comparable{

    Long id;
    String name;
    Category category;
    Long categoryId;

    public Tag(Long id, String name, Category category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public Tag(Long id, String name, Long categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }

    public Tag(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static String getString(List<Tag> tags){
        String result = "";

        if(tags != null){
            for(Tag tag : tags){
                if (tag != null){
                    if(result.length() == 0){
                        result = tag.getName();
                    }else{
                        result += ", "+tag.getName();
                    }
                }
            }
        }
        return result;
    }

    public Long getId() {
        return id;
    }

    public static int[] getIds(List<Tag> tags){

        if(tags != null){
            Collections.sort(tags);
            int[] result = new int[tags.size()];

            int i=0;
            for(Tag tag : tags){
                result[i++] = tag.getId().intValue();
            }

            return result;
        }else{
            return new int[0];
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (categoryId != null ? !categoryId.equals(tag.categoryId) : tag.categoryId != null) return false;
        if (id != null ? !id.equals(tag.id) : tag.id != null) return false;
        if (!name.equals(tag.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Object o) {
        Tag tag = (Tag)o;
        return this.getName().compareTo(tag.getName());
    }
}
