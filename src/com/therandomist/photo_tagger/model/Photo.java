package com.therandomist.photo_tagger.model;

import com.therandomist.photo_tagger.service.FileService;

import java.util.List;

public class Photo {

    Long id;
    String filename;
    String folder;
    GPSLocation location;
    List<Tag> people;
    List<Tag> keywords;
    List<Tag> printing;

    public Photo(Long id, String filename, String folder, GPSLocation location, List<Tag> people, List<Tag> keywords, List<Tag> printing) {
        this.id = id;
        this.filename = filename;
        this.folder = folder;
        this.location = location;
        this.people = people;
        this.keywords = keywords;
        this.printing = printing;
    }

    public Photo(String filename, String folder, GPSLocation location, List<Tag> people, List<Tag> keywords, List<Tag> printing) {
        this.filename = filename;
        this.folder = folder;
        this.location = location;
        this.people = people;
        this.keywords = keywords;
        this.printing = printing;
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

    public boolean hasTag(Tag tag){
        return people.contains(tag) || keywords.contains(tag) || printing.contains(tag);
    }

    public String getPeople(){
        return Tag.getString(people);
    }

    public void addPeople(Tag person){
        if(!people.contains(person)){
            people.add(person);
        }
    }

    public void removePeople(Tag person){
        people.remove(person);
    }

    public String getKeywords(){
        return Tag.getString(keywords);
//        List<Tag> allKeywords = new ArrayList<Tag>();
//
//        if(keywords != null){
//            allKeywords.addAll(keywords);
//        }
//
//        String keywordString = Tag.getString(allKeywords);
//
////        if(location != null && location.getName() != null && location.getName() != ""){
////            keywordString += ", "+location.getName();
////        }
//
//        return keywordString;
    }

    public String getPrinting(){
        return Tag.getString(printing);
    }

    public void addPrintingTag(Tag tag){
        if(!printing.contains(tag)){
            printing.add(tag);
        }
    }

    public void removePrintingTag(Tag tag){
        printing.remove(tag);
    }

    public void addKeywordTag(Tag tag){
        if(!keywords.contains(tag)){
            keywords.add(tag);
        }
    }

    public void removeKeywordTag(Tag tag){
        keywords.remove(tag);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
