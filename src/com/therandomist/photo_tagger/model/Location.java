package com.therandomist.photo_tagger.model;

public class Location {

    Double latitude;
    Double longitude;
    String name;
    Long id;
    Area area;
    Long areaId;

    public Location(String name, Double latitude, Double longitude, Area area) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.area = area;
    }

    public Location(Long id, String name, Double latitude, Double longitude, Long areaId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.id = id;
        this.areaId = areaId;
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

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public Area getArea() {
        return area;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
