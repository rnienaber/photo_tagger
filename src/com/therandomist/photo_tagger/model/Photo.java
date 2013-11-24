package com.therandomist.photo_tagger.model;

import com.therandomist.photo_tagger.service.FileService;

import java.util.ArrayList;
import java.util.List;

public class Photo {

    Long id;
    String filename;
    String folder;
    GPSLocation location;
    List<Tag> people;
    List<Tag> keywords;
    List<Tag> printing;
    List<Tag> other;

    public Photo(Long id, String filename, String folder, GPSLocation location, List<Tag> people, List<Tag> keywords, List<Tag> printing, List<Tag> other) {
        this.id = id;
        this.filename = filename;
        this.folder = folder;
        this.location = location;
        this.people = people;
        this.keywords = keywords;
        this.printing = printing;
        this.other = other;
    }

    public Photo(String filename, String folder, GPSLocation location, List<Tag> people, List<Tag> keywords, List<Tag> printing, List<Tag> other) {
        this.filename = filename;
        this.folder = folder;
        this.location = location;
        this.people = people;
        this.keywords = keywords;
        this.printing = printing;
        this.other = other;
    }

    public Photo(){
    }

    public void setPath(String path){
        folder = FileService.getFolder(path);
        filename = FileService.getFilename(path);
    }

    public String getFilename() {
        return filename;
    }

    public String getFolder() {
        return folder;
    }

    public Double getLatitude() {
        return location != null ? location.getLatitude() : new Double(0);
    }

    public Double getLongitude() {
        return location != null ? location.getLongitude() : new Double(0);
    }

    public String getPeople(){
        return Tag.getString(people);
    }

    public String getKeywords(){
        List<Tag> allKeywords = new ArrayList<Tag>();

        if(keywords != null){
            allKeywords.addAll(keywords);
        }
        if(other != null){
            allKeywords.addAll(other);
        }

        String keywordString = Tag.getString(allKeywords);

        if(location != null && location.getName() != null && location.getName() != ""){
            keywordString += ", "+location.getName();
        }

        return keywordString;
    }

    public String getPrinting(){
        return Tag.getString(printing);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
