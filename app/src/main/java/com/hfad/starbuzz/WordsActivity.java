package com.hfad.starbuzz;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class WordsActivity extends Activity {

    private SQLiteDatabase db;
    private Cursor wordCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        //Create an OnItemClickListener
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        if (position == 0) {
                            Intent intent = new Intent(WordsActivity.this,
                                    AddWordActivity.class);
                            intent.putExtra(TopLevelActivity.LANGUAGE, getIntent().getStringExtra(TopLevelActivity.LANGUAGE));
                            startActivity(intent);
                        }
                        if (position == 1) {
                            Intent intent = new Intent(WordsActivity.this,
                                    WordCheckActivity.class);
                            intent.putExtra(TopLevelActivity.LANGUAGE, getIntent().getStringExtra(TopLevelActivity.LANGUAGE));
                            startActivity(intent);
                        }
                    }
                };
        //Add the listener to the list view
        ListView listView = (ListView) findViewById(R.id.list_words_actions);
        listView.setOnItemClickListener(itemClickListener);

        loadWords();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWords();
    }

    private void loadWords() {
        ListView listWords = (ListView)findViewById(R.id.list_words);
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new WordDatabaseHelper(this);
            db = starbuzzDatabaseHelper.getReadableDatabase();
            String tableName = defineTableName();
            wordCursor = db.query(tableName,
                    new String[] {"_id", "WORD", "MEANING"}, null,
                    null, null, null, null);
            CursorAdapter wordsAdapter =
                    new SimpleCursorAdapter(WordsActivity.this,
                            android.R.layout.simple_list_item_2,
                            wordCursor,
                            new String[]{"WORD", "MEANING"},
                            new int[]{android.R.id.text1, android.R.id.text2}, 0);
            listWords.setAdapter(wordsAdapter);
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        listWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                String query = "SELECT * FROM " + defineTableName() + " WHERE _id = ?";
                Cursor cursor = db.rawQuery(query, new String[] {Long.toString(id)});
                if (cursor.moveToFirst()) {
                    Intent intent = new Intent(WordsActivity.this, WordActivity.class);
                    intent.putExtra(TopLevelActivity.LANGUAGE, getIntent().getStringExtra(TopLevelActivity.LANGUAGE));
                    intent.putExtra(WordActivity.WORD_ID_INTENT_EXTRA, cursor.getInt(0));
                    intent.putExtra(WordActivity.WORD_INTENT_EXTRA, cursor.getString(1));
                    intent.putExtra(WordActivity.MEANING_INTENT_EXTRA, cursor.getString(2));
                    startActivity(intent);
                }
            }
        });
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
}
