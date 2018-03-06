package com.hfad.starbuzz;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.hfad.starbuzz.IntentExtraConstant.WORD;

public class AddUpdateWordActivity extends Activity {

    private WordDatabaseHelper wordDatabaseHelper;
    private String language;
    private Word word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_word);
        wordDatabaseHelper = new WordDatabaseHelper(this);
        language = getIntent().getStringExtra(IntentExtraConstant.LANGUAGE);

        Spinner chooseTopicDropdown = (Spinner) findViewById(R.id.choose_topic_dropdown);
        chooseTopicDropdown.setAdapter(formTopicAdapter());

        word = getIntent().getParcelableExtra(WORD);
        if (word != null) {
            findViewById(R.id.update_word_btn).setVisibility(View.VISIBLE);
            findViewById(R.id.delete_word_btn).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.word)).setText(word.getWordStr());
            ((TextView) findViewById(R.id.meaning)).setText(word.getMeaning());

            if (word.getTopic() != null && !word.getTopic().isEmpty()) {
                setSpinText((Spinner) findViewById(R.id.choose_topic_dropdown), word.getTopic());
            }
        } else {
            findViewById(R.id.add_word_btn).setVisibility(View.VISIBLE);
        }
    }

    public void onAddWordBtnClicked(View view) {
        wordDatabaseHelper.addWord(language, getValuesToAddWord());
        finish();
    }

    public void onUpdateWordBtnClick(View view) {
        wordDatabaseHelper.updateWord(language, word.getWordId(), getValuesToUpdateWord());
        finish();
    }

    public void onDeleteWordBtnClick(View view) {
        wordDatabaseHelper.deleteWord(language, word.getWordId());
    }

    private ArrayAdapter<String> formTopicAdapter() {
        Cursor topicCursor = wordDatabaseHelper.getTopicCursor(language);
        Set<String> topicSet = new HashSet(topicCursor.getCount());
        if (topicCursor.moveToFirst()) {
            for (int i = 0; i < topicCursor.getCount(); i++) {
                topicSet.add(topicCursor.getString(1));
                topicCursor.moveToNext();
            }
        }
        topicSet.add("None");
        return new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(topicSet));
    }

    private void setSpinText(Spinner spin, String topic) {
        ArrayAdapter adapter = ((ArrayAdapter)spin.getAdapter());
        for (int i = 0; i < adapter.getCount(); i++) {
            String item = (String) adapter.getItem(i);
            if (item.equals(topic)) {
                spin.setSelection(i);
                break;
            }
        }
    }

    private ContentValues getValuesToAddWord() {
        EditText wordET = (EditText) findViewById(R.id.word);
        EditText meaningET = (EditText) findViewById(R.id.meaning);

        ContentValues contentValues = new ContentValues();
        contentValues.put("WORD", wordET.getText().toString());
        contentValues.put("MEANING", meaningET.getText().toString());
        contentValues.put("TOPIC", defineTopic());
        return contentValues;
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
        EditText newTopicET = (EditText) findViewById(R.id.add_topic);
        Spinner spinner = (Spinner) findViewById(R.id.choose_topic_dropdown);
        if (!newTopicET.getText().toString().isEmpty()) {
            topic = newTopicET.getText().toString();
        } else if (spinner.getSelectedItem() != null) {
            ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
            topic = (String) adapter.getItem((int)spinner.getSelectedItemId());
        }
        return topic;
    }
}
