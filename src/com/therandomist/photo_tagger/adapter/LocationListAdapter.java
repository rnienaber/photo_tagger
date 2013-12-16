package com.therandomist.photo_tagger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.therandomist.photo_tagger.R;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Location;

import java.util.List;

public class LocationListAdapter extends ArrayAdapter<Location> {

    private List<Location> locations;
    private int rowLayout;
    private Area area;

    public LocationListAdapter(Context context, int textViewResourceId, List<Location> items, Area area){
        super(context, textViewResourceId, items);
        this.locations = items;
        this.rowLayout = textViewResourceId;
        this.area = area;
    }

    public void removeAllLocations(){
        locations.clear();
    }

    public void removeLocation(Location location){
        locations.remove(location);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null){
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(rowLayout, null);
        }
        Location location = locations.get(position);
        if(location != null){

            TextView tagName = (TextView) view.findViewById(R.id.location_name);
            if(tagName != null){
                tagName.setText(location.getName());
            }

//            ImageView tagStarImage = (ImageView) view.findViewById(R.id.tag_added);
//            if(tagStarImage != null && photo.hasLocation(tag)){
//                tagStarImage.setImageResource(R.drawable.star);
//            }else if (!photo.hasLocation(tag)){
//                tagStarImage.setImageBitmap(null);
//            }
        }

        return view;
    }
}