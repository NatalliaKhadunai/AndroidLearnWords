package com.hfad.starbuzz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.hfad.starbuzz.IntentExtraConstant.LANGUAGE;

public class TopLevelActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        AdapterView.OnItemClickListener itemClickListener =
            new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> listView,
                                        View v,
                                        int position,
                                        long id) {
                    Language language = Language.ENGLISH;
                    if (position == 0) {
                        language = Language.ENGLISH;
                    }
                    if (position == 1) {
                        language = Language.GERMAN;
                    }
                    if (position == 2) {
                        language = Language.POLISH;
                    }
                    Intent intent = new Intent(TopLevelActivity.this,
                                                   WordsActivity.class);
                    intent.putExtra(LANGUAGE, language.name());
                    startActivity(intent);

                }
            };

        ListView listView = (ListView) findViewById(R.id.list_languages);
        listView.setOnItemClickListener(itemClickListener);
    }
}