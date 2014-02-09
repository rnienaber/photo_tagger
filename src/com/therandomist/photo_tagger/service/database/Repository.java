package com.therandomist.photo_tagger.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.therandomist.photo_tagger.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class Repository<T>{

    protected final String tableName;
    private final DatabaseHelper helper;
    private final String defaultOrderBy;

    public Repository(Context context, String tableName, String defaultOrderBy){
        this.helper = new DatabaseHelper(context);
        this.tableName = tableName;
        this.defaultOrderBy = defaultOrderBy;
    }

    public SQLiteDatabase openReadable() throws SQLException {
        return helper.getReadableDatabase();
    }

    protected SQLiteDatabase openWritable() throws SQLException {
        return helper.getWritableDatabase();
    }

    public T find(Long id){
        SQLiteDatabase db = openReadable();
        Cursor cursor = null;

        try{
            cursor = db.query(true, tableName, null, "_id=" + id, null, null, null, null, null);
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

    public List<T> findAllBy(String columnName, Object value, SQLiteDatabase db){
        Log.i(HomeActivity.APP_NAME, "Find all by: "+columnName+ " = "+ value);

        List<T> results = new ArrayList<T>();
        Cursor cursor = null;

        try{
            String where = null;
            if(columnName != null){
                if(value instanceof String){
                    where = columnName+"='"+value.toString()+"' COLLATE NOCASE";
                }else if(value instanceof Number){
                    where = columnName+"="+value.toString();
                }
            }
            cursor = db.query(true, tableName, null, where, null, null, null, defaultOrderBy, null);

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
        }
        return results;
    }

    public List<T> findAllBy(String columnName, Object value){
        SQLiteDatabase db = openReadable();
        try{
            return findAllBy(columnName,  value, db);
        }finally {
            db.close();
        }
    }

    public List<T> findAll(SQLiteDatabase database){
        return findAllBy(null, null, database);
    }

    public List<T> findAll(){
        SQLiteDatabase db = openReadable();
        try{
            return findAll(db);
        }finally {
            db.close();
        }
    }

    protected List<T> findUsingWhere(String where){
        SQLiteDatabase db = openReadable();
        Cursor cursor = null;
        List<T> results = new ArrayList<T>();
        try{
            cursor = db.query(true, tableName, null, where, null, null, null, null, null);
            if (cursor != null) {
                results = getManyFromCursor(cursor);
            }
        }finally {
            if(cursor != null){
                cursor.close();
            }
            db.close();
        }
        return results;
    }

    protected List<T> getManyFromCursor(Cursor cursor){
        List<T> results = new ArrayList<T>();
        cursor.moveToFirst();
        if(cursor.getCount() > 0 && cursor.moveToFirst()){
            do{
                results.add(getFromCursor(cursor));
            }while(cursor.moveToNext());
        }
        return results;
    }

    protected String getInClause(List<String> items){
        String inClause = "";
        for(String item : items){
            if(inClause.equalsIgnoreCase("")){
                inClause += " '"+item+"' ";
            }else{
                inClause += ", '"+item+"' ";
            }
        }
        return inClause;
    }

    protected String getInClauseIds(List<Long> ids){
        String inClause = "";
        for(Long id : ids){
            if(inClause.equalsIgnoreCase("")){
                inClause += " "+id+" ";
            }else{
                inClause += ", "+id+" ";
            }
        }
        return inClause;
    }

    public Long insert(T item){
        if (item == null) return null;
        SQLiteDatabase db = openWritable();
        try{
            ContentValues values = new ContentValues();
            buildContentValues(item, values);
            return db.insert(tableName, null, values);
        }finally {
            db.close();
        }
    }

    public boolean update(T item, Long id){
        if (item == null) return false;
        SQLiteDatabase db = openWritable();
        try{
            ContentValues values = new ContentValues();
            buildContentValues(item, values);
            return db.update(tableName, values, "_id=" + id, null) > 0;
        }finally {
            db.close();
        }
    }

    protected abstract void buildContentValues(T item, ContentValues values);

    protected abstract T getFromCursor(Cursor cursor);

}
