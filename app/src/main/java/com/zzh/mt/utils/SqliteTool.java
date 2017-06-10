package com.zzh.mt.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zzh.mt.sql.MyProvider;

/**
 * Created by 腾翔信息 on 2017/6/8.
 */

public class SqliteTool {
    private static SqliteTool instance = null;
    private String data;
    private Cursor cursor;
    private SQLiteDatabase db;


    public synchronized  static SqliteTool getInstance(){
        if (instance == null){
            instance = new SqliteTool();
        }
        return instance;
    }

    //添加信息
    public void addData(Context context, String name){
        cursor = context.getContentResolver().query(MyProvider.URI,null,"url=?",new String[]{name},null);

            while (cursor.moveToNext()){
                data = cursor.getString(cursor.getColumnIndex("url"));
            }
            if (!name.equals(data)){
                ContentValues values = new ContentValues();
                values.put("url",name);
                context.getContentResolver().insert(MyProvider.URI,values);
            }else if (name.equals(data)){
                //删除原来的新添加一个现有的
                delete(context,data);
                ContentValues values = new ContentValues();
                values.put("url",name);
                context.getContentResolver().insert(MyProvider.URI,values);
            }


    }
    //删表格
    public void clean(Context context){
        SQLiteDatabase db = context.openOrCreateDatabase("data",Context.MODE_PRIVATE,null);
        db.execSQL("DROP TABLE list");
    }

    public void delete(Context context ,String data){
        context.getContentResolver().delete(MyProvider.URI,"url=?",new String[] {data});
    }
}
