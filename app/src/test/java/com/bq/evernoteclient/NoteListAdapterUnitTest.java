package com.bq.evernoteclient;

import com.bq.evernoteclient.notes.adapter.NoteListAdapter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class NoteListAdapterUnitTest {

    NoteListAdapter adapter;

    @Before
    public void setUp(){

        adapter = new NoteListAdapter();
        adapter.addNotes(exampleNoteList());

    }

    @Test
    public void getItem() throws Exception {

        assertEquals(adapter.getItem(0).getTitle(), "title1");
        assertEquals(adapter.getItem(0).getContent(), "content1");;

        assertEquals(adapter.getItem(1).getTitle(), "title2");
        assertEquals(adapter.getItem(1).getContent(), "content2");;

    }

    @Test
    public void getItemCount() throws Exception {

        assertEquals(adapter.getItemCount(), 2);

    }

    @Test
    public void getItemViewType() throws Exception {

        adapter.addLoading();
        assertEquals(adapter.getItemViewType(2), NoteListAdapter.LOADING);
        assertEquals(adapter.getItemViewType(1), NoteListAdapter.ITEM);
        assertEquals(adapter.getItemViewType(0), NoteListAdapter.ITEM);

    }

    private NoteList exampleNoteList(){

        NoteList noteList = new NoteList();
        List<Note> notes = new ArrayList<Note>();

        Note note1 = new Note();
        note1.setTitle("title1");
        note1.setContent("content1");
        notes.add(note1);

        Note note2 = new Note();
        note2.setTitle("title2");
        note2.setContent("content2");
        notes.add(note2);

        noteList.setNotes(notes);

        return noteList;
    }
}