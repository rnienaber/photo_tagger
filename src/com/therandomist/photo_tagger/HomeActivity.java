package com.therandomist.photo_tagger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.therandomist.photo_tagger.service.CategoryService;
import com.therandomist.photo_tagger.service.PhotoService;
import com.therandomist.photo_tagger.service.TagService;

public class HomeActivity extends Activity {

    public static final String APP_NAME = "PHOTO TAGGER";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

//        CategoryService service = new CategoryService(this);
//        service.deleteAllCategories();

//        PhotoService photoService = new PhotoService(this);
//        photoService.deleteAllPhotos();

        Button tagPhotosButton = (Button) findViewById(R.id.tag_photos_button);
        tagPhotosButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Log.i(HomeActivity.APP_NAME, "Clicked on tag photos.");
                Intent i = new Intent(view.getContext(), ManagePhotos.class);
                startActivity(i);
            }
        });

        Button manageTagsButton = (Button) findViewById(R.id.manage_tags_button);
        manageTagsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Log.i(HomeActivity.APP_NAME, "Clicked on manage tags.");
                Intent i = new Intent(view.getContext(), ManageCategoriesAndTagsActivity.class);
                startActivity(i);
            }
        });
    }
}
