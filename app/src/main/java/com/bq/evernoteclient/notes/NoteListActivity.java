package com.bq.evernoteclient.notes;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bq.evernoteclient.R;
import com.bq.evernoteclient.evernoteapi.EvernoteApiManager;
import com.bq.evernoteclient.evernoteapi.EvernoteClientCallback;
import com.evernote.edam.notestore.NoteList;

/**
 * Created by David on 10/12/15.
 */
public class NoteListActivity extends AppCompatActivity implements EvernoteClientCallback<NoteList> {

    private RecyclerView recyclerView;

    private NoteListAdapter noteListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        initRecyclerView();
        retrieveNotes();

    }

    private void retrieveNotes() {
        EvernoteApiManager.getInstance(getApplicationContext()).retrieveNotes(0, 0, this);
    }

    private void initRecyclerView() {

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        noteListAdapter = new NoteListAdapter();
        recyclerView.setAdapter(noteListAdapter);

    }

    @Override
    public void onSuccess(NoteList result) {
        noteListAdapter.addNotes(result);
        noteListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onException(Exception exception) {
        return;
    }
}
