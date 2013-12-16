package com.therandomist.photo_tagger.service;

import android.content.Context;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Country;
import com.therandomist.photo_tagger.service.database.CountryDBAdapter;

import java.util.List;

public class CountryService {

    private CountryDBAdapter database;
    private Context context;

    public CountryService(Context context) {
        database = new CountryDBAdapter(context);
        this.context = context;
    }

    public Country getCountry(Long countryId){
        database.open();
        Country country = database.getCountry(countryId);
        database.close();

        List<Area> areas = new AreaService(context).getAllAreasForCountry(country);
        country.setAreas(areas);

        return country;
    }

    public Country getCountry(String name){
        database.open();
        Country country = database.getCountry(name);
        database.close();

        List<Area> areas = new AreaService(context).getAllAreasForCountry(country);
        country.setAreas(areas);

        return country;
    }

    public List<Country> getAllCountries(){
        database.open();
        List<Country> countries = database.getAllCountries();
        database.close();

        for(Country country : countries){
            List<Area> areas = new AreaService(context).getAllAreasForCountry(country);
            country.setAreas(areas);
        }

        return countries;
    }

    public void addCountry(Country country){
        if(country != null){
            database.open();
            long id = database.addCountry(country);
            database.close();
            country.setId(id);
        }
    }

    public void deleteAllCountries(){
        database.open();
        database.deleteAllCountries();
        database.close();
    }
}
