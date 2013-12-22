package com.therandomist.photo_tagger.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.therandomist.photo_tagger.model.Area;

public class AreaRepository extends Repository<Area> {

    public static final String DATABASE_CREATE = "create table area (_id integer primary key autoincrement, "
            + " name text not null, "
            + " country_id integer not null); ";

    public AreaRepository(Context context) {
        super(context, "area", "name");
    }

    @Override
    protected void buildContentValues(Area area, ContentValues values) {
        values.put("name", area.getName());
        values.put("country_id", area.getCountry().getId());
    }

    @Override
    protected Area getFromCursor(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex("name"));
        Long id = cursor.getLong(cursor.getColumnIndex("_id"));
        Long countryId = cursor.getLong(cursor.getColumnIndex("country_id"));

        return new Area(id, name, countryId);
    }
}
