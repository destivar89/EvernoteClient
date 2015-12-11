package com.bq.evernoteclient.notes.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bq.evernoteclient.R;
import com.bq.evernoteclient.evernoteapi.EvernoteApiManager;
import com.bq.evernoteclient.evernoteapi.EvernoteClientCallback;
import com.evernote.edam.type.Note;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by David on 10/12/15.
 */
public class NoteDetailActivity extends AppCompatActivity implements EvernoteClientCallback<Note>, View.OnClickListener{

    @Bind(R.id.title_textview) TextView title;
    @Bind(R.id.content_textview) TextView content;
    @Bind(R.id.progress_bar) ProgressBar progressBar;
    @Bind(R.id.retry_button) Button retryButton;
    @Bind(R.id.feedback) LinearLayout feedbackLayout;

    String guid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        retryButton.setOnClickListener(this);
        initActionBar();

        guid = getIntent().getStringExtra(getString(R.string.note_guid));
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
        feedbackLayout.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(Note result) {

        title.setText(result.getTitle());
        content.setText(Html.fromHtml(result.getContent()));
        hideLoading();
    }

    @Override
    public void onException(Exception exception) {
        hideLoading();
        showErrorFeedback();
    }

    private void showErrorFeedback() {
        feedbackLayout.setVisibility(View.VISIBLE);
        title.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.retry_button:
                retrieveNoteDetail(guid);
                break;
        }
    }
}
