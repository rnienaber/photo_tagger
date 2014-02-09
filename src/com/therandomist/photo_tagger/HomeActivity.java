package com.therandomist.photo_tagger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.service.FileHelper;
import com.therandomist.photo_tagger.service.PhotoService;
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
                Intent i = new Intent(view.getContext(), PhotoListActivity.class);
                startActivity(i);
            }
        });

        Button galleryButton = (Button) findViewById(R.id.gallery_button);
        galleryButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), GalleryListActivity.class);
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

        ImageView photoView = (ImageView) findViewById(R.id.photo);

        if(photoView != null){
            PhotoService photoService = new PhotoService(getApplicationContext());
            Photo photo = photoService.getHomePagePhoto();
            if(photo != null){
                String photoPath = photo.getPath();
                Log.i(HomeActivity.APP_NAME, "Loading: "+photoPath);
                BitmapDrawable d = new BitmapDrawable(getResources(), FileHelper.getPathOnDevice(photo.getFolder(), photo.getFilename()));
                photoView.setImageDrawable(d);
            }
        }
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
