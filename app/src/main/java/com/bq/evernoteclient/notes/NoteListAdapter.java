package com.bq.evernoteclient.notes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bq.evernoteclient.R;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 10/12/15.
 */
public class NoteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private NoteList notes = new NoteList();

    public void addNotes(NoteList notes) {
        for (Note note : notes.getNotes()) {
            this.notes.addToNotes(note);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);

        NoteViewHolder viewHolder = new NoteViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        NoteViewHolder noteViewHolder = (NoteViewHolder) holder;
        noteViewHolder.title.setText(notes.getNotes().get(position).getTitle());
        noteViewHolder.content.setText(notes.getNotes().get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return notes.getNotesSize();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title;
        public TextView content;

        public NoteViewHolder(View itemView) {

            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_textview);
            content = (TextView) itemView.findViewById(R.id.content_textview);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
