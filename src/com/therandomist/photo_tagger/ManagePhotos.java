package com.therandomist.photo_tagger;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import com.therandomist.photo_tagger.adapter.FileListAdapter;
import com.therandomist.photo_tagger.service.FileHelper;

import java.io.File;
import java.util.*;

public class ManagePhotos extends ExpandableListActivity {
    private static final String KEY1 = "KEY1";


    private FileListAdapter adapter;
    private FileHelper fileHelper;
    private List<File> files;

    private List<Map<String, String>> groupData;
    private List<List<File>> childData;

    private String currentPath = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileHelper = new FileHelper();
        groupData = new ArrayList<Map<String, String>>();
        childData = new ArrayList<List<File>>();

        loadFolders();

        adapter = new FileListAdapter(
                this,
                groupData,
                new String[] { KEY1 },                            //group from
                new int[] { R.id.folder_name, android.R.id.text2 },   //group to
                childData
        );

        setContentView(R.layout.manage_photos);
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

        for(File file : files){
            if(!file.isFile()){
                Map<String, String> parentMap = new HashMap<String, String>();
                groupData.add(parentMap);
                parentMap.put(KEY1, file.getName());

                List<File> children = Arrays.asList(file.listFiles());
                Collections.sort(children);
                childData.add(children);
            }
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