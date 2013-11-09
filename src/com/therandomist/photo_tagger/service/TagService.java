package com.therandomist.photo_tagger.service;

import android.content.Context;
import com.therandomist.photo_tagger.model.Category;
import com.therandomist.photo_tagger.model.Tag;
import com.therandomist.photo_tagger.service.database.TagDBAdapter;

import java.util.List;

public class TagService {

    private TagDBAdapter database;

    public TagService(Context context) {
        database = new TagDBAdapter(context);
    }

    public Tag getTag(Long tagId){
        database.open();
        Tag tag = database.getTag(tagId);
        database.close();
        return tag;
    }

    public List<Tag> getAllTags(){
        database.open();
        List<Tag> result = database.getAllTags();
        database.close();
        return result;
    }

    public List<Tag> getAllTagsForCategory(Category category){
        database.open();
        List<Tag> result = database.getAllTagsForCategory(category);
        database.close();
        return result;
    }

    public void addTag(Tag tag){
        if(tag != null){
            database.open();
            long id = database.addTag(tag);
            database.close();
            tag.setId(id);
        }
    }

    public void deleteAllTags(){
        database.open();
        database.deleteAllTags();
        database.close();
    }
}
