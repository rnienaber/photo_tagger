package com.therandomist.photo_tagger.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;
import com.therandomist.photo_tagger.model.Category;
import com.therandomist.photo_tagger.model.GPSLocation;
import com.therandomist.photo_tagger.model.Photo;
import com.therandomist.photo_tagger.model.Tag;
import com.therandomist.photo_tagger.service.FileService;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.List;

public class PhotoDBAdapter {

    public static final String KEY_ROW_ID = "_id";
    public static final String KEY_FILENAME = "filename";
    public static final String KEY_FOLDER = "folder";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_PEOPLE = "people";
    public static final String KEY_KEYWORDS = "keywords";
    public static final String KEY_PRINTING = "printing";

    public static final String DATABASE_TABLE = "photo";

    public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
            + " filename text not null,"
            + " folder text,"
            + " latitude real, "
            + " longitude real, "
            + " people text,"
            + " keywords text,"
            + " printing text); ";

    private DBAdapter.DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private TagDBAdapter tagDBAdapter;
    private GPSLocationDBAdapter gpsLocationDBAdapter;

    private final Context context;

    public PhotoDBAdapter(Context context) {
        this.context = context;
        this.tagDBAdapter = new TagDBAdapter(context);
        this.gpsLocationDBAdapter = new GPSLocationDBAdapter(context);
    }

    public PhotoDBAdapter open() throws SQLException {
        dbHelper = new DBAdapter.DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }


    public Photo getPhoto(Long photoId){
        Cursor cursor = fetchPhoto(photoId);
        Photo photo = null;

        if(cursor != null){
            photo = getPhoto(cursor);
            cursor.close();
        }
        return photo;
    }

    public Photo getPhoto(String path){
        Cursor cursor = fetchPhoto(path);
        Photo photo = null;

        if(cursor != null){
            photo = getPhoto(cursor);
            cursor.close();
        }
        return photo;
    }

    public List<Photo> getAllPhotos(){

        Log.i(HomeActivity.APP_NAME, "getting all photos");

        List<Photo> photos = new ArrayList<Photo>();
        Cursor cursor = fetchAllPhotos();

        if(cursor.getCount() >0 && cursor.moveToFirst()){
            do{
                photos.add(getPhoto(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return photos;
    }

    protected Photo getPhoto(Cursor cursor){
        try{
            cursor.getLong(cursor.getColumnIndex(KEY_ROW_ID));
        }catch(CursorIndexOutOfBoundsException cioobe){
            Log.i(HomeActivity.APP_NAME, "There is no photo to return");
            return null;
        }

        Long id = cursor.getLong(cursor.getColumnIndex(KEY_ROW_ID));

        String filename = cursor.getString(cursor.getColumnIndex(KEY_FILENAME));
        String folder = cursor.getString(cursor.getColumnIndex(KEY_FOLDER));

        Double latitude = cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE));
        Double longitude = cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE));
        GPSLocation gspLocation = null;

        String peopleString = cursor.getString(cursor.getColumnIndex(KEY_PEOPLE));
        String keywordsString = cursor.getString(cursor.getColumnIndex(KEY_KEYWORDS));
        String printingString = cursor.getString(cursor.getColumnIndex(KEY_PRINTING));

        List<Tag> people = convertNamesToTag(peopleString);
        List<Tag> printing = convertNamesToTag(printingString);

        List<Tag> keywords = new ArrayList<Tag>();
        List<Tag> other = new ArrayList<Tag>();

        String[] keywordList = keywordsString.split(",");
        for(String keyword : keywordList){
            Tag tag = getTag(keyword);
            if(tag == null){
                gspLocation = getGPSLocation(keyword);
            }else if(tag.getCategory().getName().toLowerCase() == "keywords"){
                keywords.add(tag);
            }else{
                other.add(tag);
            }
        }

        return new Photo(id, filename, folder, gspLocation, people, keywords, printing, other);
    }

    private List<Tag> convertNamesToTag(String names){
        String[] nameList = names.split(",");
        List<Tag> tags = new ArrayList<Tag>();

        if(nameList.length > 0){
            for(String name : nameList){
                tags.add(getTag(name));
            }
        }
        return tags;
    }

    private Tag getTag(String name){
        if(name == null || name == "")
            return null;

        tagDBAdapter.open();
        Tag tag = tagDBAdapter.getTag(name);
        tagDBAdapter.close();
        return tag;
    }

    private GPSLocation getGPSLocation(String name){
        return gpsLocationDBAdapter.getGPSLocation(name);
    }

    protected Cursor fetchPhoto(Long photoId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_FILENAME,
                        KEY_FOLDER,
                        KEY_LATITUDE,
                        KEY_LONGITUDE,
                        KEY_PEOPLE,
                        KEY_KEYWORDS,
                        KEY_PRINTING
                }, KEY_ROW_ID + "=" + photoId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchPhoto(String path) throws SQLException {

        Log.i(HomeActivity.APP_NAME, "Trying to fetch photo: "+path);

        String folder = FileService.getFolder(path);
        String filename = FileService.getFilename(path);

        String where = KEY_FOLDER + "='" + folder + "' AND " + KEY_FILENAME + "='" +filename +"'";
        Log.i(HomeActivity.APP_NAME, "where: "+where);

        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_FILENAME,
                        KEY_FOLDER,
                        KEY_LATITUDE,
                        KEY_LONGITUDE,
                        KEY_PEOPLE,
                        KEY_KEYWORDS,
                        KEY_PRINTING
                }, where, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    protected Cursor fetchAllPhotos() throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_FILENAME,
                        KEY_FOLDER,
                        KEY_LATITUDE,
                        KEY_LONGITUDE,
                        KEY_PEOPLE,
                        KEY_KEYWORDS,
                        KEY_PRINTING
                }, null, null, null, null, KEY_FILENAME, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long addPhoto(Photo photo) {
        Log.i(HomeActivity.APP_NAME + "/PhotoDBAdapter", "Adding photo: " + photo.getFilename());
        ContentValues values = new ContentValues();

        values.put(KEY_FILENAME, photo.getFilename());
        values.put(KEY_FOLDER, photo.getFolder());
        values.put(KEY_LATITUDE, photo.getLatitude());
        values.put(KEY_LONGITUDE, photo.getLongitude());
        values.put(KEY_PEOPLE, photo.getPeople());
        values.put(KEY_KEYWORDS, photo.getKeywords());
        values.put(KEY_PRINTING, photo.getPrinting());

        return db.insert(DATABASE_TABLE, null, values);
    }

    public boolean updatePhoto(Photo photo){
        Log.i(HomeActivity.APP_NAME + "/PhotoDBAdapter", "Saving photo: " + photo.getFilename());
        ContentValues values = new ContentValues();

        values.put(KEY_LATITUDE, photo.getLatitude());
        values.put(KEY_LONGITUDE, photo.getLongitude());
        values.put(KEY_PEOPLE, photo.getPeople());
        values.put(KEY_KEYWORDS, photo.getKeywords());
        values.put(KEY_PRINTING, photo.getPrinting());

        return db.update(DATABASE_TABLE, values, KEY_ROW_ID + "=" + photo.getId(), null) > 0;
    }

    public void deleteAllPhotos(){
        open();
        db.execSQL("delete from "+DATABASE_TABLE);
        close();
    }
}