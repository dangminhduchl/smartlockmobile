package com.example.smartlockjava.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "person_database";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_PERSON = "person";

    // Table Columns
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FACE_VECTOR = "face_vector";

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_PERSON + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            USERNAME + " TEXT NOT NULL," +
            PASSWORD + " TEXT ," +
            FACE_VECTOR + " BLOB );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        onCreate(db);
    }
}