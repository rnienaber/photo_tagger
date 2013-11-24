package com.therandomist.photo_tagger.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;
import com.therandomist.photo_tagger.model.GPSLocation;

import java.util.ArrayList;
import java.util.List;

public class GPSLocationDBAdapter {

    public static final String KEY_NAME = "name";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ROW_ID = "_id";

    public static final String DATABASE_TABLE = "gpslocation";

    public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
            + " name text unique not null, "
            + " latitude real not null, "
            + " longitude real not null); ";

    private DBAdapter.DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private final Context context;

    public GPSLocationDBAdapter(Context context) {
        this.context = context;
    }

    public GPSLocationDBAdapter open() throws SQLException {
        dbHelper = new DBAdapter.DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }


    public GPSLocation getGPSLocation(Long GPSLocationId){
        Cursor cursor = fetchGPSLocation(GPSLocationId);
        GPSLocation GPSLocation = null;

        if(cursor != null){
            GPSLocation = getGPSLocation(cursor);
            cursor.close();
        }

        return GPSLocation;
    }

    public GPSLocation getGPSLocation(String name){
        if(name  == null || name == "")
            return null;

        Cursor cursor = fetchGPSLocation(name);
        GPSLocation GPSLocation = null;

        if(cursor != null){
            GPSLocation = getGPSLocation(cursor);
            cursor.close();
        }

        return GPSLocation;
    }

    public List<GPSLocation> getAllGPSLocations(){

        Log.i(HomeActivity.APP_NAME, "getting all locations");

        List<GPSLocation> gpsLocations = new ArrayList<GPSLocation>();
        Cursor cursor = fetchAllGPSLocations();

        if(cursor.getCount() >0 && cursor.moveToFirst()){
            do{
                gpsLocations.add(getGPSLocation(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return gpsLocations;
    }

    protected GPSLocation getGPSLocation(Cursor cursor){

        String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        Long id = cursor.getLong(cursor.getColumnIndex(KEY_ROW_ID));
        Double latitude = cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE));
        Double longitude = cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE));

        return new GPSLocation(id, name, latitude, longitude);
    }

    protected Cursor fetchGPSLocation(Long GPSLocationId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME,
                        KEY_LATITUDE,
                        KEY_LONGITUDE
                }, KEY_ROW_ID + "=" + GPSLocationId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchGPSLocation(String name) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME,
                        KEY_LATITUDE,
                        KEY_LONGITUDE
                }, KEY_NAME + "=" + name, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchAllGPSLocations() throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME,
                        KEY_LATITUDE,
                        KEY_LONGITUDE
                }, null, null, null, null, KEY_NAME, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long addGPSLocation(GPSLocation GPSLocation) {
        Log.i(HomeActivity.APP_NAME, "adding GPSLocation: " + GPSLocation.getName());
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, GPSLocation.getName());
        values.put(KEY_LATITUDE, GPSLocation.getLatitude());
        values.put(KEY_LONGITUDE, GPSLocation.getLongitude());
        return db.insert(DATABASE_TABLE, null, values);
    }

    public void deleteAllGPSLocations(){
        open();
        db.execSQL("delete from "+DATABASE_TABLE);
        close();
    }
}