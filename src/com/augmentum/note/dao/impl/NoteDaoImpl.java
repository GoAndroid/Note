package com.augmentum.note.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.augmentum.note.dao.NoteDao;
import com.augmentum.note.database.NoteDbHelper;
import com.augmentum.note.model.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteDaoImpl implements NoteDao {

    @Override
    public void insert(NoteDbHelper dbHelper, Note note) {
        SQLiteDatabase db =  dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Note.NoteEntry.COLUMN_NAME_TYPE, note.getType());
        values.put(Note.NoteEntry.COLUMN_NAME_PARENT_ID, note.getParentId());
        values.put(Note.NoteEntry.COLUMN_NAME_COLOR, note.getColor());
        values.put(Note.NoteEntry.COLUMN_NAME_CREATE_TIME, note.getCreateTime().getTime());
        values.put(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME, System.currentTimeMillis());
        values.put(Note.NoteEntry.COLUMN_NAME_CONTENT, note.getContent());

        long newRowId = db.insert(Note.NoteEntry.TABLE_NAME, null, values);
    }

    @Override
    public void update(NoteDbHelper dbHelper, Note note) {
        SQLiteDatabase db =  dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Note.NoteEntry.COLUMN_NAME_TYPE, note.getType());
        values.put(Note.NoteEntry.COLUMN_NAME_PARENT_ID, note.getParentId());
        values.put(Note.NoteEntry.COLUMN_NAME_COLOR, note.getColor());
        values.put(Note.NoteEntry.COLUMN_NAME_CREATE_TIME, note.getCreateTime().getTime());
        values.put(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME, System.currentTimeMillis());
        values.put(Note.NoteEntry.COLUMN_NAME_CONTENT, note.getContent());

        String selection = Note.NoteEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(note.getId()) };

        db.update(
                Note.NoteEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    @Override
    public void delete(NoteDbHelper dbHelper, Note note) {
        SQLiteDatabase db =  dbHelper.getWritableDatabase();
        String selection = Note.NoteEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(note.getId()) };
        db.delete(Note.NoteEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public List<Note> query(NoteDbHelper dbHelper) {

        List<Note> list = new ArrayList<Note>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                Note.NoteEntry._ID,
                Note.NoteEntry.COLUMN_NAME_TYPE,
                Note.NoteEntry.COLUMN_NAME_PARENT_ID,
                Note.NoteEntry.COLUMN_NAME_COLOR,
                Note.NoteEntry.COLUMN_NAME_CONTENT,
                Note.NoteEntry.COLUMN_NAME_SUBJECT,
                Note.NoteEntry.COLUMN_NAME_MODIFY_TIME,
                Note.NoteEntry.COLUMN_NAME_CREATE_TIME
        };

        String sortOrder = Note.NoteEntry.COLUMN_NAME_TYPE + " DESC";

        Cursor cursor = db.query(
                Note.NoteEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        Note note;

        while (cursor.moveToNext()) {
            note = new Note();
            note.setId(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry._ID)));
            note.setType(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_TYPE)));
            note.setParentId(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_PARENT_ID)));
            note.setColor(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_COLOR)));
            note.setContent(cursor.getString(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_CONTENT)));
            note.setSubject(cursor.getString(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_SUBJECT)));
            note.setCreateTime(new Date(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_CREATE_TIME))));
            note.setModifyTime(new Date(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME)));
            list.add(note);
        }

        return  list;
    }
}
