package com.zzh.mt.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 腾翔信息 on 2017/6/8.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper{

    public MyDatabaseHelper(Context context){
        super(context,"data",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create Table If Not Exists list(url Text Default None)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
