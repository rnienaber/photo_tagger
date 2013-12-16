package com.therandomist.photo_tagger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.therandomist.photo_tagger.model.Country;
import com.therandomist.photo_tagger.service.CountryService;

public class AddCountriesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_countries);

        final EditText nameField = (EditText) findViewById(R.id.name_textfield);

        final CountryService service = new CountryService(this);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = nameField.getText().toString();
                service.addCountry(new Country(name));
                finish();
            }
        });
    }
}
