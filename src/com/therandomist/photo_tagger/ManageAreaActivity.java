package com.therandomist.photo_tagger;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import com.therandomist.photo_tagger.adapter.LocationListAdapter;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Location;
import com.therandomist.photo_tagger.service.AreaService;
import com.therandomist.photo_tagger.service.LocationService;

import java.util.ArrayList;
import java.util.List;

public class ManageAreaActivity  extends ListActivity {

    protected LocationListAdapter adapter = null;
    protected List<Location> locations = null;
    protected LocationService locationService;
    protected AreaService areaService;

    protected ListView listView;

    private Area area = null;

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
                Intent intent = new Intent(getApplicationContext(), AddLocationActivity.class);
                intent.putExtra("areaId", area.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
