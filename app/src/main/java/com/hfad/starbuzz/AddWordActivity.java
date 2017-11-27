package com.hfad.starbuzz;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

public class AddWordActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
    }

    public void onAddWordClicked(View view) {
        SQLiteOpenHelper starbuzzDatabaseHelper = new WordDatabaseHelper(AddWordActivity.this);
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
            String tableName = WordDatabaseHelper.defineTableName(getIntent().getStringExtra(IntentExtraConstant.LANGUAGE));
            ContentValues contentValues = getValues();
            db.insert(tableName, null, contentValues);
            db.close();
            finish();
        } catch(SQLiteException e) {
        }
    }

    private ContentValues getValues() {
        EditText wordET = (EditText)findViewById(R.id.add_word);
        EditText meaningET = (EditText)findViewById(R.id.add_word_meaning);
        ContentValues contentValues = new ContentValues();
        contentValues.put("WORD", wordET.getText().toString());
        contentValues.put("MEANING", meaningET.getText().toString());
        return contentValues;
    }
}
