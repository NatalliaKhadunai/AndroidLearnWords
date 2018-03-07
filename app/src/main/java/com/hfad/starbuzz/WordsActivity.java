package com.hfad.starbuzz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.hfad.starbuzz.IntentExtraConstant.*;

public class WordsActivity extends Activity {

    private WordDatabaseHelper wordDatabaseHelper;
    private String language;
    private String[] topicArray;
    private Set<String> chosenTopicSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);
        wordDatabaseHelper = new WordDatabaseHelper(this);
        language = getIntent().getStringExtra(IntentExtraConstant.LANGUAGE);

        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        if (position == 0) {
                            Intent intent = new Intent(WordsActivity.this,
                                    AddUpdateWordActivity.class);
                            intent.putExtra(IntentExtraConstant.LANGUAGE, language);
                            startActivity(intent);
                        }
                        if (position == 1) {
                            createChooseTopicsForCheckDialog().show();
                        }
                    }
                };

        ListView listView = (ListView) findViewById(R.id.list_words_actions);
        listView.setOnItemClickListener(itemClickListener);

        loadWordsToList();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWordsToList();
    }

    private Dialog createChooseTopicsForCheckDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        topicArray = formTopicArray();
        builder.setTitle("Choose topic").setMultiChoiceItems(topicArray, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    chosenTopicSet.add(topicArray[which]);
                } else {
                    chosenTopicSet.remove(topicArray[which]);
                }
            }
        });
        builder.setPositiveButton(R.string.choose_topic_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(WordsActivity.this,
                        WordCheckActivity.class);
                intent.putExtra(IntentExtraConstant.LANGUAGE, language);
                intent.putStringArrayListExtra(CHOSEN_TOPICS, new ArrayList<>(chosenTopicSet));
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.choose_topic_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        return builder.create();
    }

    private String[] formTopicArray() {
        Cursor topicCursor = wordDatabaseHelper.getTopicCursor(language);
        Set<String> topicSet = new HashSet(topicCursor.getCount());
        if (topicCursor.moveToFirst()) {
            for (int i = 0; i < topicCursor.getCount(); i++) {
                topicSet.add(topicCursor.getString(1));
                topicCursor.moveToNext();
            }
        }
        String[] topicArray = new String[topicSet.size()];
        topicArray = topicSet.toArray(topicArray);
        return topicArray;
    }

    private void loadWordsToList() {
        ListView listWords = (ListView) findViewById(R.id.list_words);
        Cursor wordCursor = wordDatabaseHelper.getWordsAndMeaningCursor(language);
        CursorAdapter wordsAdapter =
                new SimpleCursorAdapter(WordsActivity.this,
                        android.R.layout.simple_list_item_2,
                        wordCursor,
                        new String[]{"WORD", "MEANING"},
                        new int[]{android.R.id.text1, android.R.id.text2}, 0);
        listWords.setAdapter(wordsAdapter);

        listWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                Word word = wordDatabaseHelper.getWord(language, (int) id);
                    Intent intent = new Intent(WordsActivity.this, AddUpdateWordActivity.class);
                    intent.putExtra(IntentExtraConstant.LANGUAGE, language);
                    intent.putExtra(WORD, word);
                    startActivity(intent);
            }
        });
    }
}
