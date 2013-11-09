package com.therandomist.photo_tagger.service;

import android.content.Context;
import com.therandomist.photo_tagger.model.Category;
import com.therandomist.photo_tagger.model.Tag;
import com.therandomist.photo_tagger.service.database.CategoryDBAdapter;

import java.util.List;

public class CategoryService {

    private CategoryDBAdapter database;
    private Context context;

    public CategoryService(Context context) {
        database = new CategoryDBAdapter(context);
        this.context = context;
    }

    public Category getCategory(Long categoryId){
        database.open();
        Category category = database.getCategory(categoryId);
        database.close();

        List<Tag> tags = new TagService(context).getAllTagsForCategory(category);
        category.setTags(tags);

        return category;
    }

    public List<Category> getAllCategories(){
        database.open();
        List<Category> categories = database.getAllCategories();
        database.close();

        for(Category category : categories){
            List<Tag> tags = new TagService(context).getAllTagsForCategory(category);
            category.setTags(tags);
        }

        return categories;
    }

    public void addCategory(Category category){
        if(category != null){
            database.open();
            long id = database.addCategory(category);
            database.close();
            category.setId(id);
        }
    }

    public void deleteAllCategories(){
        database.open();
        database.deleteAllCategories();
        database.close();
    }
}
