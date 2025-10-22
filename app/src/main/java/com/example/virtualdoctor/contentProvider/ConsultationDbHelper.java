package com.example.virtualdoctor.contentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.virtualdoctor.model.ConsultationContract.ConsultationEntry;

public class ConsultationDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "virtualdoctor.db";
    private static final int DATABASE_VERSION = 1;

    public ConsultationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + ConsultationEntry.TABLE_NAME + " ("
                + ConsultationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ConsultationEntry.COLUMN_PATIENT_NAME + " TEXT NOT NULL, "
                + ConsultationEntry.COLUMN_DOCTOR_NAME + " TEXT NOT NULL, "
                + ConsultationEntry.COLUMN_DATE + " TEXT, "
                + ConsultationEntry.COLUMN_TIME + " TEXT, "
                + ConsultationEntry.COLUMN_REASON + " TEXT);";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not used for now
    }
}

