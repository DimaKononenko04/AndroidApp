package com.example.database_connection.sql_lite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

public class DbManager {

    public static void addToDB(DbHelper dbHelper){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(DbHelper.KEY_NAME, "Lena");
                contentValues.put(DbHelper.KEY_SURNAME, "Golovach");
                contentValues.put(DbHelper.KEY_TELEPHONE, "0874993817");
                contentValues.put(DbHelper.KEY_LICENSE_PLATE, "AA5381AP");
                database.insert(DbHelper.TABLE_OWNERS,null,contentValues);
    }

    public static void viewOwnerInfo(DbHelper dbHelper, TextView licensePlate){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DbHelper.TABLE_OWNERS,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DbHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DbHelper.KEY_NAME);
            int surnameIndex = cursor.getColumnIndex(DbHelper.KEY_SURNAME);
            int telephoneIndex = cursor.getColumnIndex(DbHelper.KEY_TELEPHONE);
            int licensePlateIndex = cursor.getColumnIndex(DbHelper.KEY_LICENSE_PLATE);
            do {
                if (cursor.getString(licensePlateIndex).contentEquals(licensePlate.getText())){
                    Log.e("mLog", "ID = " + cursor.getInt(idIndex) +
                            ", name = " + cursor.getString(nameIndex) +
                            ", surname = " + cursor.getString(surnameIndex) +
                            ", telephone = " + cursor.getString(telephoneIndex) +
                            ", license plate = " + cursor.getString(licensePlateIndex));
                }
            }while (cursor.moveToNext());
            cursor.close();
        }
    }
}
