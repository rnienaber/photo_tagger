package com.therandomist.photo_tagger.service.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    private static final String DATABASE_NAME = "photo_tagger";
    private static final int DATABASE_VERSION = 1;

    protected static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CategoryDBAdapter.DATABASE_CREATE);
            db.execSQL(TagDBAdapter.DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("DBAdapter", "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS "+ CategoryDBAdapter.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "+ TagDBAdapter.DATABASE_TABLE);

            onCreate(db);
        }
    }
}