package com.therandomist.photo_tagger.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.therandomist.photo_tagger.GalleryPickerActivity;
import com.therandomist.photo_tagger.R;
import com.therandomist.photo_tagger.service.FileHelper;

import java.io.File;
import java.util.List;

public class FolderListAdapter extends ArrayAdapter<File> {

    private List<File> folders;
    private int rowLayout;

    public FolderListAdapter(Context context, int textViewResourceId, List<File> items){
        super(context, textViewResourceId, items);
        this.folders = items;
        this.rowLayout = textViewResourceId;
    }

    public void removeAllTags(){
        folders.clear();
    }

    public void removeTag(String folder){
        folders.remove(folder);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null){
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(rowLayout, null);
        }
        final File folder = folders.get(position);
        if(folder != null){
            TextView name = (TextView) view.findViewById(R.id.folder_name);
            if(name != null){
                name.setText(folder.getName());
            }

            if(!FileHelper.hasSubFolders(folder)){
                Button viewButton = (Button) view.findViewById(R.id.view_button);
                viewButton.setVisibility(View.INVISIBLE);
            }
        }

        Button viewButton = (Button) view.findViewById(R.id.view_button);
        viewButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent i = new Intent(getContext(), GalleryPickerActivity.class);
                i.putExtra("folder", folder.getAbsolutePath());
                getContext().startActivity(i);
            }
        });

        return view;
    }
}