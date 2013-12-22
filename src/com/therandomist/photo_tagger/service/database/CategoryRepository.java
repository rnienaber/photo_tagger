package com.therandomist.photo_tagger.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.therandomist.photo_tagger.model.Category;

public class CategoryRepository extends Repository<Category>{

    public static final String DATABASE_CREATE = "create table category (_id integer primary key autoincrement, "
            + " name text not null); ";

    public CategoryRepository(Context context) {
        super(context, "category", "name");
    }

    @Override
    protected Category getFromCursor(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex("name"));
        Long id = cursor.getLong(cursor.getColumnIndex("_id"));
        return new Category(id, name);
    }

    @Override
    protected void buildContentValues(Category category, ContentValues values) {
        values.put("name", category.getName());
    }
}
