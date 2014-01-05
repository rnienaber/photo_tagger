package com.therandomist.photo_tagger.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Country;
import com.therandomist.photo_tagger.model.Location;
import com.therandomist.photo_tagger.service.database.AreaRepository;
import com.therandomist.photo_tagger.service.database.LocationRepository;

import java.util.List;

public class AreaService {

    private AreaRepository repository;
    private LocationRepository locationRepository;

    public AreaService(Context context) {
        repository = new AreaRepository(context);
        locationRepository = new LocationRepository(context);
    }

    public Area getArea(Long areaId){
        SQLiteDatabase database = repository.openReadable();

        try{
            Area area = repository.findAllBy("_id", areaId, database).get(0);
            List<Location> locations = locationRepository.findAllBy("area_id", area.getId(), database);
            area.setLocations(locations);
            return area;
        }finally{
            database.close();
        }
    }

    public List<Area> getAllAreasForCountry(Country country){
        return repository.findAllBy("country_id", country.getId());
    }

    public void addArea(Area area){
        Long id = repository.insert(area);
        if(id != null){
            area.setId(id);
        }
    }
}
