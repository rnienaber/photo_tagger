package com.therandomist.photo_tagger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.service.PhotoService;

public class AddNotesActivity extends Activity {

    protected PhotoService photoService;
    private String photoPath = null;
    private Photo photo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_notes);

        photoService = new PhotoService(this);
        readFromBundle();

        final EditText notesField = (EditText) findViewById(R.id.notes_textfield);
        notesField.setText(photo.getNotes());

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                photo.setNotes(notesField.getText().toString());
                photoService.savePhoto(photo);
                finish();
            }
        });
    }

    public void readFromBundle(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        photoPath = (String)bundle.get("photoPath");
        photo = photoService.getPhoto(photoPath);
    }
}
