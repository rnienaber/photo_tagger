package com.therandomist.photo_tagger.service;

import android.content.Context;
import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;
import com.therandomist.photo_tagger.model.Category;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.service.database.PhotoDBAdapter;

import java.util.List;

public class PhotoService {

    private PhotoDBAdapter database;

    public PhotoService(Context context) {
        database = new PhotoDBAdapter(context);
    }

    public Photo getPhoto(Long photoId){
        database.open();
        Photo photo = database.getPhoto(photoId);
        database.close();
        return photo;
    }

    public Photo getPhoto(String path){
        String photoPath = FileService.asPhotoPath(path);

        Log.i(HomeActivity.APP_NAME, "Getting photo from: " + photoPath);

        database.open();
        Photo photo = database.getPhoto(photoPath);
        database.close();

        if(photo == null){
            photo = new Photo();
            photo.setPath(photoPath);
            addPhoto(photo);
        }

        return photo;
    }

    public List<Photo> getAllPhotos(){
        database.open();
        List<Photo> result = database.getAllPhotos();
        database.close();
        return result;
    }

    public void addPhoto(Photo photo){
        if(photo != null){
            database.open();
            long id = database.addPhoto(photo);
            database.close();
            photo.setId(id);
        }
    }

    public void deleteAllPhotos(){
        database.open();
        database.deleteAllPhotos();
        database.close();
    }
}
