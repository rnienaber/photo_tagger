package com.therandomist.photo_tagger.service;

import android.content.Context;
import com.therandomist.photo_tagger.model.Category;
import com.therandomist.photo_tagger.model.Tag;
import com.therandomist.photo_tagger.service.database.TagRepository;

import java.util.ArrayList;
import java.util.List;

public class TagService {

    private TagRepository repository;

    public TagService(Context context) {
        repository = new TagRepository(context);
    }

    public List<Tag> getAllTagsForCategory(Category category){
        return repository.findAllBy("category_id", category.getId());
    }

    public List<Tag> getTagsById(int[] ids){
        List<Long> longIds = new ArrayList<Long>();
        for(Integer id : ids){
            longIds.add(id.longValue());
        }
        return repository.getTags(longIds);
    }

    public void addTag(Tag tag){
        Long id = repository.insert(tag);
        if(id != null){
            tag.setId(id);
        }
    }
}
