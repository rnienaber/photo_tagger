package com.therandomist.photo_tagger.model;

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

    public String getFilename() {
        return filename;
    }

    public String getFolder() {
        return folder;
    }

    public Double getLatitude() {
        return location.getLatitude();
    }

    public Double getLongitude() {
        return location.getLongitude();
    }

    public String getPeople(){
        return Tag.getString(people);
    }

    public String getKeywords(){

        List<Tag> allKeywords = new ArrayList<Tag>();
        allKeywords.addAll(keywords);
        allKeywords.addAll(other);

        String keywordString = Tag.getString(allKeywords);

        if(location.getName() != null && location.getName() != ""){
            keywordString += ", "+location.getName();
        }

        return keywordString;
    }

    public String getPrinting(){
        return Tag.getString(printing);
    }

}
