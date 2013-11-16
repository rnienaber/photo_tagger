package com.therandomist.photo_tagger.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;
import com.therandomist.photo_tagger.model.Category;
import com.therandomist.photo_tagger.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagDBAdapter {

    public static final String KEY_NAME = "name";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_ROW_ID = "_id";

    public static final String DATABASE_TABLE = "tag";

    public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
            + " name text not null,"
            + " category_id integer not null); ";

    private DBAdapter.DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private final Context context;

    public TagDBAdapter(Context context) {
        this.context = context;
    }

    public TagDBAdapter open() throws SQLException {
        dbHelper = new DBAdapter.DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }


    public Tag getTag(Long tagId){
        Cursor cursor = fetchTag(tagId);
        Tag tag = null;

        if(cursor != null){
            tag = getTag(cursor);
            cursor.close();
        }
        return tag;
    }

    public Tag getTag(String name){
        Cursor cursor = fetchTag(name);
        Tag tag = null;

        if(cursor != null){
            tag = getTag(cursor);
            cursor.close();
        }
        return tag;
    }

    public List<Tag> getAllTags(){

        Log.i(HomeActivity.APP_NAME, "getting all tags");

        List<Tag> tags = new ArrayList<Tag>();
        Cursor cursor = fetchAllTags();

        if(cursor.getCount() >0 && cursor.moveToFirst()){
            do{
                tags.add(getTag(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return tags;
    }

    public List<Tag> getAllTagsForCategory(Category category){

        Log.i(HomeActivity.APP_NAME, "getting all tags for category");

        List<Tag> tags = new ArrayList<Tag>();
        Cursor cursor = fetchAllTagsForCategory(category.getId());

        if(cursor.getCount() >0 && cursor.moveToFirst()){
            do{
                tags.add(getTag(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return tags;
    }

    protected Tag getTag(Cursor cursor){

        String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        Long id = cursor.getLong(cursor.getColumnIndex(KEY_ROW_ID));
        Long categoryId = cursor.getLong(cursor.getColumnIndex(KEY_CATEGORY_ID));

        return new Tag(id, name, categoryId);
    }

    protected Cursor fetchTag(Long tagId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME,
                        KEY_CATEGORY_ID
                }, KEY_ROW_ID + "=" + tagId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchTag(String name) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME,
                        KEY_CATEGORY_ID
                }, KEY_NAME + "=" + name, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchAllTagsForCategory(Long categoryId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME,
                        KEY_CATEGORY_ID
                }, KEY_CATEGORY_ID + "=" + categoryId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    protected Cursor fetchAllTags() throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROW_ID,
                        KEY_NAME,
                        KEY_CATEGORY_ID
                }, null, null, null, null, KEY_NAME, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long addTag(Tag tag) {
        Log.i(HomeActivity.APP_NAME, "adding tag: " + tag.getName());
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, tag.getName());
        values.put(KEY_CATEGORY_ID, tag.getCategory().getId());
        return db.insert(DATABASE_TABLE, null, values);
    }

    public void deleteAllTags(){
        open();
        db.execSQL("delete from "+DATABASE_TABLE);
        close();
    }
}