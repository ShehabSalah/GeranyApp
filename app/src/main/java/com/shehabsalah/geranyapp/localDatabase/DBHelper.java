package com.shehabsalah.geranyapp.localDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ShehabSalah on 8/29/17.
 *
 */

public class DBHelper extends SQLiteOpenHelper {
    //DATABASE_VERSION is containing the version number of the current DB schema.
    //If the Database schema changed, the DATABASE_VERSION must change also.
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "GeranyApp.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Post Table
        final String CREATE_POST_TABLE          = "CREATE TABLE " + PostModel.TABLE_NAME + " ( " +
                PostModel.COL_USERNAME          + " TEXT NOT NULL, " +
                PostModel.COL_PROFILE_PIC       + " TEXT NULL, " +
                PostModel.COL_TEXT              + " TEXT NOT NULL, " +
                PostModel.COL_LOCATION          + " TEXT NOT NULL );";


        //Execute the SQL of creating post table
        db.execSQL(CREATE_POST_TABLE);
    }

    //This method is Called only if the
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop database tables DATABASE_VERSION number is changed
        db.execSQL("DROP TABLE IF EXISTS " + PostModel.TABLE_NAME);
        //Create New Database
        onCreate(db);

    }
}

