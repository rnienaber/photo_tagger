package com.therandomist.photo_tagger.service;

import android.content.Context;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Location;
import com.therandomist.photo_tagger.service.database.LocationRepository;

import java.util.List;

public class LocationService {

    private LocationRepository repository;

    public LocationService(Context context) {
        repository = new LocationRepository(context);
    }

    public List<Location> getAllLocationsForArea(Area area){
        return repository.findAllBy("area_id", area.getId());
    }

    public void addLocation(Location location){
        Long id = repository.insert(location);
        if(id != null){
            location.setId(id);
        }
    }

}
