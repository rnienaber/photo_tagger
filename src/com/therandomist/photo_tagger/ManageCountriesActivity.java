package com.therandomist.photo_tagger;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import com.therandomist.photo_tagger.adapter.CountriesListAdapter;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Country;
import com.therandomist.photo_tagger.service.CountryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageCountriesActivity extends ExpandableListActivity {
    private static final String KEY1 = "KEY1";

    private CountriesListAdapter adapter;
    private CountryService countryService;
    private List<Country> countries;

    private List<Map<String, String>> groupData;
    private List<List<Area>> childData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        countryService = new CountryService(this);
        groupData = new ArrayList<Map<String, String>>();
        childData = new ArrayList<List<Area>>();

        loadCountries();

        adapter = new CountriesListAdapter(
                this,
                groupData,
                R.layout.country_row,         //group layout
                new String[] { KEY1 },                            //group from
                new int[] { R.id.country_name, android.R.id.text2 },   //group to
                childData,
                R.layout.area_row         //child layout
        );

        setContentView(R.layout.manage_countries);
        setListAdapter(adapter);
    }

    public void loadCountries(){
        countries = countryService.getAllCountries();

        groupData.clear();
        childData.clear();

        for(Country country : countries){
            Map<String, String> countryMap = new HashMap<String, String>();
            groupData.add(countryMap);
            countryMap.put(KEY1, country.getName());

            childData.add(country.getAreas());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCountries();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.country_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_country:
                Intent categoryIntent = new Intent(getApplicationContext(), AddCountriesActivity.class);
                startActivity(categoryIntent);
                return true;
            case R.id.add_area:
                Intent tagIntent = new Intent(getApplicationContext(), AddAreaActivity.class);
                startActivity(tagIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        super.onChildClick(parent, v, groupPosition, childPosition, id);
        Area area = adapter.getChild(groupPosition, childPosition);

        if(area != null){
            Intent i = new Intent(this, ManageAreaActivity.class);
            i.putExtra("areaId", area.getId());
            startActivity(i);
        }
        return true;
    }
}
