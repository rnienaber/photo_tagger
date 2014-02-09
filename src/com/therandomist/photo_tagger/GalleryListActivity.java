package com.therandomist.photo_tagger;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.therandomist.photo_tagger.adapter.FolderListAdapter;
import com.therandomist.photo_tagger.service.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryListActivity extends ListActivity {

    private FolderListAdapter adapter;
    private List<File> folders;

    private String currentPath = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_list);

        loadFolders();
        adapter = new FolderListAdapter(this, R.layout.gallery_folder_row, folders);
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
            return null;
        }
    }

    public void loadFolders(){
        String path = getFilePath();

        if(path != null){
            folders = FileHelper.getAllFolders(path);
        }else{
            folders = new ArrayList<File>();
            folders.add(new File(FileHelper.ROOT));
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        File folder = adapter.getItem(position);
        if(FileHelper.hasSubFolders(folder)){
            Intent i = new Intent(this, GalleryListActivity.class);
            i.putExtra("folder", folder.getAbsolutePath());
            startActivity(i);
        }else{
            Intent i = new Intent(this, GalleryPickerActivity.class);
            i.putExtra("folder", folder.getAbsolutePath());
            startActivity(i);
        }
    }
}