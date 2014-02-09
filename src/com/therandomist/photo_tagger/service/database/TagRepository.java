package com.therandomist.photo_tagger.service.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.therandomist.photo_tagger.model.Tag;

import java.util.List;

public class TagRepository extends Repository<Tag>{

    public static final String DATABASE_CREATE = "create table tag (_id integer primary key autoincrement, "
            + " name text not null,"
            + " category_id integer not null); ";

    public TagRepository(Context context) {
        super(context, "tag", "name");
    }

    protected Tag getFromCursor(Cursor cursor){
        String name = cursor.getString(cursor.getColumnIndex("name"));
        Long id = cursor.getLong(cursor.getColumnIndex("_id"));
        Long categoryId = cursor.getLong(cursor.getColumnIndex("category_id"));

        return new Tag(id, name, categoryId);
    }

    public List<Tag> getTags(List<Long> ids){
        String inClause = getInClauseIds(ids);
        String where = "_id in ("+inClause+")";
        return findUsingWhere(where);
    }

    @Override
    protected void buildContentValues(Tag tag, ContentValues values) {
        values.put("name", tag.getName());
        values.put("category_id", tag.getCategory().getId());
    }
}
