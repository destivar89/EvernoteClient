package com.bq.evernoteclient.notes.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bq.evernoteclient.R;
import com.bq.evernoteclient.evernoteapi.EvernoteApiManager;
import com.bq.evernoteclient.evernoteapi.EvernoteClientCallback;
import com.evernote.edam.type.Note;

/**
 * Created by David on 10/12/15.
 */
public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener, EvernoteClientCallback<Note> {

    private EditText titleEdit;
    private EditText contentEdit;
    private Button saveNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleEdit = (EditText) findViewById(R.id.input_title);
        contentEdit = (EditText) findViewById(R.id.input_content);
        saveNoteButton = (Button) findViewById(R.id.save_note_button);

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
        finish();
        //TODO: feedback
    }

    @Override
    public void onException(Exception exception) {
        //TODO: error feedback
    }
}
