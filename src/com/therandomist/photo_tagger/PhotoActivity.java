package com.therandomist.photo_tagger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class PhotoActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo);

        loadPhoto();

    }

    public void loadPhoto(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String photoPath = (String)bundle.get("photoPath");

        ImageView photoView = (ImageView) findViewById(R.id.photo);
        if(photoView != null){

            BitmapDrawable d = new BitmapDrawable(getResources(), photoPath);
            photoView.setImageDrawable(d);
        }
    }
}