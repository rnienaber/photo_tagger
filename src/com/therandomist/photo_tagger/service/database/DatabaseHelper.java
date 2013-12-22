package com.therandomist.photo_tagger.service.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "photo_tagger";
    private static final int DATABASE_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context, Environment.getExternalStorageDirectory()
                + File.separator + "photo_tagger"
                + File.separator + DATABASE_NAME + ".sqlite", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CategoryRepository.DATABASE_CREATE);
        db.execSQL(TagRepository.DATABASE_CREATE);
        db.execSQL(PhotoRepository.DATABASE_CREATE);
        db.execSQL(CountryRepository.DATABASE_CREATE);
        db.execSQL(AreaRepository.DATABASE_CREATE);
        db.execSQL(LocationRepository.DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("DBAdapter", "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS category");
        db.execSQL("DROP TABLE IF EXISTS location");
        db.execSQL("DROP TABLE IF EXISTS country");
        db.execSQL("DROP TABLE IF EXISTS tag");
        db.execSQL("DROP TABLE IF EXISTS photo");
        db.execSQL("DROP TABLE IF EXISTS area");

        onCreate(db);
    }

}
