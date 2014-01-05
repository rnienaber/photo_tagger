package com.therandomist.photo_tagger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Location;
import com.therandomist.photo_tagger.service.AreaService;
import com.therandomist.photo_tagger.service.LocationService;

public class LocationActivity extends FragmentActivity  {

    public static final String VIEW = "view";
    public static final String USE = "use";
    public static final String CREATE = "create";
    public static final int IDENTIFIER = 52;

    private GoogleMap map;
    private Circle circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        setContentView(R.layout.location);

        String state = (String)bundle.get("state");
        if(state.equalsIgnoreCase(CREATE)){
            Long areaId = (Long)bundle.get("areaId");
            setupCreateView(areaId);
        }else{

            Long locationId = (Long)bundle.get("locationId");
            LocationService locationService = new LocationService(getApplicationContext());
            Location location = locationService.getLocation(locationId);

            if(state.equalsIgnoreCase(VIEW)){
                setupViewView(location);
            }else if(state.equalsIgnoreCase(USE)){
                setupUseView(location);
            }
        }
    }

    public void setupCreateView(Long areaId){
        AreaService areaService = new AreaService(getApplicationContext());
        final Area area = areaService.getArea(areaId);

        setTitle("Add Location in "+area.getName());
        setupMap(null);

        final EditText nameField = (EditText) findViewById(R.id.name_textfield);
        final LocationService service = new LocationService(this);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setVisibility(View.VISIBLE);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                double latitude = map.getCameraPosition().target.latitude;
                double longitude = map.getCameraPosition().target.longitude;
                String name = nameField.getText().toString();

                service.addLocation(new Location(name, latitude, longitude, area));
                finish();
            }
        });
    }

    public void setupViewView(Location location){
        setTitle("View Location in " + location.getArea().getName());
        setupMap(location);

        final EditText nameField = (EditText) findViewById(R.id.name_textfield);
        nameField.setText(location.getName());
    }

    public void setupUseView(Location location){
        setTitle("Use Location in " + location.getArea().getName());
        setupMap(location);

        final EditText nameField = (EditText) findViewById(R.id.name_textfield);
        nameField.setText(location.getName());

        Button nearButton = (Button) findViewById(R.id.near_button);
        nearButton.setVisibility(View.VISIBLE);
        nearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String locationName = nameField.getText().toString();
                nameField.setText("near "+locationName);
            }
        });

        Button applyButton = (Button) findViewById(R.id.apply_button);
        applyButton.setVisibility(View.VISIBLE);
        applyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                double latitude = map.getCameraPosition().target.latitude;
                double longitude = map.getCameraPosition().target.longitude;
                String name = nameField.getText().toString();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("name", name);
                resultIntent.putExtra("latitude", latitude);
                resultIntent.putExtra("longitude", longitude);
                setResult(Activity.RESULT_OK, resultIntent);

                finish();
            }
        });


    }

    public void setupMap(Location location){
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.getUiSettings().setZoomControlsEnabled(false);

        if(location != null){
            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(position, 15);
            map.moveCamera(update);
        }

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if(circle != null){
                    circle.setCenter(cameraPosition.target);
                }else{
                    circle = map.addCircle(new CircleOptions()
                            .center(cameraPosition.target)
                            .radius(15)
                            .strokeWidth(2.0f)
                            .strokeColor(Color.RED));
                }
            }
        });
    }

}
