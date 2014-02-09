package com.therandomist.photo_tagger;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import com.therandomist.photo_tagger.adapter.FileListAdapter;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.service.FileHelper;
import com.therandomist.photo_tagger.service.PhotoService;

import java.io.File;
import java.util.*;

public class PhotoListActivity extends ExpandableListActivity {
    private static final String NAME = "NAME";
    private static final String NUMBER_PHOTOS = "NUMBER_PHOTOS";
    private static final String NUMBER_TAGGED = "NUMBER_TAGGED";

    private FileListAdapter adapter;
    private FileHelper fileHelper;
    private List<File> files;
    private Map<String, Photo> photoMap;

    private List<Map<String, String>> groupData;
    private List<List<File>> childData;

    private String currentPath = "";
    private PhotoService photoService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileHelper = new FileHelper();
        photoService = new PhotoService(getApplicationContext());
        groupData = new ArrayList<Map<String, String>>();
        childData = new ArrayList<List<File>>();
        photoMap = new HashMap<String, Photo>();

        loadFolders();

        adapter = new FileListAdapter(
                this,
                groupData,
                childData,
                photoMap
        );

        setContentView(R.layout.simple_expandable_list);
        setListAdapter(adapter);
    }

    public String getFilePath(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            String folderToLoad = (String)bundle.get("folder");
            if(folderToLoad != null && !folderToLoad.equals("")){
                currentPath = folderToLoad;
            }
        }

        if(!currentPath.equals("")){
            return currentPath;
        }else{
            return FileHelper.ROOT;
        }
    }

    public void loadFolders(){
        files = fileHelper.getAllFiles(getFilePath());

        groupData.clear();
        childData.clear();
        photoMap.clear();

        for(File file : files){
            if(file.isDirectory()){
                Map<String, String> parentMap = new HashMap<String, String>();
                groupData.add(parentMap);
                parentMap.put(NAME, file.getName());

                List<File> children = Arrays.asList(file.listFiles());
                Collections.sort(children);
                childData.add(children);

                List<Photo> photos = photoService.getPhotosForFolder(file.getAbsolutePath());
                int completed = 0;
                for(Photo photo : photos){
                    photoMap.put(FileHelper.getPath(photo.getFolder(), photo.getFilename()), photo);
                    if(photo.hasData() && ( photo.isDeleted() || photo.hasLocation())) completed++;
                }

                parentMap.put(NUMBER_PHOTOS, photos.size()+"");
                parentMap.put(NUMBER_TAGGED, completed+"");
            }
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        super.onChildClick(parent, v, groupPosition, childPosition, id);
        File file = adapter.getChild(groupPosition, childPosition);
        String path = file.getAbsolutePath();

        if(file != null){
            if(file.isFile()){
                Intent i = new Intent(this, PhotoActivity.class);
                i.putExtra("photoPath", path);
                startActivity(i);
            }else{
                Intent i = new Intent(this, PhotoListActivity.class);
                i.putExtra("folder", path);
                startActivity(i);
            }
        }
        return true;
    }
}