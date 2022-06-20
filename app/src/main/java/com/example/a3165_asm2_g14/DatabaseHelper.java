package com.example.a3165_asm2_g14;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "MoneySaver.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "saved_record";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "record_date";
    private static final String COLUMN_TYPE = "record_type";
    private static final String COLUMN_AMOUNT = "record_amount";
    private static final String COLUMN_CATEGORY = "record_category";
    private static final String COLUMN_METHOD = "record_method";
    private static final String COLUMN_NOTE = "record_note";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuery = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_METHOD + " TEXT, " +
                COLUMN_NOTE + " TEXT);";
        sqLiteDatabase.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addRecord(String date, String type, Double amount, String category, String method, String note) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_TYPE, type);
        contentValues.put(COLUMN_AMOUNT, amount);
        contentValues.put(COLUMN_CATEGORY, category);
        contentValues.put(COLUMN_METHOD, method);
        contentValues.put(COLUMN_NOTE, note);

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        Log.d("thomas","pass add");
    }

    public Cursor readAllData() {
        String sqlQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
        }
        Log.d("thomas","pass read");
        return cursor;
    }

    public void deleteRecord(String row_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, "_id=?", new String[]{row_id});
    }

}
