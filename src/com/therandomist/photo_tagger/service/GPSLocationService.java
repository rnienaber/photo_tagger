package com.therandomist.photo_tagger.service;

import android.content.Context;
import com.therandomist.photo_tagger.model.GPSLocation;
import com.therandomist.photo_tagger.service.database.GPSLocationDBAdapter;

import java.util.List;

public class GPSLocationService {

    private GPSLocationDBAdapter database;

    public GPSLocationService(Context context) {
        database = new GPSLocationDBAdapter(context);
    }

    public GPSLocation getGPSLocation(Long gpsLocationId){
        database.open();
        GPSLocation gpsLocation = database.getGPSLocation(gpsLocationId);
        database.close();

        return gpsLocation;
    }

    public GPSLocation getGPSLocation(String name){
        database.open();
        GPSLocation gpsLocation = database.getGPSLocation(name);
        database.close();

        return gpsLocation;
    }

    public List<GPSLocation> getAllGPSLocations(){
        database.open();
        List<GPSLocation> gpsLocations = database.getAllGPSLocations();
        database.close();

        return gpsLocations;
    }

    public void addGPSLocation(GPSLocation gpsLocation){
        if(gpsLocation != null){
            database.open();
            long id = database.addGPSLocation(gpsLocation);
            database.close();
            gpsLocation.setId(id);
        }
    }

    public void deleteAllGPSLocations(){
        database.open();
        database.deleteAllGPSLocations();
        database.close();
    }
}
