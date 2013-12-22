package com.therandomist.photo_tagger;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.therandomist.photo_tagger.adapter.TagListAdapter;
import com.therandomist.photo_tagger.model.Category;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.model.Tag;
import com.therandomist.photo_tagger.service.CategoryService;
import com.therandomist.photo_tagger.service.PhotoService;
import com.therandomist.photo_tagger.service.TagService;

import java.util.ArrayList;
import java.util.List;

public class PhotoTagListActivity extends ListActivity {

    protected TagListAdapter adapter = null;
    protected List<Tag> tags = null;
    protected TagService service;
    protected CategoryService categoryService;
    protected PhotoService photoService;

    protected ListView listView;

    private String photoPath = null;
    private String categoryName = null;
    private Photo photo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_list);

        service = new TagService(this);
        categoryService = new CategoryService(this);
        photoService = new PhotoService(this);

        readFromBundle();
        loadPhoto();

        initializeList();
    }

    public void readFromBundle(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        photoPath = (String)bundle.get("photoPath");
        categoryName = (String)bundle.get("categoryName");
    }

    public void loadPhoto(){
        photo = photoService.getPhoto(photoPath);
    }

    public void initializeList(){
        tags = new ArrayList<Tag>();
        adapter = new TagListAdapter(this, getRowLayout(), tags, photo);
        setListAdapter(adapter);
        listView = getListView();
        loadTags();
    }

    public void onListItemClick(ListView l, View view, int position, long id){
        super.onListItemClick(l, view, position, id);
        Tag tag = adapter.getItem(position);

        if(categoryName.equalsIgnoreCase("people")){
            if(photo.hasTag(tag)){
                photo.removePeople(tag);
            }else{
                photo.addPeople(tag);
            }
        }
        else if(categoryName.equalsIgnoreCase("printing")){
            if(photo.hasTag(tag)){
                photo.removePrintingTag(tag);
            }else{
                photo.addPrintingTag(tag);
            }
        }
        else if(categoryName.equalsIgnoreCase("keywords")){
            if(photo.hasTag(tag)){
                photo.removeKeywordTag(tag);
            }else{
                photo.addKeywordTag(tag);
            }
        }

        photoService.savePhoto(photo);
        adapter.notifyDataSetChanged();
    }

    public void loadTags(){
        Category category = categoryService.getCategory(categoryName);
        tags = service.getAllTagsForCategory(category);
        if(tags != null && tags.size() > 0){
            adapter.notifyDataSetChanged();
            for(Tag tag : tags){
                adapter.add(tag);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public int getRowLayout(){
        return R.layout.tag_row;
    }
}
