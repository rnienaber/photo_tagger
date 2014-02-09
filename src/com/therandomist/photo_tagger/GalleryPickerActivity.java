package com.therandomist.photo_tagger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.therandomist.photo_tagger.service.FileHelper;

public class GalleryPickerActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_picker);



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String folder = (String)bundle.get("folder");

        TextView photoNameView = (TextView) findViewById(R.id.folder_name);
        if(photoNameView != null){
            photoNameView.setText(FileHelper.getCorrectedFolder(folder));
        }
    }

}