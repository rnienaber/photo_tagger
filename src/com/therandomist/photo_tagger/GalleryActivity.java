package com.therandomist.photo_tagger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import com.therandomist.photo_tagger.adapter.GalleryAdapter;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.service.PhotoService;
import com.therandomist.photo_tagger.service.database.PhotoRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryActivity extends Activity {

    private List<Photo> photos;
    private PhotoService service;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        service = new PhotoService(getApplicationContext());
        loadPhotos();

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new GalleryAdapter(this, photos));
    }

    private void loadPhotos(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String people = (String)bundle.get("people");
        String printing = (String)bundle.get("printing");
        String keywords = (String)bundle.get("keywords");
        String folder = (String)bundle.get("folder");

        Map<String, List<String>> tagMap = new HashMap<String, List<String>>();
        if(people != null && !people.equals("")){
            String[] tags = people.split(",");
            tagMap.put(PhotoRepository.PEOPLE, Arrays.asList(tags));
        }else if(printing!= null && !printing.equals("")){
            String[] tags = printing.split(",");
            tagMap.put(PhotoRepository.PRINTING, Arrays.asList(tags));
        }else if(keywords != null && !keywords.equals("")){
            String[] tags = keywords.split(",");
            tagMap.put(PhotoRepository.KEYWORDS, Arrays.asList(tags));
        }

        photos = service.getGalleryPhotos(tagMap, folder);
    }
}