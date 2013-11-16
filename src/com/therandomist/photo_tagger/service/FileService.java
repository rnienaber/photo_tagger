package com.therandomist.photo_tagger.service;

import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileService {

    public List<File> getAllFiles(String path){
        File file = new File(path);
        Log.i(HomeActivity.APP_NAME, "Loading file: "+ file.getAbsolutePath());
        return Arrays.asList(file.listFiles());
    }
}