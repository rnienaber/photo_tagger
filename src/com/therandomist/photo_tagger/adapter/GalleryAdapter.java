package com.therandomist.photo_tagger.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.therandomist.photo_tagger.PhotoActivity;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.service.FileHelper;

import java.util.List;

public class GalleryAdapter extends BaseAdapter {
    private Context context;
    private List<Photo> photos;

    public GalleryAdapter(Context c, List<Photo> photos) {
        context = c;
        this.photos = photos;
    }

    public int getCount() {
        return photos.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, final View convertView, ViewGroup parent) {
        final ImageView photoView;
        if (convertView == null) {
            photoView = new ImageView(context);
            photoView.setLayoutParams(new GridView.LayoutParams(90, 90));
            photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            photoView.setPadding(2, 2, 2, 2);
        } else {
            photoView = (ImageView) convertView;
        }

        final Photo photo = photos.get(position);
        final String path = FileHelper.getPathOnDevice(photo.getPath());

        photoView.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                Intent i = new Intent(photoView.getContext(), PhotoActivity.class);
                i.putExtra("photoPath", path);
                photoView.getContext().startActivity(i);
                return false;
            }
        });

        BitmapDrawable d = new BitmapDrawable(context.getResources(), path);
        photoView.setImageDrawable(d);
        return photoView;
    }
}