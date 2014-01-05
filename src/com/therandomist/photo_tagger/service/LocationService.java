package com.therandomist.photo_tagger.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Location;
import com.therandomist.photo_tagger.service.database.AreaRepository;
import com.therandomist.photo_tagger.service.database.LocationRepository;

import java.util.List;

public class LocationService {

    private LocationRepository repository;
    private AreaRepository areaRepository;

    public LocationService(Context context) {
        repository = new LocationRepository(context);
        areaRepository = new AreaRepository(context);
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

    public Location getLocation(Long locationId) {
        SQLiteDatabase database = repository.openReadable();

        try{
            Location location = repository.findAllBy("_id", locationId, database).get(0);
            Area area = areaRepository.findAllBy("_id", location.getAreaId(), database).get(0);
            location.setArea(area);
            return location;
        }finally{
            database.close();
        }
    }
}
