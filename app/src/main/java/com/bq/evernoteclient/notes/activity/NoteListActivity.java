package com.bq.evernoteclient.notes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.bq.evernoteclient.R;
import com.bq.evernoteclient.evernoteapi.EvernoteApiManager;
import com.bq.evernoteclient.evernoteapi.EvernoteClientCallback;
import com.bq.evernoteclient.notes.adapter.NoteListAdapter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.NoteSortOrder;

/**
 * Created by David on 10/12/15.
 */
public class NoteListActivity extends AppCompatActivity implements EvernoteClientCallback<NoteList>,
        View.OnClickListener, NoteListAdapter.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private FloatingActionButton addNoteButton;
    private ProgressBar progressBar;
    private Spinner sortSpinner;

    private NoteListAdapter noteListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        addNoteButton = (FloatingActionButton) findViewById(R.id.add_note_button);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        sortSpinner = (Spinner) findViewById(R.id.sort_spinner);

        addNoteButton.setOnClickListener(this);

        initRecyclerView();
        initSortSpinner();

    }

    private void retrieveNotes(NoteSortOrder order) {
        showLoading();
        EvernoteApiManager.getInstance(getApplicationContext()).retrieveNotes(order.getValue(), 0, this);
    }

    private void initRecyclerView() {

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        noteListAdapter = new NoteListAdapter();
        noteListAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(noteListAdapter);

    }

    private void initSortSpinner(){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.sort_options));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerArrayAdapter);
        sortSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onSuccess(NoteList result) {
        noteListAdapter.addNotes(result);
        noteListAdapter.notifyDataSetChanged();
        hideLoading();
    }

    @Override
    public void onException(Exception exception) {
        hideLoading();
        //TODO: show error feedback
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_note_button){
            Intent addNoteIntent = new Intent(this, AddNoteActivity.class);
            startActivity(addNoteIntent);
        }
    }

    private void sortBy(NoteSortOrder order){
        noteListAdapter.clearData();
        retrieveNotes(order);
    }

    private void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideLoading(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent noteDetailIntent = new Intent(this, NoteDetailActivity.class);
        noteDetailIntent.putExtra(getString(R.string.note_guid), noteListAdapter.getItem(position).getGuid());
        startActivity(noteDetailIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                sortBy(NoteSortOrder.CREATED);
                break;
            case 1:
                sortBy(NoteSortOrder.TITLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
