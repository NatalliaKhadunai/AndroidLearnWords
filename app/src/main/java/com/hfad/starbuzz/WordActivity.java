package com.hfad.starbuzz;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class WordActivity extends Activity {

    public static final String WORD_ID_INTENT_EXTRA = "wordId";
    public static final String WORD_INTENT_EXTRA = "word";
    public static final String MEANING_INTENT_EXTRA = "meaning";

    private Integer wordId;
    private String word;
    private String meaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        wordId = getIntent().getIntExtra(WORD_ID_INTENT_EXTRA, -1);
        word = getIntent().getStringExtra(WORD_INTENT_EXTRA);
        meaning = getIntent().getStringExtra(MEANING_INTENT_EXTRA);
        ((TextView)findViewById(R.id.word)).setText(word);
        ((TextView)findViewById(R.id.meaning)).setText(meaning);
    }

    public void onUpdateWordBtnClick(View view) {
        SQLiteOpenHelper starbuzzDatabaseHelper = new WordDatabaseHelper(WordActivity.this);
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
            String tableName = defineTableName();
            ContentValues contentValues = getValues();
            db.update(tableName, contentValues, "_id = " + wordId, null);
            db.close();
            finish();
        } catch(SQLiteException e) {
        }
        finish();
    }

    public void onDeleteWordBtnClick(View view) {
        SQLiteOpenHelper starbuzzDatabaseHelper = new WordDatabaseHelper(WordActivity.this);
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
            String tableName = defineTableName();
            db.delete(tableName, "_id = " + wordId, null);
            db.close();
            finish();
        } catch(SQLiteException e) {
        }
        finish();
    }

    private String defineTableName() {
        String languageStr = getIntent().getStringExtra(TopLevelActivity.LANGUAGE);
        Language language = Language.valueOf(languageStr);
        switch (language) {
            case ENGLISH: return "ENGLISH_WORD";
            case GERMAN: return "GERMAN_WORD";
            case POLISH: return "POLISH_WORD";
        }
        return "ENGLISH_WORD";
    }
    private ContentValues getValues() {
        EditText wordET = (EditText)findViewById(R.id.word);
        EditText meaningET = (EditText)findViewById(R.id.meaning);
        ContentValues contentValues = new ContentValues();
        contentValues.put("WORD", wordET.getText().toString());
        contentValues.put("MEANING", meaningET.getText().toString());
        return contentValues;
    }

}
