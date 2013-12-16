package com.therandomist.photo_tagger.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;
import com.therandomist.photo_tagger.model.Country;

import java.util.ArrayList;
import java.util.List;

public class CountryDBAdapter {

    public static final String KEY_NAME = "name";
    public static final String KEY_ROW_ID = "_id";

    public static final String DATABASE_TABLE = "country";

    public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
            + " name text not null); ";

    private DBAdapter.DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private final Context context;

    public CountryDBAdapter(Context context) {
        this.context = context;
    }

    public CountryDBAdapter open() throws SQLException {
        dbHelper = new DBAdapter.DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }


    public Country getCountry(Long countryId){
        Cursor cursor = fetchCountry(countryId);
        Country country = null;

        if(cursor != null){
            country = getCountry(cursor);
            cursor.close();
        }

        return country;
    }

    public Country getCountry(String name){
        Cursor cursor = fetchCountry(name.trim());
        Country country = null;

        if(cursor != null){
            country = getCountry(cursor);
            cursor.close();
        }

        return country;
    }

    public List<Country> getAllCountries(){

        Log.i(HomeActivity.APP_NAME, "getting all countries");

        List<Country> countries = new ArrayList<Country>();
        Cursor cursor = fetchAllCountries();

        if(cursor.getCount() >0 && cursor.moveToFirst()){
            do{
                countries.add(getCountry(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return countries;
    }

    protected Country getCountry(Cursor cursor){

        String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        Long id = cursor.getLong(cursor.getColumnIndex(KEY_ROW_ID));

        return new Country(id, name);
    }

    protected Cursor fetchCountry(Long countryId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME
                }, KEY_ROW_ID + "=" + countryId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchCountry(String name) throws SQLException {
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

    protected Cursor fetchAllCountries() throws SQLException {
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

    public long addCountry(Country country) {
        Log.i(HomeActivity.APP_NAME, "adding country: " + country.getName());
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, country.getName());
        return db.insert(DATABASE_TABLE, null, values);
    }

    public void deleteAllCountries(){
        open();
        db.execSQL("delete from "+DATABASE_TABLE);
        close();
    }
}