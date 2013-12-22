package com.therandomist.photo_tagger.service;

import android.content.Context;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.model.Tag;
import com.therandomist.photo_tagger.service.database.PhotoRepository;
import com.therandomist.photo_tagger.service.database.TagRepository;

import java.util.ArrayList;
import java.util.List;

public class PhotoService {

    private PhotoRepository repository;
    private TagRepository tagRepository;


    public PhotoService(Context context) {
        repository = new PhotoRepository(context);
        tagRepository = new TagRepository(context);
    }

    public Photo getPhoto(String path){
        String folder = FileHelper.getFolder(path);
        String filename = FileHelper.getFilename(path);

        Photo photo = repository.findByPath(folder, filename);

        if(photo == null){
            photo = new Photo();
            photo.setPath(path);
            addPhoto(photo);
        }else{

            List<Tag> people = convertNamesToTag(photo.getPeopleString());
            List<Tag> printing = convertNamesToTag(photo.getPrintingString());
            List<Tag> keywords = convertNamesToTag(photo.getKeywordString());

            photo.setPeople(people);
            photo.setPrinting(printing);
            photo.setKeywords(keywords);
        }

        return photo;
    }

    public void addPhoto(Photo photo){
        Long id = repository.insert(photo);
        if(id != null){
            photo.setId(id);
        }
    }

    public void savePhoto(Photo photo){
        if(photo.getId() != null){
            repository.update(photo, photo.getId());
        }else{
            addPhoto(photo);
        }
    }

    private List<Tag> convertNamesToTag(String names){
        String[] nameList = names.split(",");
        List<Tag> tags = new ArrayList<Tag>();

        if(nameList.length > 0){
            for(String name : nameList){
                tags.add(getTag(name));
            }
        }
        return tags;
    }

    private Tag getTag(String name){
        if(name == null || name.equals(""))
            return null;

        return tagRepository.findAllBy("name", name.trim()).get(0);
    }
}
