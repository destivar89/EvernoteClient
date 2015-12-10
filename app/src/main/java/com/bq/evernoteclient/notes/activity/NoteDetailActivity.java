package com.bq.evernoteclient.notes.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bq.evernoteclient.R;
import com.bq.evernoteclient.evernoteapi.EvernoteApiManager;
import com.bq.evernoteclient.evernoteapi.EvernoteClientCallback;
import com.evernote.edam.type.Note;

/**
 * Created by David on 10/12/15.
 */
public class NoteDetailActivity extends AppCompatActivity implements EvernoteClientCallback<Note>{

    private TextView title;
    private TextView content;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = (TextView) findViewById(R.id.title_textview);
        content = (TextView) findViewById(R.id.content_textview);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        initActionBar();

        String guid = getIntent().getStringExtra(getString(R.string.note_guid));
        retrieveNoteDetail(guid);

    }

    private void retrieveNoteDetail(String guid){
        showLoading();
        EvernoteApiManager.getInstance(getApplicationContext()).retrieveNoteDetail(guid, this);
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    private void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
        title.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
    }

    private void hideLoading(){
        progressBar.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(Note result) {
        title.setText(result.getTitle());
        content.setText(result.getContent());
        hideLoading();
    }

    @Override
    public void onException(Exception exception) {
        hideLoading();
        //TODO: show error feedback
    }
}
