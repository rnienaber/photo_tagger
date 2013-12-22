package com.therandomist.photo_tagger.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.therandomist.photo_tagger.model.Category;
import com.therandomist.photo_tagger.model.Tag;
import com.therandomist.photo_tagger.service.database.CategoryRepository;
import com.therandomist.photo_tagger.service.database.TagRepository;

import java.util.List;

public class CategoryService {

    private CategoryRepository repository;
    private TagRepository tagRepository;

    public CategoryService(Context context) {
        this.repository = new CategoryRepository(context);
        this.tagRepository = new TagRepository(context);
    }

    public Category getCategory(String name){
        SQLiteDatabase database = repository.openReadable();

        try{
            Category category = repository.findAllBy("name", name, database).get(0);
            List<Tag> tags = tagRepository.findAllBy("category_id", category.getId(), database);
            category.setTags(tags);
            return category;
        }finally{
            database.close();
        }
    }

    public List<Category> getAllCategories(){
        SQLiteDatabase database = repository.openReadable();

        try{
            List<Category> categories = repository.findAll(database);
            for(Category category : categories){
                List<Tag> tags = tagRepository.findAllBy("category_id", category.getId(), database);
                category.setTags(tags);
            }
            return categories;
        }finally{
            database.close();
        }
    }

    public void addCategory(Category category){
        Long id = repository.insert(category);
        if(id != null){
            category.setId(id);
        }
    }
}
