package com.therandomist.photo_tagger;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.therandomist.photo_tagger.adapter.LocationListAdapter;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Location;
import com.therandomist.photo_tagger.service.AreaService;
import com.therandomist.photo_tagger.service.LocationService;

import java.util.ArrayList;
import java.util.List;

public class ManageAreaActivity  extends ListActivity {

    public static final int IDENTIFIER = 622;

    protected LocationListAdapter adapter = null;
    protected List<Location> locations = null;
    protected LocationService locationService;
    protected AreaService areaService;

    protected ListView listView;

    private Area area = null;
    private String state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_list);

        locationService = new LocationService(this);
        areaService = new AreaService(this);

        readFromBundle();
        initializeList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLocations();
        adapter.notifyDataSetChanged();
    }

    public void readFromBundle(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Long areaId = (Long)bundle.get("areaId");
        state = (String)bundle.get("state");
        area = areaService.getArea(areaId);
    }

    public void initializeList(){
        locations = new ArrayList<Location>();
        adapter = new LocationListAdapter(this, getRowLayout(), locations, area);
        setListAdapter(adapter);
        listView = getListView();
        loadLocations();
    }

    public void loadLocations(){
        locations.clear();
        adapter.clear();

        locations = locationService.getAllLocationsForArea(area);
        if(locations != null && locations.size() > 0){
            adapter.notifyDataSetChanged();
            for(Location location: locations){
                adapter.add(location);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public int getRowLayout(){
        return R.layout.location_row;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.area_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_location:
                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                intent.putExtra("areaId", area.getId());
                intent.putExtra("state", LocationActivity.CREATE);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Location location = adapter.getItem(position);

        if(location != null){
            Intent i = new Intent(this, LocationActivity.class);
            i.putExtra("locationId", location.getId());
            i.putExtra("areaId", area.getId());

            if(state.equalsIgnoreCase("photo")){
                i.putExtra("state", LocationActivity.USE);
                startActivityForResult(i, LocationActivity.IDENTIFIER);
            }else{
                i.putExtra("state", LocationActivity.VIEW);
                startActivity(i);
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(HomeActivity.APP_NAME, "Manage Areas - Returning from: " + requestCode);

        if(!state.equalsIgnoreCase("photo")) return;

        switch(requestCode){
            case LocationActivity.IDENTIFIER :
                if (resultCode == Activity.RESULT_OK){
                    String name = data.getStringExtra("name");
                    Double latitude = data.getDoubleExtra("latitude", 0);
                    Double longitude = data.getDoubleExtra("longitude", 0);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", name);
                    resultIntent.putExtra("latitude", latitude);
                    resultIntent.putExtra("longitude", longitude);
                    setResult(Activity.RESULT_OK, resultIntent);

                    finish();
                }
                break;
        }
    }
}
