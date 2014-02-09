package com.therandomist.photo_tagger.service.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;
import com.therandomist.photo_tagger.model.Location;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.service.FileHelper;

import java.util.List;
import java.util.Map;

public class PhotoRepository extends Repository<Photo>{

    public static final String PRINTING = "printing";

    public static final String DATABASE_CREATE = "create table photo (_id integer primary key autoincrement, "
            + " filename text not null,"
            + " folder text,"
            + " notes text,"
            + " latitude real, "
            + " longitude real, "
            + " location_name text, "
            + " people text,"
            + " keywords text,"
            + " printing text); ";

    public PhotoRepository(Context context) {
        super(context, "photo", "filename");
    }

    public List<Photo> findByTags(Map<String, List<String>> tagList){
        Log.i(HomeActivity.APP_NAME, "Trying to fetch photos by tags");
        String where = "";

        for(Map.Entry<String, List<String>> entry : tagList.entrySet()){
            String tagType = entry.getKey();
            List<String> tagNames = entry.getValue();

            for(String tag : tagNames){
                String clause = tagType +" like '%"+tag+"%' ";

                if(where.equals("")){
                    where = clause;
                }else{
                    where += " AND "+clause;
                }
            }
        }

        return findUsingWhere(where);
    }

    public Photo findByPath(String path){
        String folder = FileHelper.getFolder(path);
        String filename = FileHelper.getFilename(path);

        Log.i(HomeActivity.APP_NAME, "Trying to fetch photo: "+folder+" "+filename);

        String where = "folder='" + folder + "' AND filename='" +filename +"'";

        return findUsingWhere(where).get(0);
    }

    protected Photo getFromCursor(Cursor cursor){
        try{
            cursor.getLong(cursor.getColumnIndex("_id"));
        }catch(CursorIndexOutOfBoundsException cioobe){
            Log.i(HomeActivity.APP_NAME, "There is no photo to return");
            return null;
        }

        Long id = cursor.getLong(cursor.getColumnIndex("_id"));

        String filename = cursor.getString(cursor.getColumnIndex("filename"));
        String folder = cursor.getString(cursor.getColumnIndex("folder"));
        String notes = cursor.getString(cursor.getColumnIndex("notes"));

        Double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
        Double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
        String locationName = cursor.getString(cursor.getColumnIndex("location_name"));
        Location gpsLocation = new Location(locationName, latitude, longitude, null);

        String peopleString = cursor.getString(cursor.getColumnIndex("people"));
        String keywordsString = cursor.getString(cursor.getColumnIndex("keywords"));
        String printingString = cursor.getString(cursor.getColumnIndex("printing"));

        return new Photo(id, filename, folder, notes, gpsLocation, peopleString, keywordsString, printingString);
    }

    @Override
    protected void buildContentValues(Photo photo, ContentValues values) {
        values.put("filename", photo.getFilename());
        values.put("folder", photo.getFolder());
        values.put("notes", photo.getNotes());
        values.put("latitude", photo.getLatitude());
        values.put("longitude", photo.getLongitude());
        values.put("location_name", photo.getLocationName());
        values.put("people", photo.getPeople());
        values.put("keywords", photo.getKeywords());
        values.put("printing", photo.getPrinting());
    }



}
