package com.example.as.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DBNAME = "account.db";

    public DBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tb_outaccount (_id integer primary key,money decimal,time varchar(10),"
                + "type varchar(10),mark varchar(200))");
        db.execSQL("create table tb_inaccount (_id integer primary key,money decimal,time varchar(10),"
                + "type varchar(10),mark varchar(200))");
        db.execSQL("create table tb_family (account vachar(100) primary key,password varchar(20),call vachar(100))");
        db.execSQL("create table tb_flag (_id integer primary key,flag varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
