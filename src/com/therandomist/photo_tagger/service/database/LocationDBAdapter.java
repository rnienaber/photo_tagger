package com.therandomist.photo_tagger.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationDBAdapter {

    public static final String KEY_NAME = "name";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_AREA_ID = "area_id";
    public static final String KEY_ROW_ID = "_id";

    public static final String DATABASE_TABLE = "location";

    public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
            + " name text not null,"
            + " latitude real not null, "
            + " longitude real not null, "
            + " area_id integer not null); ";

    private DBAdapter.DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private final Context context;

    public LocationDBAdapter(Context context) {
        this.context = context;
    }

    public LocationDBAdapter open() throws SQLException {
        dbHelper = new DBAdapter.DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }


    public Location getLocation(Long locationId){
        Cursor cursor = fetchLocation(locationId);
        Location location = null;

        if(cursor != null){
            location = getLocation(cursor);
            cursor.close();
        }
        return location;
    }

    public Location getLocation(String name){
        name = name.trim();
        Cursor cursor = fetchLocation(name);
        Location location = null;

        if(cursor != null){
            location = getLocation(cursor);
            cursor.close();
        }
        return location;
    }

    public List<Location> getAllLocations(){

        Log.i(HomeActivity.APP_NAME, "getting all locations");

        List<Location> locations = new ArrayList<Location>();
        Cursor cursor = fetchAllLocations();

        if(cursor.getCount() >0 && cursor.moveToFirst()){
            do{
                locations.add(getLocation(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return locations;
    }

    public List<Location> getAllLocationsForArea(Area area){

        Log.i(HomeActivity.APP_NAME, "getting all locations for area");

        List<Location> locations = new ArrayList<Location>();
        Cursor cursor = fetchAllLocationsForArea(area.getId());

        if(cursor.getCount() >0 && cursor.moveToFirst()){
            do{
                locations.add(getLocation(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return locations;
    }

    protected Location getLocation(Cursor cursor){

        String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        Long id = cursor.getLong(cursor.getColumnIndex(KEY_ROW_ID));
        Long areaId = cursor.getLong(cursor.getColumnIndex(KEY_AREA_ID));
        Double latitude = cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE));
        Double longitude = cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE));

        return new Location(id, name, latitude, longitude, areaId);
    }

    protected Cursor fetchLocation(Long locationId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME,
                        KEY_AREA_ID,
                        KEY_LATITUDE,
                        KEY_LONGITUDE
                }, KEY_ROW_ID + "=" + locationId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchLocation(String name) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME,
                        KEY_AREA_ID,
                        KEY_LATITUDE,
                        KEY_LONGITUDE
                }, KEY_NAME + "='" + name+"'", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchAllLocationsForArea(Long areaId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME,
                        KEY_AREA_ID,
                        KEY_LATITUDE,
                        KEY_LONGITUDE
                }, KEY_AREA_ID + "=" + areaId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchAllLocations() throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME,
                        KEY_AREA_ID,
                        KEY_LATITUDE,
                        KEY_LONGITUDE
                }, null, null, null, null, KEY_NAME, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long addLocation(Location location) {
        Log.i(HomeActivity.APP_NAME, "adding location: " + location.getName());
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, location.getName());
        values.put(KEY_AREA_ID, location.getArea().getId());
        values.put(KEY_LATITUDE, location.getLatitude());
        values.put(KEY_LONGITUDE, location.getLongitude());
        return db.insert(DATABASE_TABLE, null, values);
    }

    public void deleteAllLocations(){
        open();
        db.execSQL("delete from "+DATABASE_TABLE);
        close();
    }
}