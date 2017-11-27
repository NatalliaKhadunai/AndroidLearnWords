package com.hfad.starbuzz;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WordCheckActivity extends Activity {

    private Integer wordId;
    private String wordToCheck;
    private String wordsMeaning;
    private Integer wordRating;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_check);

        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new WordDatabaseHelper(this);
            db = starbuzzDatabaseHelper.getReadableDatabase();

            cursor = db.query(WordDatabaseHelper.defineTableName(getIntent().getStringExtra(IntentExtraConstant.LANGUAGE)),
                    new String[]{"_id", "WORD", "MEANING", "RATING"},
                    null, null, null, null, "RATING");
            cursor.moveToFirst();
            wordId = cursor.getInt(0);
            wordToCheck = cursor.getString(1);
            wordsMeaning = cursor.getString(2);
            wordRating = cursor.getInt(3);
            ((TextView)findViewById(R.id.word_to_be_checked)).setText(wordsMeaning);
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    public void onCheckWordClicked(View view) {
        String typedWord = ((EditText)findViewById(R.id.word_to_check)).getText().toString();
        if (typedWord.equals(wordToCheck)) {
            TextView resultTextView = (TextView)findViewById(R.id.word_check_result);
            resultTextView.setTextColor(Color.GREEN);
            resultTextView.setText("Correct!");
            updateWordRating(1);
        } else {
            TextView resultTextView =(TextView)findViewById(R.id.word_check_result);
            resultTextView.setTextColor(Color.RED);
            resultTextView.setText("Incorrect! Should be \"" + wordToCheck + "\" instead of \"" + typedWord + "\"");
            updateWordRating(-1);
        }
    }

    public void onNextCheckWordClicked(View view) {
        if (cursor.moveToNext()) {
            wordId = cursor.getInt(0);
            wordToCheck = cursor.getString(1);
            wordsMeaning = cursor.getString(2);
            wordRating = cursor.getInt(3);
            ((TextView)findViewById(R.id.word_to_be_checked)).setText(wordsMeaning);
            ((EditText)findViewById(R.id.word_to_check)).setText("");
            ((TextView)findViewById(R.id.word_check_result)).setText("");
        }
        else {
            ((TextView)findViewById(R.id.word_check_result)).setText("No more words!");
        }
    }

    public void updateWordRating(int rating) {
        ContentValues values = new ContentValues();
        values.put("RATING", wordRating + rating);
        db.update(WordDatabaseHelper.defineTableName(getIntent().getStringExtra(IntentExtraConstant.LANGUAGE)), values, "_id = " + wordId, null);
    }
}
