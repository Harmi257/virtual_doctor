package com.example.virtualdoctor.contentProvider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.virtualdoctor.DBHelper;
import com.example.virtualdoctor.model.ConsultationContract;

public class ConsultationProvider extends ContentProvider {

    private DBHelper dbHelper;

    private static final int CODE_CONSULTATION_DIR = 1;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(ConsultationContract.CONTENT_AUTHORITY,
                ConsultationContract.PATH_CONSULTATIONS, CODE_CONSULTATION_DIR);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (match == CODE_CONSULTATION_DIR) {
            long id = db.insert(ConsultationContract.ConsultationEntry.TABLE_NAME, null, values);
            if (id == -1) throw new IllegalArgumentException("Insert failed");
            return ContentUris.withAppendedId(uri, id);
        } else {
            throw new IllegalArgumentException("Insert not supported for URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(ConsultationContract.ConsultationEntry.TABLE_NAME);
        return builder.query(dbHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) { return 0; }
    @Override public int delete(Uri uri, String selection, String[] selectionArgs) { return 0; }
    @Override public String getType(Uri uri) { return null; }
}
