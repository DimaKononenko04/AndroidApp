package com.example.database_connection.sql_lite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ownersInfoDb";
    public static final String TABLE_OWNERS = "owners";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_TELEPHONE = "telephone";
    public static final String KEY_LICENSE_PLATE = "license_plate";

    public DbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = String.format("create table %s(%s integer primary key,%s text," +
                "%s text,%s text,%s text)",TABLE_OWNERS,KEY_ID,KEY_NAME,KEY_SURNAME,KEY_TELEPHONE,KEY_LICENSE_PLATE);
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropQuery = String.format("drop table if exists %s",TABLE_OWNERS);
        db.execSQL(dropQuery);
        onCreate(db);
    }
}
