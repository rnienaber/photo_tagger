package com.therandomist.photo_tagger.model;

import com.therandomist.photo_tagger.service.FileHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Photo {

    Long id;
    String filename;
    String folder;
    String notes;
    Location location;
    List<Tag> people;
    List<Tag> keywords;
    List<Tag> printing;

    String peopleString;
    String keywordString;
    String printingString;

    public Photo(Long id, String filename, String folder, String notes, Location location, List<Tag> people, List<Tag> keywords, List<Tag> printing) {
        this.id = id;
        this.filename = filename;
        this.folder = folder;
        this.notes = notes;
        this.location = location;
        this.people = people;
        this.keywords = keywords;
        this.printing = printing;
    }

    public Photo(Long id, String filename, String folder, String notes, Location location, String peopleString, String keywordString, String printingString) {
        this.id = id;
        this.filename = filename;
        this.folder = folder;
        this.notes = notes;
        this.location = location;
        this.peopleString = peopleString;
        this.keywordString = keywordString;
        this.printingString = printingString;
    }

    public Photo() {
    }

    public void setPath(String path) {
        folder = FileHelper.getFolder(path);
        filename = FileHelper.getFilename(path);
    }

    public String getFilename() {
        return filename;
    }

    public String getFolder() {
        return folder;
    }

    public String getPath() {
        return FileHelper.getPath(folder, filename);
    }

    public Double getLatitude() {
        return location != null ? location.getLatitude() : Double.valueOf(0);
    }

    public Double getLongitude() {
        return location != null ? location.getLongitude() : Double.valueOf(0);
    }

    public String getLocationName() {
        return location != null ? location.getName() : "";
    }

    public boolean hasTag(Tag tag) {
        return people.contains(tag) || keywords.contains(tag) || printing.contains(tag);
    }

    public String getPeople() {
        return Tag.getString(people);
    }

    public void addPeople(Tag person) {
        if (!people.contains(person)) {
            people.add(person);
        }
    }

    public void removePeople(Tag person) {
        people.remove(person);
    }

    public String getKeywords() {
        return Tag.getString(keywords);
    }

    public String getPrinting() {
        return Tag.getString(printing);
    }

    public void addPrintingTag(Tag tag) {
        if (!printing.contains(tag)) {
            printing.add(tag);
        }
    }

    public void removePrintingTag(Tag tag) {
        printing.remove(tag);
    }

    public void addKeywordTag(Tag tag) {
        if (!keywords.contains(tag)) {
            keywords.add(tag);
        }
    }

    public void removeKeywordTag(Tag tag) {
        keywords.remove(tag);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setPeople(List<Tag> people) {
        this.people = people;
    }

    public void setPrinting(List<Tag> printing) {
        this.printing = printing;
    }

    public void setKeywords(List<Tag> keywords) {
        this.keywords = keywords;
    }

    public String getPeopleString() {
        return peopleString;
    }

    public String getPrintingString() {
        return printingString;
    }

    public String getKeywordString() {
        return keywordString;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public boolean hasLocation() {
        return getLocationName() != null && !getLocationName().equals("");
    }

    public List<Tag> getSelectedTags(String categoryName) {
        if (categoryName.equalsIgnoreCase("keywords")) {
            return keywords;
        } else if (categoryName.equalsIgnoreCase("people")) {
            return people;
        } else if (categoryName.equalsIgnoreCase("printing")) {
            return printing;
        }

        return new ArrayList<Tag>();
    }

    public void applyTags(String categoryName, List<Tag> selectedTags) {
        Collections.sort(selectedTags);
        if (categoryName.equalsIgnoreCase("people")) {
            people = selectedTags;
        } else if (categoryName.equalsIgnoreCase("printing")) {
            printing = selectedTags;
        } else if (categoryName.equalsIgnoreCase("keywords")) {
            keywords = selectedTags;
        }
    }

    public boolean hasData() {
        boolean hasData = false;

        if (getKeywordString().length() > 0 || getPeopleString().length() > 0 || getPrintingString().length() > 0 || hasLocation()) {
            hasData = true;
        }

        return hasData;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "filename='" + filename + '\'' +
                ", location=" + location +
                ", people=" + people +
                ", keywords=" + keywords +
                ", printing=" + printing +
                '}';
    }

    public boolean isDeleted() {
        return printingString.toLowerCase().contains("delete");
    }
}
