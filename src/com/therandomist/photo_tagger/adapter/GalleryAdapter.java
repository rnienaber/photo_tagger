package com.therandomist.photo_tagger.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.therandomist.photo_tagger.HomeActivity;
import com.therandomist.photo_tagger.PhotoActivity;
import com.therandomist.photo_tagger.R;
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
        final View photoView;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            photoView = layoutInflater.inflate(R.layout.gallery_item, null);
            photoView.setLayoutParams(new GridView.LayoutParams(100 , 100));
            photoView.setPadding(2, 2, 2, 2);
        } else {
            photoView = (View) convertView;
        }

        Photo photo = photos.get(position);
        Log.i(HomeActivity.APP_NAME, "Photo: "+photo.getFilename());
        final String path = FileHelper.getPathOnDevice(photo.getPath());

        TextView nameView = (TextView)photoView.findViewById(R.id.photo_name);
        nameView.setText(photo.getFilename());

        ImageView imageView = (ImageView)photoView.findViewById(R.id.photo_image);
        BitmapDrawable d = new BitmapDrawable(context.getResources(), path);
        imageView.setImageDrawable(d);

        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                Intent i = new Intent(photoView.getContext(), PhotoActivity.class);
                i.putExtra("photoPath", path);
                photoView.getContext().startActivity(i);
                return false;
            }
        });
        return photoView;
    }
}