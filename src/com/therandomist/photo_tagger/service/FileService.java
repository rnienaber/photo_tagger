package com.therandomist.photo_tagger.service;

import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileService {

    public static final String ROOT = "/mnt/sdcard/photos";

    public List<File> getAllFiles(String path){
        File file = new File(path);
        Log.i(HomeActivity.APP_NAME, "Loading file: "+ file.getAbsolutePath());
        return Arrays.asList(file.listFiles());
    }

    public static String asPhotoPath(String path){
        return path.substring(path.indexOf("photos"), path.length());
    }

    public static String getFolder(String path){
        return path.substring(0, path.lastIndexOf("/"));
    }

    public static String getFilename(String path){
        return path.substring(path.lastIndexOf("/")+1, path.length());
    }
}