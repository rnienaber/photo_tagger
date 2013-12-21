package com.therandomist.photo_tagger;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Location;
import com.therandomist.photo_tagger.service.AreaService;
import com.therandomist.photo_tagger.service.LocationService;

public class AddLocationActivity extends FragmentActivity  {
    private AreaService areaService;
    private GoogleMap map;
    private Circle circle;

    Long areaId;
    Area area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location);

        areaService = new AreaService(getApplicationContext());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        areaId = (Long)bundle.get("areaId");
        area = areaService.getArea(areaId);

        setTitle("Add Location in "+area.getName());

        setupMap();

        final EditText nameField = (EditText) findViewById(R.id.name_textfield);
        final LocationService service = new LocationService(this);

        Button saveButton = (Button) findViewById(R.id.save_button);
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

    public void setupMap(){
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.getUiSettings().setZoomControlsEnabled(false);
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
