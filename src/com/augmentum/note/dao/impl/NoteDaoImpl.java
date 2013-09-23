package com.augmentum.note.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.augmentum.note.NoteApplication;
import com.augmentum.note.dao.NoteDao;
import com.augmentum.note.database.NoteDbHelper;
import com.augmentum.note.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDaoImpl implements NoteDao {

    private static NoteDaoImpl instance = new NoteDaoImpl();
    private NoteDbHelper mDbHelper;

    private NoteDaoImpl() {
        mDbHelper = NoteApplication.getInstance().getDbHelper();
    }

    public static NoteDaoImpl getInstance() {
        return instance;
    }

    /**
     * Insert a <code>Note</code> object to database, a note will be insert with a type, parent_id, create_time,
     * if is a note_type insert with color, modifyTime, Content,
     * if is a folder_type insert with subject.
     *
     * @param note which <code>note</code> object will be insert
     */
    @Override
    public void insert(Note note) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Note.NoteEntry.COLUMN_NAME_TYPE, note.getType());
        values.put(Note.NoteEntry.COLUMN_NAME_PARENT_ID, note.getParentId());
        values.put(Note.NoteEntry.COLUMN_NAME_CREATE_TIME, note.getCreateTime());

        if (Note.TYPE_NOTE == note.getType()) {
            values.put(Note.NoteEntry.COLUMN_NAME_COLOR, note.getColor());
            values.put(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME, note.getModifyTime());
            values.put(Note.NoteEntry.COLUMN_NAME_CONTENT, note.getContent());
        } else {
            values.put(Note.NoteEntry.COLUMN_NAME_SUBJECT, note.getSubject());
        }

        if (db != null) {
            db.insert(Note.NoteEntry.TABLE_NAME, null, values);
        }
    }

    /**
     * Update a <code>Note</code> object to database,
     * if is a note_type with parent_id, color, content, modify_time
     * if is a folder_type with subject
     *
     * @param note which <code>Note</code> object will be delete Id can't be null
     */
    @Override
    public void update(Note note) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (Note.TYPE_NOTE == note.getType()) {
            values.put(Note.NoteEntry.COLUMN_NAME_PARENT_ID, note.getParentId());
            values.put(Note.NoteEntry.COLUMN_NAME_COLOR, note.getColor());
            values.put(Note.NoteEntry.COLUMN_NAME_CONTENT, note.getContent());
            values.put(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME, note.getModifyTime());
        } else {
            values.put(Note.NoteEntry.COLUMN_NAME_SUBJECT, note.getSubject());
        }

        String selection = Note.NoteEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(note.getId())};

        if (null != db) {
            db.update(
                    Note.NoteEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
            );
        }
    }

    /**
     * Delete a <code>Note</code> object from database by id,
     * if note is a folder_type delete with children.
     *
     * @param note which <code>Note</code> be delete Id can't be null
     */
    @Override
    public void delete(Note note) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        if (Note.TYPE_FOLDER == note.getType()) {
            String selection = Note.NoteEntry.COLUMN_NAME_PARENT_ID + " = ? or " + Note.NoteEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(note.getId()), String.valueOf(note.getId())};

            if (null != db) {
                db.delete(Note.NoteEntry.TABLE_NAME, selection, selectionArgs);
            }

        } else {
            String selection = Note.NoteEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(note.getId())};

            if (null != db) {
                db.delete(Note.NoteEntry.TABLE_NAME, selection, selectionArgs);
            }

        }

    }

    /**
     * Get All <code>Note</code> object have no parent.
     *
     * @return a list of <code>Note</code> object
     */
    @Override
    public List<Note> getAllNoParent() {
        List<Note> list = new ArrayList<Note>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

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

        String selection = Note.NoteEntry.COLUMN_NAME_PARENT_ID +
                " = " + Note.NO_PARENT;
        String sortOrder = Note.NoteEntry.COLUMN_NAME_TYPE + " DESC, " +
                Note.NoteEntry.COLUMN_NAME_CREATE_TIME + " DESC";

        Cursor cursor = null;

        if (db != null) cursor = db.query(
                Note.NoteEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                sortOrder
        );

        Note note;

        while (cursor != null && cursor.moveToNext()) {
            note = new Note();
            note.setId(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry._ID)));
            note.setType(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_TYPE)));

            note.setCreateTime(cursor.getLong(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_CREATE_TIME)));

            if (Note.TYPE_NOTE == note.getType()) {
                note.setModifyTime(cursor.getLong(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME)));
                note.setParentId(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_PARENT_ID)));
                note.setColor(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_COLOR)));
                note.setContent(cursor.getString(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_CONTENT)));
            }

            if (Note.TYPE_FOLDER == note.getType()) {
                String subject = cursor.getString(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_SUBJECT));
                note.setSubject(subject);
                note.setChildCount(getChildCount(note));
                note.setModifyTime(getChildrenModifyTime(note));
            }

            list.add(note);
        }

        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

    /**
     * Get all <code>Note</code> object what are children of parent.
     * @param parent a <code>Note</code>object type is folder
     * @return a list of <code>Note</code> object type is note
     */
    @Override
    public List<Note> getChildren(Note parent) {
        List<Note> list = new ArrayList<Note>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

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
        String selection = Note.NoteEntry.COLUMN_NAME_PARENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(parent.getId())};
        String sortOrder = Note.NoteEntry.COLUMN_NAME_MODIFY_TIME + " DESC";

        Cursor cursor = null;

        if (db != null) {
            cursor = db.query(
                    Note.NoteEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
        }

        Note noteTemp;

        while (cursor != null && cursor.moveToNext()) {
            noteTemp = new Note();
            noteTemp.setId(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry._ID)));
            noteTemp.setType(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_TYPE)));
            noteTemp.setParentId(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_PARENT_ID)));
            noteTemp.setColor(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_COLOR)));
            noteTemp.setContent(cursor.getString(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_CONTENT)));
            noteTemp.setSubject(cursor.getString(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_SUBJECT)));
            noteTemp.setCreateTime(cursor.getLong(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_CREATE_TIME)));
            noteTemp.setModifyTime(cursor.getLong(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME)));
            list.add(noteTemp);
        }

        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

    /**
     * Get all <code>Note</code> object which type is folder
     * @return a list of folder type <code>Note</code> Object
     */
    @Override
    public List<Note> getFolders() {
        List<Note> result = new ArrayList<Note>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Note.NoteEntry._ID,
                Note.NoteEntry.COLUMN_NAME_SUBJECT
        };

        String selection = Note.NoteEntry.COLUMN_NAME_TYPE + " = ?";
        String[] selectionArgs = {String.valueOf(Note.TYPE_FOLDER)};

        Cursor cursor = null;

        if (db != null) {
            cursor = db.query(
                    Note.NoteEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        }

        Note note;

        while (cursor != null && cursor.moveToNext()) {
            note = new Note();
            note.setId(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry._ID)));
            note.setSubject(cursor.getString(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_SUBJECT)));
            result.add(note);
        }

        if (cursor != null) {
            cursor.close();
        }

        return result;
    }

    private int getChildCount(Note parent) {
        int result;

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Note.NoteEntry._ID
        };

        String selection = Note.NoteEntry.COLUMN_NAME_PARENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(parent.getId())};

        Cursor cursor = null;

        if (db != null) {
            cursor = db.query(
                    Note.NoteEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        }

        result = cursor != null ? cursor.getCount() : 0;

        if (cursor != null) {
            cursor.close();
        }

        return result;
    }

    private long getChildrenModifyTime(Note parent) {
        long result = 0;

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Note.NoteEntry.COLUMN_NAME_MODIFY_TIME
        };

        String selection = Note.NoteEntry.COLUMN_NAME_PARENT_ID + " = ?";
        String[] selectionArgs = {
                String.valueOf(parent.getId())
        };
        String orderBy = Note.NoteEntry.COLUMN_NAME_MODIFY_TIME + " DESC";

        Cursor cursor = null;

        if (db != null) {
            cursor = db.query(
                    Note.NoteEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    orderBy
            );
        }

        if (cursor != null && cursor.moveToFirst()) {
            if (-1 != cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME)) {
                result = cursor.getLong(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME));
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return result;
    }
}
