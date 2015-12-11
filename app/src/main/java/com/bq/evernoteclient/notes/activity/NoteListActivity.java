package com.bq.evernoteclient.notes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.bq.evernoteclient.R;
import com.bq.evernoteclient.evernoteapi.EvernoteApiManager;
import com.bq.evernoteclient.evernoteapi.EvernoteClientCallback;
import com.bq.evernoteclient.notes.adapter.NoteListAdapter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.NoteSortOrder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by David on 10/12/15.
 */
public class NoteListActivity extends AppCompatActivity implements EvernoteClientCallback<NoteList>,
        View.OnClickListener, NoteListAdapter.OnItemClickListener, AdapterView.OnItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener{

    private static final int MAX_ITEMS = 20;
    private static final int CREATE_NOTE_CODE = 0;

    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.add_note_button) FloatingActionButton addNoteButton;
    @Bind(R.id.progress_bar) ProgressBar progressBar;
    @Bind(R.id.sort_spinner) Spinner sortSpinner;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.feedback) LinearLayout feedbackLayout;
    @Bind(R.id.retry_button) Button retryButton;

    private NoteListAdapter noteListAdapter;
    private boolean isLastPage;
    private boolean isLoading;
    private int currentOffset = 0;
    private NoteSortOrder selectedOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addNoteButton.setOnClickListener(this);
        retryButton.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        initRecyclerView();
        initSortSpinner();

    }

    private void retrieveNotes(NoteSortOrder order) {
        if (currentOffset == 0){
            showLoading();
        }
        EvernoteApiManager.getInstance(getApplicationContext()).retrieveNotes(order.getValue(), currentOffset, this);
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
        currentOffset += result.getNotesSize();
        if (result.getNotesSize() >= MAX_ITEMS) {
            noteListAdapter.addLoading();
        } else {
            isLastPage = true;
        }
        hideLoading();
    }

    @Override
    public void onException(Exception exception) {
        hideLoading();
        showErrorFeedback();
    }

    private void showErrorFeedback() {
        feedbackLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.add_note_button:
                Intent addNoteIntent = new Intent(this, AddNoteActivity.class);
                startActivityForResult(addNoteIntent, CREATE_NOTE_CODE);
                break;
            case R.id.retry_button:
                sortBy(selectedOrder);
                break;
        }

    }

    private void sortBy(NoteSortOrder order){
        currentOffset = 0;
        noteListAdapter.clearData();
        retrieveNotes(order);
    }

    private void showLoading(){
        feedbackLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideLoading(){
        isLoading = false;
        swipeRefreshLayout.setRefreshing(false);
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
                selectedOrder = NoteSortOrder.CREATED;
                break;
            case 1:
                selectedOrder = NoteSortOrder.TITLE;
        }
        sortBy(selectedOrder);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public RecyclerView.OnScrollListener retrieveScrollListener(final RecyclerView.LayoutManager layoutManager){
        RecyclerView.OnScrollListener
                recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= MAX_ITEMS) {
                        loadMoreItems();
                    }
                }
            }

        };

        return recyclerViewOnScrollListener;
    }

    private void loadMoreItems() {
        isLoading = true;
        retrieveNotes(selectedOrder);
    }

    @Override
    public void onRefresh() {
        sortBy(selectedOrder);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CREATE_NOTE_CODE:
                if (resultCode == RESULT_OK) {
                    Snackbar.make(findViewById(R.id.content), getString(R.string.note_created_feedback),
                            Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }
}
