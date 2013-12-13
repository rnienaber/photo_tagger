package com.therandomist.photo_tagger.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.therandomist.photo_tagger.HomeActivity;
import com.therandomist.photo_tagger.R;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.model.Tag;

import java.util.List;

public class TagListAdapter extends ArrayAdapter<Tag> {

    private List<Tag> tags;
    private int rowLayout;
    private Photo photo;

    public TagListAdapter(Context context, int textViewResourceId, List<Tag> items, Photo photo){
        super(context, textViewResourceId, items);
        this.tags = items;
        this.rowLayout = textViewResourceId;
        this.photo = photo;
    }

    public void removeAllTags(){
        tags.clear();
    }

    public void removeTag(Tag tag){
        tags.remove(tag);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null){
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(rowLayout, null);
        }
        Tag tag = tags.get(position);
        if(tag != null){

            TextView tagName = (TextView) view.findViewById(R.id.tag_name);
            if(tagName != null){
                tagName.setText(tag.getName());
            }

            ImageView tagStarImage = (ImageView) view.findViewById(R.id.tag_added);
            if(tagStarImage != null && photo.hasTag(tag)){
                tagStarImage.setImageResource(R.drawable.star);
            }else if (!photo.hasTag(tag)){
                tagStarImage.setImageBitmap(null);
            }
        }

        return view;
    }
}