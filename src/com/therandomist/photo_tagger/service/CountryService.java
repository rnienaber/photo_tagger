package com.therandomist.photo_tagger.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Country;
import com.therandomist.photo_tagger.service.database.AreaRepository;
import com.therandomist.photo_tagger.service.database.CountryRepository;

import java.util.List;

public class CountryService {

    private CountryRepository repository;
    private AreaRepository areaRepository;

    public CountryService(Context context) {
        repository = new CountryRepository(context);
        areaRepository = new AreaRepository(context);
    }

    public List<Country> getAllCountries(){

        SQLiteDatabase database = repository.openReadable();

        try{
            List<Country> countries = repository.findAll(database);

            for(Country country : countries){
                List<Area> areas = areaRepository.findAllBy("country_id", country.getId(), database);
                country.setAreas(areas);
            }
            return countries;
        }finally{
            database.close();
        }
    }

    public void addCountry(Country country){
        Long id = repository.insert(country);
        if(id != null){
            country.setId(id);
        }
    }
}
