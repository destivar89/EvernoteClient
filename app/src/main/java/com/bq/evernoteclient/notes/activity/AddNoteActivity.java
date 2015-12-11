package com.bq.evernoteclient.notes.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bq.evernoteclient.R;
import com.bq.evernoteclient.evernoteapi.EvernoteApiManager;
import com.bq.evernoteclient.evernoteapi.EvernoteClientCallback;
import com.evernote.edam.type.Note;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by David on 10/12/15.
 */
public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener, EvernoteClientCallback<Note> {

    @Bind(R.id.input_title) EditText titleEdit;
    @Bind(R.id.input_content) EditText contentEdit;
    @Bind(R.id.save_note_button) Button saveNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        saveNoteButton.setOnClickListener(this);

        initActionBar();

    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.save_note_button){
            createNote();
        }
    }

    private void createNote() {
        EvernoteApiManager.getInstance(getApplicationContext()).createNote(titleEdit.getText().toString(),
                contentEdit.getText().toString(), this);
    }

    @Override
    public void onSuccess(Note result) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onException(Exception exception) {
        Snackbar.make(findViewById(R.id.content), getString(R.string.error_feedback), Snackbar.LENGTH_LONG).show();
    }
}
