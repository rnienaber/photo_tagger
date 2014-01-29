package com.therandomist.photo_tagger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import com.therandomist.photo_tagger.R;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.service.FileHelper;

import java.io.File;
import java.util.List;
import java.util.Map;

public class FileListAdapter extends SimpleExpandableListAdapter {

    Context context;
    List<? extends List<File>> childData;
    Map<String, Photo> photoMap;

    public FileListAdapter(Context context,
                           List<? extends Map<String, ?>> groupData,
                           int groupLayout,
                           String[] groupFrom,
                           int[] groupTo,
                           List<? extends List<? extends Map<String, ?>>> childData,
                           int childLayout,
                           String[] childFrom,
                           int[] childTo) {

        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);
        this.context = context;
    }

    public FileListAdapter(Context context,
                           List<? extends Map<String, ?>> groupData,
                           String[] groupFrom,
                           int[] groupTo,
                           List<? extends List<File>> childData, Map<String, Photo> photos) {

        super(context, groupData, R.layout.folder_row, groupFrom, groupTo, null, R.layout.file_row, null, null);
        this.childData = childData;
        this.context = context;
        this.photoMap = photos;
    }

    public void setChildData(List<? extends List<File>> childData){
        this.childData = childData;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childData.get(groupPosition) != null ? childData.get(groupPosition).size() : 0;
    }

    @Override
    public File getChild(int groupPosition, int childPosition) {
        return childData.get(groupPosition).get(childPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.file_row, null);
        }

        File child = getChild(groupPosition, childPosition);

        String path = child.getAbsolutePath();
        Photo photo = photoMap.get(FileHelper.getCorrectedPath(path));

        if(child != null){
            TextView name = (TextView) v.findViewById(R.id.child_name);
            if(name != null){
                name.setText(child.getName());
            }

            ImageView starImage = (ImageView) v.findViewById(R.id.photo_tagged);
            if(starImage != null && photo != null){
                if(photo.hasData()){
                    starImage.setImageResource(R.drawable.star);
                }else if (!photo.hasData()){
                    starImage.setImageBitmap(null);
                }
            }

            ImageView earthImage = (ImageView) v.findViewById(R.id.photo_has_location);
            if(starImage != null && photo != null){
                if(photo.hasLocation()){
                    earthImage.setImageResource(R.drawable.world);
                }else if (!photo.hasLocation()){
                    earthImage.setImageBitmap(null);
                }
            }
        }

        return v;
    }
}
