package com.example.database_connection.manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.database_connection.model.Owner;
import com.example.database_connection.sql_lite.DbHelper;

public class OwnerManager {

    public static Owner getOwnerInfo(DbHelper dbHelper, String licensePlate){
        Owner owner = null;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DbHelper.TABLE_OWNERS,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DbHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DbHelper.KEY_NAME);
            int surnameIndex = cursor.getColumnIndex(DbHelper.KEY_SURNAME);
            int telephoneIndex = cursor.getColumnIndex(DbHelper.KEY_TELEPHONE);
            int licensePlateIndex = cursor.getColumnIndex(DbHelper.KEY_LICENSE_PLATE);
            do {
                if (cursor.getString(licensePlateIndex).contentEquals(licensePlate)){
                    owner = new Owner();
                    owner.setId(String.valueOf(cursor.getInt(idIndex)));
                    owner.setName(cursor.getString(nameIndex));
                    owner.setSurname(cursor.getString(surnameIndex));
                    owner.setTelephone(cursor.getString(telephoneIndex));
                    owner.setLicensePlate(cursor.getString(licensePlateIndex));
                    return owner;
                }
            }while (cursor.moveToNext());
            cursor.close();
        }
        return owner;
    }

    public static void addOwnerToDb(DbHelper dbHelper, Owner owner){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.KEY_NAME, owner.getName());
        contentValues.put(DbHelper.KEY_SURNAME, owner.getSurname());
        contentValues.put(DbHelper.KEY_TELEPHONE, owner.getTelephone());
        contentValues.put(DbHelper.KEY_LICENSE_PLATE, owner.getLicensePlate());
        database.insert(DbHelper.TABLE_OWNERS,null,contentValues);
    }
}
