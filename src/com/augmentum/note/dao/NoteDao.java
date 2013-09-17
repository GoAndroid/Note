package com.augmentum.note.dao;

import com.augmentum.note.database.NoteDbHelper;
import com.augmentum.note.model.Note;

import java.util.List;

public interface NoteDao {

    public void insert(NoteDbHelper dbHelper, Note note);

    public void update(NoteDbHelper dbHelper, Note note);

    public void delete(NoteDbHelper dbHelper, Note note);

    public List<Note> query(NoteDbHelper dbHelper);

}
