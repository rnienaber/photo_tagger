package com.therandomist.photo_tagger.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;
import com.therandomist.photo_tagger.model.Area;
import com.therandomist.photo_tagger.model.Country;

import java.util.ArrayList;
import java.util.List;

public class AreaDBAdapter {

    public static final String KEY_NAME = "name";
    public static final String KEY_COUNTRY_ID = "country_id";
    public static final String KEY_ROW_ID = "_id";

    public static final String DATABASE_TABLE = "area";

    public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
            + " name text not null, "
            + " country_id integer not null); ";

    private DBAdapter.DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private final Context context;

    public AreaDBAdapter(Context context) {
        this.context = context;
    }

    public AreaDBAdapter open() throws SQLException {
        dbHelper = new DBAdapter.DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }


    public Area getArea(Long areaId){
        Cursor cursor = fetchArea(areaId);
        Area area = null;

        if(cursor != null){
            area = getArea(cursor);
            cursor.close();
        }

        return area;
    }

    public Area getArea(String name){
        Cursor cursor = fetchArea(name.trim());
        Area area = null;

        if(cursor != null){
            area = getArea(cursor);
            cursor.close();
        }

        return area;
    }

    public List<Area> getAllAreas(){

        Log.i(HomeActivity.APP_NAME, "getting all areas");

        List<Area> areas = new ArrayList<Area>();
        Cursor cursor = fetchAllAreas();

        if(cursor.getCount() >0 && cursor.moveToFirst()){
            do{
                areas.add(getArea(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return areas;
    }

    public List<Area> getAllAreasForCountry(Country country){
        Log.i(HomeActivity.APP_NAME, "getting all areas for country");

        List<Area> areas = new ArrayList<Area>();
        Cursor cursor = fetchAllAreasForCountry(country.getId());

        if(cursor.getCount() >0 && cursor.moveToFirst()){
            do{
                areas.add(getArea(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return areas;
    }

    public List<Area> getAllAreasForCountry(Long countryId){
        Log.i(HomeActivity.APP_NAME, "getting all areas for country");

        List<Area> areas = new ArrayList<Area>();
        Cursor cursor = fetchAllAreasForCountry(countryId);

        if(cursor.getCount() >0 && cursor.moveToFirst()){
            do{
                areas.add(getArea(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return areas;
    }

    protected Area getArea(Cursor cursor){

        String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        Long id = cursor.getLong(cursor.getColumnIndex(KEY_ROW_ID));

        return new Area(id, name);
    }

    protected Cursor fetchArea(Long areaId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME
                }, KEY_ROW_ID + "=" + areaId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchArea(String name) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME
                }, KEY_NAME + "='" + name + "' COLLATE NOCASE", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchAllAreas() throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME
                }, null, null, null, null, KEY_NAME, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchAllAreasForCountry(Long countryId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME,
                        KEY_COUNTRY_ID
                }, KEY_COUNTRY_ID + "=" + countryId, null, null, null, KEY_NAME, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long addArea(Area area) {
        Log.i(HomeActivity.APP_NAME, "adding area: " + area.getName());
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, area.getName());
        values.put(KEY_COUNTRY_ID, area.getCountry().getId());
        return db.insert(DATABASE_TABLE, null, values);
    }

    public void deleteAllAreas(){
        open();
        db.execSQL("delete from "+DATABASE_TABLE);
        close();
    }
}