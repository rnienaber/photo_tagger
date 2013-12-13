package com.therandomist.photo_tagger.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;
import com.therandomist.photo_tagger.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDBAdapter {

    public static final String KEY_NAME = "name";
    public static final String KEY_ROW_ID = "_id";

    public static final String DATABASE_TABLE = "category";

    public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
            + " name text not null); ";

    private DBAdapter.DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private final Context context;

    public CategoryDBAdapter(Context context) {
        this.context = context;
    }

    public CategoryDBAdapter open() throws SQLException {
        dbHelper = new DBAdapter.DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }


    public Category getCategory(Long categoryId){
        Cursor cursor = fetchCategory(categoryId);
        Category category = null;

        if(cursor != null){
            category = getCategory(cursor);
            cursor.close();
        }

        return category;
    }

    public Category getCategory(String name){
        Cursor cursor = fetchCategory(name);
        Category category = null;

        if(cursor != null){
            category = getCategory(cursor);
            cursor.close();
        }

        return category;
    }

    public List<Category> getAllCategories(){

        Log.i(HomeActivity.APP_NAME, "getting all categories");

        List<Category> categories = new ArrayList<Category>();
        Cursor cursor = fetchAllCategories();

        if(cursor.getCount() >0 && cursor.moveToFirst()){
            do{
                categories.add(getCategory(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return categories;
    }

    protected Category getCategory(Cursor cursor){

        String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        Long id = cursor.getLong(cursor.getColumnIndex(KEY_ROW_ID));

        return new Category(id, name);
    }

    protected Cursor fetchCategory(Long categoryId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME
                }, KEY_ROW_ID + "=" + categoryId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchCategory(String name) throws SQLException {
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

    protected Cursor fetchAllCategories() throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME
                }, null, null, null, null, KEY_ROW_ID, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long addCategory(Category category) {
        Log.i(HomeActivity.APP_NAME, "adding category: " + category.getName());
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());
        return db.insert(DATABASE_TABLE, null, values);
    }

    public void deleteAllCategories(){
        open();
        db.execSQL("delete from "+DATABASE_TABLE);
        close();
    }
}