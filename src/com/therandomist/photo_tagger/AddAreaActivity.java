package com.therandomist.photo_tagger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Country;
import com.therandomist.photo_tagger.service.AreaService;
import com.therandomist.photo_tagger.service.CountryService;

import java.util.List;

public class AddAreaActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_area);

        final EditText nameField = (EditText) findViewById(R.id.name_textfield);

        final CountryService countryService = new CountryService(this);
        final AreaService areaService = new AreaService(this);

        final Spinner spinner = (Spinner) findViewById(R.id.country_spinner);
        List<Country> countries = countryService.getAllCountries();

        ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(this, android.R.layout.simple_spinner_item, countries);
        spinner.setAdapter(adapter);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Country country = (Country)spinner.getSelectedItem();
                String areaName = nameField.getText().toString();
                areaService.addArea(new Area(areaName, country));
                finish();
            }
        });
    }
}
