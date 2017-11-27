package com.hfad.starbuzz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class WordDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "word"; // the name of our database
    private static final int DB_VERSION = 1; // the version of the database

    WordDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db);
    }

    private void updateMyDatabase(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ENGLISH_WORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, WORD TEXT NOT NULL, MEANING TEXT NOT NULL, RATING INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE GERMAN_WORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, WORD TEXT NOT NULL, MEANING TEXT NOT NULL, RATING INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE POLISH_WORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, WORD TEXT NOT NULL, MEANING TEXT NOT NULL, RATING INTEGER DEFAULT 0)");
    }

    public static String defineTableName(String languageStr) {
        Language language = Language.valueOf(languageStr);
        switch (language) {
            case ENGLISH: return "ENGLISH_WORD";
            case GERMAN: return "GERMAN_WORD";
            case POLISH: return "POLISH_WORD";
        }
        return "ENGLISH_WORD";
    }
}