package com.example.yuda.findaplace;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yuda on 08/05/2017.
 */

public class mySQLHelper extends SQLiteOpenHelper {
    public mySQLHelper(Context context) {
        super(context, "location.db " , null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLCreate="CREATE TABLE "+ DBConstants.tableName+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.NameColumn+" TEXT,  " +DBConstants.AddressColumn
                +" TEXT,  "+DBConstants.LatColumn+" TEXT , "+DBConstants.LngColumn
                +" TEXT, "+DBConstants.imageColumn +" TEXT )";
        db.execSQL(SQLCreate);

        String sqlcreate="CREATE TABLE "+ DBConstants.Lastknow+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.NameColumn+" TEXT,  " +DBConstants.AddressColumn
                +" TEXT,  "+DBConstants.LatColumn+" TEXT , "+DBConstants.LngColumn
                +" TEXT, "+DBConstants.imageColumn +" TEXT )";
        db.execSQL(sqlcreate);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
