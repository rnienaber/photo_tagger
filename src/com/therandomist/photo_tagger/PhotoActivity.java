package com.therandomist.photo_tagger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.service.FileService;
import com.therandomist.photo_tagger.service.PhotoService;

public class PhotoActivity extends Activity {

    private Photo photo = null;
    private PhotoService photoService;
    private String photoPath = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.photoService = new PhotoService(getApplicationContext());

        setContentView(R.layout.photo);
        readFromBundle();
        loadPhoto();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPhoto();
    }

    public void readFromBundle(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        photoPath = (String)bundle.get("photoPath");
    }

    public void loadPhoto(){
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

        TextView peopleTagsView = (TextView) findViewById(R.id.people_tags_text);
        if(peopleTagsView != null){
            peopleTagsView.setText(photo.getPeople());
        }

        Button peopleTagsButton = (Button) findViewById(R.id.people_tags_button);
        peopleTagsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Log.i(HomeActivity.APP_NAME, "Clicked on add tags to photo.");
                Intent i = new Intent(view.getContext(), PhotoTagListActivity.class);
                i.putExtra("categoryName", "people");
                i.putExtra("photoPath", photoPath);
                startActivity(i);

            }
        });
    }
}