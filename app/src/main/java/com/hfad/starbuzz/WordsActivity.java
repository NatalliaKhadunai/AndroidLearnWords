package com.hfad.starbuzz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

import static com.hfad.starbuzz.IntentExtraConstant.*;

public class WordsActivity extends Activity {

    private WordDatabaseHelper wordDatabaseHelper;
    private String language;

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
        final Cursor topicCursor = wordDatabaseHelper.getTopicCursor(language);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose topic").setMultiChoiceItems(topicCursor, "TOPIC", "TOPIC", null);
        builder.setPositiveButton(R.string.choose_topic_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(WordsActivity.this,
                        WordCheckActivity.class);
                intent.putExtra(IntentExtraConstant.LANGUAGE, language);
                intent.putStringArrayListExtra(CHOSEN_TOPICS, getChosenTopicsForCheck(dialogInterface));
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

    private ArrayList<String> getChosenTopicsForCheck(DialogInterface dialogInterface) {
        long[] checkedItemIds = ((AlertDialog) dialogInterface).getListView().getCheckedItemIds();
        Adapter adapter = ((AlertDialog) dialogInterface).getListView().getAdapter();
        Cursor cursor = ((CursorAdapter) adapter).getCursor();
        ArrayList<String> chosenTopics = new ArrayList<>();
        for (long checkedId : checkedItemIds) {
            cursor.moveToPosition((int) checkedId - 1);
            String topic = cursor.getString(1);
            chosenTopics.add(topic);
        }
        return chosenTopics;
    }
}
