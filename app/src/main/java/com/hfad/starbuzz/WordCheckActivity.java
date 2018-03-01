package com.hfad.starbuzz;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import static com.hfad.starbuzz.IntentExtraConstant.CHOSEN_TOPICS;

public class WordCheckActivity extends Activity {

    private Integer wordId;
    private String wordToCheck;
    private String wordsMeaning;
    private Integer wordRating;
    private WordDatabaseHelper wordDatabaseHelper;
    private Cursor cursor;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_check);
        wordDatabaseHelper = new WordDatabaseHelper(this);
        language = getIntent().getStringExtra(IntentExtraConstant.LANGUAGE);

        List<String> topics = getIntent().getStringArrayListExtra(CHOSEN_TOPICS);
        String[] selections = new String[topics.size()];
        if (topics != null && !topics.isEmpty()) {
            cursor = wordDatabaseHelper.getWordsOfSpecifTopicsCursor(language, topics.toArray(selections));
        }
        initializeFirstWord();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onCheckWordClicked(View view) {
        String typedWord = ((EditText) findViewById(R.id.word_to_check)).getText().toString();
        if (typedWord.equals(wordToCheck)) {
            TextView resultTextView = (TextView) findViewById(R.id.word_check_result);
            resultTextView.setTextColor(Color.GREEN);
            resultTextView.setText("Correct!");
            updateWordRating(1);
        } else {
            TextView resultTextView = (TextView) findViewById(R.id.word_check_result);
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
            ((TextView) findViewById(R.id.word_to_be_checked)).setText(wordsMeaning);
            ((EditText) findViewById(R.id.word_to_check)).setText("");
            ((TextView) findViewById(R.id.word_check_result)).setText("");
        } else {
            ((TextView) findViewById(R.id.word_check_result)).setText("No more words!");
        }
    }

    private void initializeFirstWord() {
        cursor.moveToFirst();
        wordId = cursor.getInt(0);
        wordToCheck = cursor.getString(1);
        wordsMeaning = cursor.getString(2);
        wordRating = cursor.getInt(4);
        ((TextView) findViewById(R.id.word_to_be_checked)).setText(wordsMeaning);
    }

    private void updateWordRating(int rating) {
        ContentValues values = new ContentValues();
        values.put("RATING", wordRating + rating);
        wordDatabaseHelper.updateWord(language, wordId, values);
    }
}
