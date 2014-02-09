package com.therandomist.photo_tagger.service;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileHelper {

    public static final String STORAGE_ROOT = Environment.getExternalStorageDirectory().getPath();
    public static final String ROOT = Environment.getExternalStorageDirectory().getPath() +"/photos";

    public static List<File> getAllFiles(String path){
        File file = new File(path);
        File[] files = file.listFiles();
        Arrays.sort(files);
        return Arrays.asList(files);
    }

    public static List<File> getAllFolders(String path){
        List<File> files = getAllFiles(path);
        List<File> folders = new ArrayList<File>();
        for(File file : files){
            if(file.isDirectory()) folders.add(file);
        }
        return folders;
    }

    public static boolean hasSubFolders(File folder){
        return getAllFolders(folder.getAbsolutePath()).size() > 0;
    }

    public static String getFolder(String path){
        String fullPath = path.substring(0, path.lastIndexOf("/"));
        return getCorrectedPath(fullPath);
    }

    public static String getCorrectedFolder(String folder){
        return getCorrectedPath(folder);
    }

    public static String getFilename(String path){
        return path.substring(path.lastIndexOf("/")+1, path.length());
    }

    public static String getPath(String folder, String filename){
        return folder +"/"+ filename;
    }

    public static String getPathOnDevice(String folder, String filename){
        return STORAGE_ROOT + getPath(folder, filename);
    }

    public static String getCorrectedPath(String path){
        return path.replace(STORAGE_ROOT, "");
    }

    public static String[] getPreviousAndNextPaths(String photoFolder, String photoFilename){
        String folder = getPathOnDevice(photoFolder, "");
        String path = getPathOnDevice(photoFolder, photoFilename);

        List<File> files = getAllFiles(folder);

        int previous = -1;
        int next = -1;

        for(int i=0; i< files.size(); i++ ){
            String currentPath = files.get(i).getAbsolutePath();
            if(currentPath.equals(path)){
                previous = i-1;
                next = i+1;
                break;
            }
        }

        String [] result = new String[]{null, null};
        if(previous >= 0){
            File previousFile = files.get(previous);
            result[0] = previousFile.getAbsolutePath();
        }
        if(next < files.size()){
            File nextFile = files.get(next);
            result[1] = nextFile.getAbsolutePath();
        }

        return result;
    }

}