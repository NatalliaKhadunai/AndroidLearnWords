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
                        }
                    }
                };
        //Add the listener to the list view
        ListView listView = (ListView) findViewById(R.id.list_words_actions);
        listView.setOnItemClickListener(itemClickListener);

        //Create an OnItemClickListener
        /*AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        if (position == 0) {
                            Intent intent = new Intent(WordsActivity.this,
                                    WordListActivity.class);
                            startActivity(intent);
                        }
                    }
                };*/
        //Add the listener to the list view
        //ListView listView = (ListView) findViewById(R.id.list_words);
        //listView.setOnItemClickListener(itemClickListener);

        ListView listFavorites = (ListView)findViewById(R.id.list_words);
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new WordDatabaseHelper(this);
            db = starbuzzDatabaseHelper.getReadableDatabase();
            String tableName = defineTableName();
            wordCursor = db.query(tableName,
                    new String[] {"_id", "WORD"}, null,
                    null, null, null, null);
            CursorAdapter favoriteAdapter =
                    new SimpleCursorAdapter(WordsActivity.this,
                            android.R.layout.simple_list_item_1,
                            wordCursor,
                            new String[]{"WORD"},
                            new int[]{android.R.id.text1}, 0);
            listFavorites.setAdapter(favoriteAdapter);
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        //Navigate to DrinkActivity if a drink is clicked
        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                Intent intent = new Intent(WordsActivity.this, DrinkActivity.class);
                startActivity(intent);
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
