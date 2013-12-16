package com.therandomist.photo_tagger.service;

import android.content.Context;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Country;
import com.therandomist.photo_tagger.model.Location;
import com.therandomist.photo_tagger.service.database.AreaDBAdapter;

import java.util.List;

public class AreaService {

    private AreaDBAdapter database;
    private Context context;

    public AreaService(Context context) {
        database = new AreaDBAdapter(context);
        this.context = context;
    }

    public Area getArea(Long areaId){
        database.open();
        Area area = database.getArea(areaId);
        database.close();

        List<Location> locations = new LocationService(context).getAllLocationsForArea(area);
        area.setLocations(locations);

        return area;
    }

    public Area getArea(String name){
        database.open();
        Area area = database.getArea(name);
        database.close();

        List<Location> locations = new LocationService(context).getAllLocationsForArea(area);
        area.setLocations(locations);

        return area;
    }

    public List<Area> getAllAreas(){
        database.open();
        List<Area> areas = database.getAllAreas();
        database.close();

        for(Area area : areas){
            List<Location> locations = new LocationService(context).getAllLocationsForArea(area);
            area.setLocations(locations);
        }

        return areas;
    }

    public List<Area> getAllAreasForCountry(Country country){
        database.open();
        List<Area> result = database.getAllAreasForCountry(country);
        database.close();
        return result;
    }

    public List<Area> getAllAreasForCountry(Long countryId){
        database.open();
        List<Area> result = database.getAllAreasForCountry(countryId);
        database.close();
        return result;
    }

    public void addArea(Area area){
        if(area != null){
            database.open();
            long id = database.addArea(area);
            database.close();
            area.setId(id);
        }
    }

    public void deleteAllAreas(){
        database.open();
        database.deleteAllAreas();
        database.close();
    }
}
