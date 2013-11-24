package com.therandomist.photo_tagger;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import com.therandomist.photo_tagger.adapter.FileListAdapter;
import com.therandomist.photo_tagger.model.Category;
import com.therandomist.photo_tagger.service.FileService;

import java.io.File;
import java.util.*;

public class ManagePhotos extends ExpandableListActivity {
    private static final String KEY1 = "KEY1";


    private FileListAdapter adapter;
    private FileService fileService;
    private List<File> files;

    private List<Map<String, String>> groupData;
    private List<List<File>> childData;

    private String currentPath = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileService = new FileService();
        groupData = new ArrayList<Map<String, String>>();
        childData = new ArrayList<List<File>>();

        loadFolders();

        adapter = new FileListAdapter(
                this,
                groupData,
                R.layout.folder_row,         //group layout
                new String[] { KEY1 },                            //group from
                new int[] { R.id.folder_name, android.R.id.text2 },   //group to
                childData,
                R.layout.file_row         //child layout
        );

        setContentView(R.layout.manage_photos);
        setListAdapter(adapter);
    }

    public String getFilePath(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            String folderToLoad = (String)bundle.get("folder");
            if(folderToLoad != null && folderToLoad != ""){
                currentPath = folderToLoad;
            }
        }

        if(currentPath != ""){
            return currentPath;
        }else{
            return FileService.ROOT;
        }
    }

    public void loadFolders(){
        files = fileService.getAllFiles(getFilePath());

        groupData.clear();
        childData.clear();

        for(File file : files){
            Map<String, String> parentMap = new HashMap<String, String>();
            groupData.add(parentMap);
            parentMap.put(KEY1, file.getName());

            List<File> children = Arrays.asList(file.listFiles());
            Collections.sort(children);

            childData.add(children);
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        super.onChildClick(parent, v, groupPosition, childPosition, id);
        File file = adapter.getChild(groupPosition, childPosition);

        if(file != null){

            if(file.isFile()){
                Intent i = new Intent(this, PhotoActivity.class);
                i.putExtra("photoPath", file.getAbsolutePath());
                startActivity(i);
            }else{
                Intent i = new Intent(this, ManagePhotos.class);
                i.putExtra("folder", file.getAbsolutePath());
                startActivity(i);
            }
        }


        return true;
    }
}