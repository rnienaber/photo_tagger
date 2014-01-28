package com.therandomist.photo_tagger.service.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;
import com.therandomist.photo_tagger.model.Location;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.service.FileHelper;

import java.util.ArrayList;
import java.util.List;

public class PhotoRepository extends Repository<Photo>{

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
        super(context, "photo", "name");
    }

    public List<Photo> findByPath(List<String> paths){
        Log.i(HomeActivity.APP_NAME, "Trying to fetch photos by paths");
        SQLiteDatabase db = openReadable();
        Cursor cursor = null;

        List<String> folders = new ArrayList<String>();
        List<String> files = new ArrayList<String>();

        for(String path : paths){
            String photoFolder = FileHelper.getFolder(path);
            String photoFilename = FileHelper.getFilename(path);
            if(!folders.contains(photoFolder)) folders.add(photoFolder);
            if(!files.contains(files)) files.add(photoFilename);
        }

        String folderWhere = "";
        for(String folder : folders){
            if(folderWhere.equalsIgnoreCase("")){
                folderWhere += " '"+folder+"' ";
            }else{
                folderWhere += ", '"+folder+"' ";
            }
        }

        String fileWhere = "";
        for(String file : files){
            if(fileWhere.equalsIgnoreCase("")){
                fileWhere += " '"+file+"' ";
            }else{
                fileWhere += ", '"+file+"' ";
            }
        }

        String where = "folder in(" + folderWhere + ") AND filename in (" +fileWhere +")";

        List<Photo> results = new ArrayList<Photo>();
        try{
            cursor = db.query(true, tableName, null, where, null, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if(cursor.getCount() > 0 && cursor.moveToFirst()){
                    do{
                        results.add(getFromCursor(cursor));
                    }while(cursor.moveToNext());
                }
            }
        }finally {
            if(cursor != null){
                cursor.close();
            }
            db.close();
        }

        return results;
    }

    public Photo findByPath(String folder, String filename){
        Log.i(HomeActivity.APP_NAME, "Trying to fetch photo: "+folder+" "+filename);
        SQLiteDatabase db = openReadable();
        Cursor cursor = null;

        String where = "folder='" + folder + "' AND filename='" +filename +"'";

        try{
            cursor = db.query(true, tableName, null, where, null, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                return getFromCursor(cursor);
            }
        }finally {
            if(cursor != null){
                cursor.close();
            }
            db.close();
        }

        return null;
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
