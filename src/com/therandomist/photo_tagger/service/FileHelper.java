package com.therandomist.photo_tagger.service;

import android.os.Environment;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileHelper {

    public static final String STORAGE_ROOT = Environment.getExternalStorageDirectory().getPath();
    public static final String ROOT = Environment.getExternalStorageDirectory().getPath() +"/photos";

    public List<File> getAllFiles(String path){
        File file = new File(path);
        return Arrays.asList(file.listFiles());
    }

    public static String getFolder(String path){
        String fullPath = path.substring(0, path.lastIndexOf("/"));
        return getCorrectedPath(fullPath);
    }

    public static String getFilename(String path){
        return path.substring(path.lastIndexOf("/")+1, path.length());
    }

    public static String getPath(String folder, String filename){
        return folder +"/"+ filename;
    }

    public static String getCorrectedPath(String path){
        return path.replace(STORAGE_ROOT, "");
    }

}