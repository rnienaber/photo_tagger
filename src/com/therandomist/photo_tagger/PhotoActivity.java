package com.therandomist.photo_tagger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.service.FileService;
import com.therandomist.photo_tagger.service.PhotoService;

import java.io.File;

public class PhotoActivity extends Activity {

    private Photo photo = null;
    private PhotoService photoService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.photoService = new PhotoService(getApplicationContext());

        setContentView(R.layout.photo);
        loadPhoto();
    }

    public void loadPhoto(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String photoPath = (String)bundle.get("photoPath");

        photo = photoService.getPhoto(photoPath);

        ImageView photoView = (ImageView) findViewById(R.id.photo);
        if(photoView != null){
            BitmapDrawable d = new BitmapDrawable(getResources(), photoPath);
            photoView.setImageDrawable(d);
        }

        TextView photoNameView = (TextView) findViewById(R.id.photo_file_name);
        if(photoNameView != null){
            photoNameView.setText(photoPath.replace(FileService.ROOT+"/", ""));
        }

        TextView peopleTagsView = (TextView) findViewById(R.id.people_tags);
        if(peopleTagsView != null){
            peopleTagsView.setText(photo.getPeople());
        }
    }
}