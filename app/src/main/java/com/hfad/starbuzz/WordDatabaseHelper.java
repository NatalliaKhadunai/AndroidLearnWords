package com.hfad.starbuzz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.Collections;

class WordDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "WORD";
    private static final int DB_VERSION = 1;

    SQLiteDatabase dbWrite = getWritableDatabase();
    SQLiteDatabase dbRead = getReadableDatabase();

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

    public void addWord(String language, ContentValues contentValues) {
        String tableName = defineTableName(language);
        dbWrite.insert(tableName, null, contentValues);
    }

    public void updateWord(String language, int wordId, ContentValues contentValues) {
        dbWrite.update(defineTableName(language), contentValues, "_id = ?", new String[] { Integer.toString(wordId) });
    }

    public void deleteWord(String language, int wordId) {
        dbWrite.delete(defineTableName(language), "_id = ?", new String[] { Integer.toString(wordId) });
    }

    public Word getWord(String language, int wordId) {
        Word word = new Word();
        String query = "SELECT * FROM " + defineTableName(language) + " WHERE _id = ?";
        Cursor cursor = dbRead.rawQuery(query, new String[] { Long.toString(wordId) });
        if (cursor.moveToFirst()) {
            word.setWordId(cursor.getInt(0));
            word.setWordStr(cursor.getString(1));
            word.setMeaning(cursor.getString(2));
            word.setTopic(cursor.getString(3));
            word.setRating(cursor.getInt(4));
        }
        return word;
    }

    public Cursor getWordsOfSpecifTopicsCursor(String language, String[] topics) {
        return dbRead.query(defineTableName(language), new String[]{"_id", "WORD", "MEANING", "TOPIC", "RATING"},
                "TOPIC IN (" + TextUtils.join(",", Collections.nCopies(topics.length, "?")) + ")", topics,
                null, null, "RATING");
    }

    public Cursor getWordsAndMeaningCursor(String language) {
        return dbRead.query(defineTableName(language),
                new String[]{"_id", "WORD", "MEANING"}, null,
                null, null, null, null);
    }

    public Cursor getTopicCursor(String language) {
        return dbRead.query(defineTableName(language),
                new String[] {"_id", "TOPIC"}, null, null,
                null, null, null, null);
    }

    public String defineTableName(String languageStr) {
        Language language = Language.valueOf(languageStr);
        switch (language) {
            case ENGLISH: return "ENGLISH_WORD";
            case GERMAN: return "GERMAN_WORD";
            case POLISH: return "POLISH_WORD";
        }
        return "ENGLISH_WORD";
    }

    private void updateMyDatabase(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ENGLISH_WORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, WORD TEXT NOT NULL, MEANING TEXT NOT NULL, TOPIC TEXT DEFAULT 'None', RATING INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE GERMAN_WORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, WORD TEXT NOT NULL, MEANING TEXT NOT NULL, TOPIC TEXT DEFAULT 'None', RATING INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE POLISH_WORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, WORD TEXT NOT NULL, MEANING TEXT NOT NULL, TOPIC TEXT DEFAULT 'None', RATING INTEGER DEFAULT 0)");
    }


}