package com.therandomist.photo_tagger.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.therandomist.photo_tagger.model.Country;

public class CountryRepository extends Repository<Country>{

    public static final String DATABASE_CREATE = "create table country (_id integer primary key autoincrement, "
            + " name text not null); ";

    public CountryRepository(Context context) {
        super(context, "country", "name");
    }

    @Override
    protected Country getFromCursor(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex("name"));
        Long id = cursor.getLong(cursor.getColumnIndex("_id"));

        return new Country(id, name);
    }

    @Override
    protected void buildContentValues(Country country, ContentValues values) {
        values.put("name", country.getName());
    }
}
