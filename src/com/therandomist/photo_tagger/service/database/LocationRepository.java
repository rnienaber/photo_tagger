package com.therandomist.photo_tagger.service.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.therandomist.photo_tagger.model.Location;

public class LocationRepository extends Repository<Location>{

    public static final String DATABASE_CREATE = "create table location (_id integer primary key autoincrement, "
            + " name text not null,"
            + " latitude real not null, "
            + " longitude real not null, "
            + " area_id integer not null); ";

    public LocationRepository(Context context) {
        super(context, "location", "name");
    }

    protected Location getFromCursor(Cursor cursor){
        String name = cursor.getString(cursor.getColumnIndex("name"));
        Long id = cursor.getLong(cursor.getColumnIndex("_id"));
        Long areaId = cursor.getLong(cursor.getColumnIndex("area_id"));
        Double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
        Double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));

        return new Location(id, name, latitude, longitude, areaId);
    }

    @Override
    protected void buildContentValues(Location location, ContentValues values) {
        values.put("name", location.getName());
        values.put("area_id", location.getArea().getId());
        values.put("latitude", location.getLatitude());
        values.put("longitude", location.getLongitude());
    }
}
