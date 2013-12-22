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

public class PhotoRepository extends Repository<Photo>{

    public static final String DATABASE_CREATE = "create table photo (_id integer primary key autoincrement, "
            + " filename text not null,"
            + " folder text,"
            + " latitude real, "
            + " longitude real, "
            + " people text,"
            + " keywords text,"
            + " printing text); ";

    public PhotoRepository(Context context) {
        super(context, "photo", "name");
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

        Double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
        Double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
        Location gpsLocation = null;

        String peopleString = cursor.getString(cursor.getColumnIndex("people"));
        String keywordsString = cursor.getString(cursor.getColumnIndex("keywords"));
        String printingString = cursor.getString(cursor.getColumnIndex("printing"));

//        List<Tag> people = convertNamesToTag(peopleString);
//        List<Tag> printing = convertNamesToTag(printingString);
//        List<Tag> keywords = convertNamesToTag(keywordsString);

        return new Photo(id, filename, folder, gpsLocation, peopleString, keywordsString, printingString);
    }

    @Override
    protected void buildContentValues(Photo photo, ContentValues values) {
        values.put("filename", photo.getFilename());
        values.put("folder", photo.getFolder());
        values.put("latitude", photo.getLatitude());
        values.put("longitude", photo.getLongitude());
        values.put("people", photo.getPeople());
        values.put("keywords", photo.getKeywords());
        values.put("printing", photo.getPrinting());
    }


}
