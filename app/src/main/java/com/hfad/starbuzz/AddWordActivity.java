package com.hfad.starbuzz;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class AddWordActivity extends Activity {

    private WordDatabaseHelper wordDatabaseHelper;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        wordDatabaseHelper = new WordDatabaseHelper(this);
        language = getIntent().getStringExtra(IntentExtraConstant.LANGUAGE);

        Spinner chooseTopicDropdown = (Spinner) findViewById(R.id.choose_topic_dropdown);
        Cursor topicCursor = wordDatabaseHelper.getTopicCursor(language);
        CursorAdapter topicAdapter =
                new SimpleCursorAdapter(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        topicCursor,
                        new String[]{"TOPIC"},
                        new int[]{android.R.id.text1}, 0);
        chooseTopicDropdown.setAdapter(topicAdapter);
    }

    public void onAddWordClicked(View view) {
        wordDatabaseHelper.addWord(language, getValuesToAddWord());
        finish();
    }

    private ContentValues getValuesToAddWord() {
        EditText wordET = (EditText) findViewById(R.id.add_word);
        EditText meaningET = (EditText) findViewById(R.id.add_word_meaning);

        ContentValues contentValues = new ContentValues();
        contentValues.put("WORD", wordET.getText().toString());
        contentValues.put("MEANING", meaningET.getText().toString());
        contentValues.put("TOPIC", defineTopic());
        return contentValues;
    }

    private String defineTopic() {
        String topic = "";
        EditText newTopicET = (EditText) findViewById(R.id.add_topic_text);
        Spinner spinner = (Spinner) findViewById(R.id.choose_topic_dropdown);
        if (!newTopicET.getText().toString().isEmpty()) {
            topic = newTopicET.getText().toString();
        } else if (spinner.getSelectedItem() != null) {
            topic = spinner.getSelectedItem().toString();
        }
        return topic;
    }
}
