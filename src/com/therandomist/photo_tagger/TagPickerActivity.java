package com.therandomist.photo_tagger;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.therandomist.photo_tagger.adapter.TagListAdapter;
import com.therandomist.photo_tagger.model.Category;
import com.therandomist.photo_tagger.model.Tag;
import com.therandomist.photo_tagger.service.CategoryService;
import com.therandomist.photo_tagger.service.TagService;

import java.util.ArrayList;
import java.util.List;

public class TagPickerActivity extends ListActivity {
    public static final int IDENTIFIER = 872;

    protected TagListAdapter adapter = null;
    protected List<Tag> tags = null;
    protected TagService service;
    protected CategoryService categoryService;

    protected ListView listView;

    private String categoryName = null;
    private List<Tag> selectedTags = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_picker);

        service = new TagService(this);
        categoryService = new CategoryService(this);
        selectedTags = new ArrayList<Tag>();

        readFromBundle();
        initializeList();

        Button applyButton = (Button) findViewById(R.id.apply_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finishActivity();
            }
        });
    }

    public void readFromBundle(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        categoryName = (String)bundle.get("categoryName");
        int[] selectedTagIds = (int[])bundle.get("selectedTagIds");
        selectedTags = service.getTagsById(selectedTagIds);
    }

    public void initializeList(){
        tags = new ArrayList<Tag>();
        adapter = new TagListAdapter(this, R.layout.tag_row, tags, selectedTags);
        setListAdapter(adapter);
        listView = getListView();
        loadTags();
    }

    public void onListItemClick(ListView l, View view, int position, long id){
        super.onListItemClick(l, view, position, id);
        Tag tag = adapter.getItem(position);

        if(selectedTags.contains(tag)){
            selectedTags.remove(tag);
        }else{
            selectedTags.add(tag);
        }

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

    private void finishActivity(){
        int[] tagIds = Tag.getIds(selectedTags);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("categoryName", categoryName);
        resultIntent.putExtra("selectedTagsIds", tagIds);

        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }
}
