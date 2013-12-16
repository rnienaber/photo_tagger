package com.therandomist.photo_tagger.service;

import android.content.Context;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Location;
import com.therandomist.photo_tagger.service.database.LocationDBAdapter;

import java.util.List;

public class LocationService {

    private LocationDBAdapter database;

    public LocationService(Context context) {
        database = new LocationDBAdapter(context);
    }

    public Location getLocation(Long locationId){
        database.open();
        Location location = database.getLocation(locationId);
        database.close();
        return location;
    }

    public Location getLocation(String name){
        database.open();
        Location location = database.getLocation(name);
        database.close();
        return location;
    }

    public List<Location> getAllLocations(){
        database.open();
        List<Location> result = database.getAllLocations();
        database.close();
        return result;
    }

    public List<Location> getAllLocationsForArea(Area area){
        database.open();
        List<Location> result = database.getAllLocationsForArea(area);
        database.close();
        return result;
    }

    public void addLocation(Location location){
        if(location != null){
            database.open();
            long id = database.addLocation(location);
            database.close();
            location.setId(id);
        }
    }

    public void deleteAllLocations(){
        database.open();
        database.deleteAllLocations();
        database.close();
    }
}
