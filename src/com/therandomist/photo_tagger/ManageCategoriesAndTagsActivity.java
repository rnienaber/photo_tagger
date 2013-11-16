package com.therandomist.photo_tagger;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.therandomist.photo_tagger.adapter.CategoriesListAdapter;
import com.therandomist.photo_tagger.model.Category;
import com.therandomist.photo_tagger.model.Tag;
import com.therandomist.photo_tagger.service.CategoryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageCategoriesAndTagsActivity extends ExpandableListActivity {
    private static final String KEY1 = "KEY1";

    private CategoriesListAdapter adapter;
    private CategoryService categoryService;
    private List<Category> categories;

    private List<Map<String, String>> groupData;
    private List<List<Tag>> childData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryService = new CategoryService(this);
        groupData = new ArrayList<Map<String, String>>();
        childData = new ArrayList<List<Tag>>();

        loadCategories();

        adapter = new CategoriesListAdapter(
                this,
                groupData,
                R.layout.tag_group,         //group layout
                new String[] { KEY1 },                            //group from
                new int[] { R.id.category_name, android.R.id.text2 },   //group to
                childData,
                R.layout.tag_row         //child layout
        );

        setContentView(R.layout.manage_tags_and_categories);
        setListAdapter(adapter);
    }

    public void loadCategories(){
        categories = categoryService.getAllCategories();

        groupData.clear();
        childData.clear();

        for(Category category : categories){
            Map<String, String> categoryMap = new HashMap<String, String>();
            groupData.add(categoryMap);
            categoryMap.put(KEY1, category.getName());

            childData.add(category.getTags());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tag_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_category_menu_item:
                Intent categoryIntent = new Intent(getApplicationContext(), ManageCategoriesActivity.class);
                startActivity(categoryIntent);
                return true;
            case R.id.add_tag_menu_item:
                Intent tagIntent = new Intent(getApplicationContext(), ManageTagsActivity.class);
                startActivity(tagIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
