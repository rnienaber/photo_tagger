package com.therandomist.photo_tagger;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import com.therandomist.photo_tagger.service.database.DatabaseHelper;

public class HomeActivity extends Activity {

    public static final String APP_NAME = "PHOTO TAGGER";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        setupUI();
    }

    public void setupUI(){
        Button tagPhotosButton = (Button) findViewById(R.id.tag_photos_button);
        tagPhotosButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ManagePhotos.class);
                startActivity(i);
            }
        });

        Button manageTagsButton = (Button) findViewById(R.id.manage_tags_button);
        manageTagsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ManageCategoriesActivity.class);
                startActivity(i);
            }
        });

        Button manageLocationsButton = (Button) findViewById(R.id.manage_locations_button);
        manageLocationsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ManageCountriesActivity.class);
                i.putExtra("state", "manage");
                startActivity(i);
            }
        });

        Button backupButton = (Button) findViewById(R.id.backup_database_button);
        backupButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                emailDatabaseFile();
            }
        });
    }

    public void emailDatabaseFile(){
        String PATH =  Environment.getExternalStorageDirectory()+ DatabaseHelper.getDBFilename();

        Uri uri = Uri.parse("file://"+PATH);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL, "");
        i.putExtra(Intent.EXTRA_SUBJECT,"Photo Tagger Database");
        i.putExtra(Intent.EXTRA_TEXT,"");
        i.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(i, "Select application"));
    }
}
