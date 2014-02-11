package com.therandomist.photo_tagger.service;

import android.content.Context;
import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.model.Tag;
import com.therandomist.photo_tagger.service.database.PhotoRepository;
import com.therandomist.photo_tagger.service.database.TagRepository;

import java.util.*;

public class PhotoService {

    private PhotoRepository repository;
    private TagRepository tagRepository;


    public PhotoService(Context context) {
        repository = new PhotoRepository(context);
        tagRepository = new TagRepository(context);
    }

    public List<Photo> getGalleryPhotos(Map<String, List<String>> tagMap, String folder){
        return repository.findByTags(tagMap, folder);
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

    public List<Photo> getPhotosForFolder(String folder){
        String correctedFolder = FileHelper.getCorrectedFolder(folder);
        return repository.findAllBy("folder", correctedFolder);
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

    public Photo getHomePagePhoto() {
        Map<String, List<String>> tagMap = new HashMap<String, List<String>>();
        tagMap.put(PhotoRepository.PRINTING, Arrays.asList("enlargement"));
        List<Photo> photos = repository.findByTags(tagMap);
        int random = new Random().nextInt(photos.size());
        return photos.get(random);
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
