package com.zzh.mt.sql;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by 腾翔信息 on 2017/6/8.
 */

public class MyProvider extends ContentProvider {
    public static final Uri URI = Uri.parse("content://com.zzh.mt.sql.MyProvider");
    private static final String TABLE_NAME ="list";
    private SQLiteDatabase dbWrite,dbRead;
    private MyDatabaseHelper helper;
    @Override
    public boolean onCreate() {
        helper = new MyDatabaseHelper(getContext());
        dbRead = helper.getReadableDatabase();
        dbWrite = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return dbRead.query(TABLE_NAME,strings,s,strings1,null,null,s1);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        dbWrite.insert(TABLE_NAME,null,contentValues);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return dbWrite.delete(TABLE_NAME,s,strings);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return dbWrite.update(TABLE_NAME,contentValues,s,strings);
    }
}
