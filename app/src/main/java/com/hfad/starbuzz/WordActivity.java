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
import android.widget.TextView;

import static com.hfad.starbuzz.IntentExtraConstant.*;

public class WordActivity extends Activity {

    private Word word;
    private String language;
    private WordDatabaseHelper wordDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        wordDatabaseHelper = new WordDatabaseHelper(this);
        language = getIntent().getStringExtra(IntentExtraConstant.LANGUAGE);

        word = getIntent().getParcelableExtra(WORD);

        ((TextView) findViewById(R.id.word)).setText(word.getWordStr());
        ((TextView) findViewById(R.id.meaning)).setText(word.getMeaning());

        Spinner chooseTopicDropdown = (Spinner) findViewById(R.id.choose_new_topic_dropdown);
        Cursor topicCursor = wordDatabaseHelper.getTopicCursor(language);
        CursorAdapter topicAdapter =
                new SimpleCursorAdapter(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        topicCursor,
                        new String[] {"TOPIC"},
                        new int[]{android.R.id.text1}, 0);
        chooseTopicDropdown.setAdapter(topicAdapter);

        if (word.getTopic() != null && !word.getTopic().isEmpty()) {
            setSpinText((Spinner) findViewById(R.id.choose_new_topic_dropdown), word.getTopic());
        }
    }

    private void setSpinText(Spinner spin, String text) {
        for (int i = 0; i < spin.getAdapter().getCount(); i++) {
            if (spin.getAdapter().getItem(i).toString().contains(text)) {
                spin.setSelection(i);
            }
        }
    }

    public void onUpdateWordBtnClick(View view) {
        wordDatabaseHelper.updateWord(language, word.getWordId(), getValuesToUpdateWord());
        finish();
    }

    public void onDeleteWordBtnClick(View view) {
        wordDatabaseHelper.deleteWord(language, word.getWordId());
    }

    private ContentValues getValuesToUpdateWord() {
        EditText wordET = (EditText) findViewById(R.id.word);
        EditText meaningET = (EditText) findViewById(R.id.meaning);

        ContentValues contentValues = new ContentValues();
        contentValues.put("WORD", wordET.getText().toString());
        contentValues.put("MEANING", meaningET.getText().toString());
        contentValues.put("TOPIC", defineTopic());
        return contentValues;
    }

    private String defineTopic() {
        String topic = "";
        EditText newTopicET = (EditText) findViewById(R.id.new_topic);
        Spinner spinner = (Spinner) findViewById(R.id.choose_topic_dropdown);
        if (!newTopicET.getText().toString().isEmpty()) {
            topic = newTopicET.getText().toString();
        } else if (spinner.getSelectedItem() != null) {
            topic = spinner.getSelectedItem().toString();
        }
        return topic;
    }
}
