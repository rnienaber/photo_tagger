package com.therandomist.photo_tagger.service;

import android.content.Context;
import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.model.Tag;
import com.therandomist.photo_tagger.service.database.PhotoRepository;
import com.therandomist.photo_tagger.service.database.TagRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoService {

    private PhotoRepository repository;
    private TagRepository tagRepository;


    public PhotoService(Context context) {
        repository = new PhotoRepository(context);
        tagRepository = new TagRepository(context);
    }

    public Photo getPhoto(String path){
        Photo photo = repository.findByPath(path);

        if(photo == null){
            photo = new Photo();
            photo.setPath(FileHelper.getCorrectedPath(path));
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

    public Map<String, Photo> getPhotosByFolders(List<String> paths){
        List<String> folders = new ArrayList<String>();
        for(String path : paths){
            String folder = FileHelper.getFolder(path);
            if(!folders.contains(folder)) folders.add(folder);
        }

        List<Photo> photos = repository.findByFolders(folders);
        Map<String, Photo> photoMap = new HashMap<String, Photo>();

        for(Photo photo : photos){
            String path = FileHelper.getPath(photo.getFolder(), photo.getFilename());

            Log.i(HomeActivity.APP_NAME, "Adding to map: "+path);

            photoMap.put(path, photo);
        }
        return photoMap;
    }

    public void addPhoto(Photo photo){
        Long id = repository.insert(photo);
        if(id != null){
            photo.setId(id);
        }
    }

    public void savePhoto(Photo photo){
        Log.i(HomeActivity.APP_NAME, "Saving photo:" + photo);
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
