package com.therandomist.photo_tagger.model;

public class GPSLocation {

    Double latitude;
    Double longitude;
    String name;
    Long id;

    public GPSLocation(String name, Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public GPSLocation(Long id, String name, Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
