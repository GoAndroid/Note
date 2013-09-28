package com.augmentum.note.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.augmentum.note.NoteApplication;
import com.augmentum.note.dao.NoteDao;
import com.augmentum.note.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDaoImpl implements NoteDao {

    private static NoteDaoImpl instance = new NoteDaoImpl();
    private SQLiteDatabase mDatabase;

    private NoteDaoImpl() {
        mDatabase = NoteApplication.getInstance().getDbHelper().getWritableDatabase();
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
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    @Override
    public long insert(Note note) {
        long result = 0;
        ContentValues values = new ContentValues();
        values.put(Note.NoteEntry.COLUMN_NAME_TYPE, note.getType());
        values.put(Note.NoteEntry.COLUMN_NAME_PARENT_ID, note.getParentId());
        values.put(Note.NoteEntry.COLUMN_NAME_CREATE_TIME, note.getCreateTime());

        if (Note.TYPE_NOTE == note.getType()) {
            values.put(Note.NoteEntry.COLUMN_NAME_COLOR, note.getColor());
            values.put(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME, note.getModifyTime());
            values.put(Note.NoteEntry.COLUMN_NAME_CONTENT, note.getContent());
            values.put(Note.NoteEntry.COLUMN_NAME_ALERT_TIME, note.getAlertTime());
        } else {
            values.put(Note.NoteEntry.COLUMN_NAME_SUBJECT, note.getSubject());
        }

        values.put(Note.NoteEntry.COLUMN_NAME_ENTER_DESKTOP_FLAG, note.getEnterDesktopFlag());
        values.put(Note.NoteEntry.COLUMN_NAME_WIDGET_ID, note.getWidgetId());

        if (mDatabase != null) {
            result = mDatabase.insert(Note.NoteEntry.TABLE_NAME, null, values);
        }

        return result;
    }

    /**
     * Restore notes from a list of note
     *
     * @param notes restore list.
     */
    @Override
    public void insertALL(List<Note> notes) {

        if (mDatabase != null) {
            mDatabase.beginTransaction();

            try {

                for (Note note : notes) {
                    ContentValues values = new ContentValues();
                    values.put(Note.NoteEntry._ID, note.getId());
                    values.put(Note.NoteEntry.COLUMN_NAME_TYPE, note.getType());
                    values.put(Note.NoteEntry.COLUMN_NAME_PARENT_ID, note.getParentId());
                    values.put(Note.NoteEntry.COLUMN_NAME_CREATE_TIME, note.getCreateTime());

                    if (Note.TYPE_NOTE == note.getType()) {
                        values.put(Note.NoteEntry.COLUMN_NAME_COLOR, note.getColor());
                        values.put(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME, note.getModifyTime());
                        values.put(Note.NoteEntry.COLUMN_NAME_CONTENT, note.getContent());
                        values.put(Note.NoteEntry.COLUMN_NAME_ALERT_TIME, note.getAlertTime());
                    } else {
                        values.put(Note.NoteEntry.COLUMN_NAME_SUBJECT, note.getSubject());
                    }

                    values.put(Note.NoteEntry.COLUMN_NAME_ENTER_DESKTOP_FLAG, note.getEnterDesktopFlag());
                    values.put(Note.NoteEntry.COLUMN_NAME_WIDGET_ID, note.getWidgetId());

                    mDatabase.insertOrThrow(Note.NoteEntry.TABLE_NAME, null, values);
                }

                mDatabase.setTransactionSuccessful();
            } finally {
                mDatabase.endTransaction();
            }

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
        ContentValues values = new ContentValues();

        if (Note.TYPE_NOTE == note.getType()) {
            values.put(Note.NoteEntry.COLUMN_NAME_PARENT_ID, note.getParentId());
            values.put(Note.NoteEntry.COLUMN_NAME_COLOR, note.getColor());
            values.put(Note.NoteEntry.COLUMN_NAME_CONTENT, note.getContent());
            values.put(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME, note.getModifyTime());
            values.put(Note.NoteEntry.COLUMN_NAME_ALERT_TIME, note.getAlertTime());
        } else {
            values.put(Note.NoteEntry.COLUMN_NAME_SUBJECT, note.getSubject());
        }

        values.put(Note.NoteEntry.COLUMN_NAME_ENTER_DESKTOP_FLAG, note.getEnterDesktopFlag());
        values.put(Note.NoteEntry.COLUMN_NAME_WIDGET_ID, note.getWidgetId());

        String selection = Note.NoteEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(note.getId())};

        if (null != mDatabase) {
            mDatabase.update(
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

        if (Note.TYPE_FOLDER == note.getType()) {
            String selection = Note.NoteEntry.COLUMN_NAME_PARENT_ID + " = ? or " + Note.NoteEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(note.getId()), String.valueOf(note.getId())};

            if (null != mDatabase) {
                mDatabase.delete(Note.NoteEntry.TABLE_NAME, selection, selectionArgs);
            }

        } else {
            String selection = Note.NoteEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(note.getId())};

            if (null != mDatabase) {
                mDatabase.delete(Note.NoteEntry.TABLE_NAME, selection, selectionArgs);
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
        List<Note> result = new ArrayList<Note>();

        String[] projection = createProjection();

        String selection = Note.NoteEntry.COLUMN_NAME_PARENT_ID +
                " = " + Note.NO_PARENT;
        String sortOrder = Note.NoteEntry.COLUMN_NAME_TYPE + " DESC, " +
                Note.NoteEntry.COLUMN_NAME_CREATE_TIME + " DESC";

        Cursor cursor = null;

        if (mDatabase != null) cursor = mDatabase.query(
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
                note.setAlertTime(cursor.getLong(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_ALERT_TIME)));
            }

            if (Note.TYPE_FOLDER == note.getType()) {
                String subject = cursor.getString(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_SUBJECT));
                note.setSubject(subject);
                note.setChildCount(getChildCount(note));
                note.setModifyTime(getChildrenModifyTime(note));
            }

            note.setEnterDesktopFlag(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_ENTER_DESKTOP_FLAG)));
            note.setWidgetId(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_WIDGET_ID)));
            result.add(note);
        }

        if (cursor != null) {
            cursor.close();
        }

        return result;
    }

    /**
     * Get all <code>Note</code> object what are children of parent.
     *
     * @param parent a <code>Note</code>object type is folder
     * @return a list of <code>Note</code> object type is note
     */
    @Override
    public List<Note> getChildren(Note parent) {
        List<Note> result = new ArrayList<Note>();

        String[] projection = createProjection();
        String selection = Note.NoteEntry.COLUMN_NAME_PARENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(parent.getId())};
        String sortOrder = Note.NoteEntry.COLUMN_NAME_MODIFY_TIME + " DESC";

        Cursor cursor = null;

        if (mDatabase != null) {
            cursor = mDatabase.query(
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
            noteTemp.setAlertTime(cursor.getLong(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_ALERT_TIME)));
            noteTemp.setEnterDesktopFlag(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_ENTER_DESKTOP_FLAG)));
            noteTemp.setWidgetId(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_WIDGET_ID)));
            result.add(noteTemp);
        }

        if (cursor != null) {
            cursor.close();
        }

        return result;
    }

    /**
     * Get all <code>Note</code> object which type is folder
     *
     * @return a list of folder type <code>Note</code> Object
     */
    @Override
    public List<Note> getFolders() {
        List<Note> result = new ArrayList<Note>();

        String[] projection = {
                Note.NoteEntry._ID,
                Note.NoteEntry.COLUMN_NAME_SUBJECT,
                Note.NoteEntry.COLUMN_NAME_ENTER_DESKTOP_FLAG
        };

        String selection = Note.NoteEntry.COLUMN_NAME_TYPE + " = ?";
        String[] selectionArgs = {String.valueOf(Note.TYPE_FOLDER)};

        Cursor cursor = null;

        if (mDatabase != null) {
            cursor = mDatabase.query(
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
            note.setEnterDesktopFlag(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_ENTER_DESKTOP_FLAG)));
            result.add(note);
        }

        if (cursor != null) {
            cursor.close();
        }

        return result;
    }

    @Override
    public List<Note> getAll() {
        List<Note> result = new ArrayList<Note>();

        String[] projection = createProjection();

        Cursor cursor = null;

        if (mDatabase != null) cursor = mDatabase.query(
                Note.NoteEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        Note noteTemp;

        while (cursor != null && cursor.moveToNext()) {
            noteTemp = createNoteFromeCursor(cursor);

            result.add(noteTemp);
        }

        if (cursor != null) {
            cursor.close();
        }

        return result;
    }

    /**
     * Delete all <code>Note</code> object in database.
     */
    @Override
    public void deleteAll() {
        mDatabase.delete(Note.NoteEntry.TABLE_NAME, null, null);
    }

    private int getChildCount(Note parent) {
        int result = 0;

        String[] projection = {
                Note.NoteEntry._ID
        };

        String selection = Note.NoteEntry.COLUMN_NAME_PARENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(parent.getId())};

        Cursor cursor = null;

        if (mDatabase != null) {
            cursor = mDatabase.query(
                    Note.NoteEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        }

        if (cursor != null) {
            result = cursor.getCount();
            cursor.close();
        }

        return result;
    }

    private long getChildrenModifyTime(Note parent) {
        long result = 0;

        String[] projection = {
                Note.NoteEntry.COLUMN_NAME_MODIFY_TIME
        };

        String selection = Note.NoteEntry.COLUMN_NAME_PARENT_ID + " = ?";
        String[] selectionArgs = {
                String.valueOf(parent.getId())
        };
        String orderBy = Note.NoteEntry.COLUMN_NAME_MODIFY_TIME + " DESC";

        Cursor cursor = null;

        if (mDatabase != null) {
            cursor = mDatabase.query(
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

    @Override
    public int[] getCount() {
        int[] result = {0, 0};

        if (mDatabase != null) {
            mDatabase.beginTransaction();
            String[] projection = {
                    Note.NoteEntry.COLUMN_NAME_TYPE
            };

            String selectionFolder = Note.NoteEntry.COLUMN_NAME_TYPE + " = " + Note.TYPE_FOLDER;
            String selectionNote = Note.NoteEntry.COLUMN_NAME_TYPE + " = " + Note.TYPE_NOTE;

            Cursor cursorFolder = mDatabase.query(
                    Note.NoteEntry.TABLE_NAME,
                    projection,
                    selectionFolder,
                    null,
                    null,
                    null,
                    null
            );

            if (cursorFolder != null) {
                result[0] = cursorFolder.getCount();
                cursorFolder.close();
            }

            Cursor cursorNote = mDatabase.query(
                    Note.NoteEntry.TABLE_NAME,
                    projection,
                    selectionNote,
                    null,
                    null,
                    null,
                    null
            );

            if (cursorNote != null) {
                result[1] = cursorNote.getCount();
                cursorNote.close();
            }

            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        }

        return result;
    }

    @Override
    public Note getById(long id) {
        Note result = null;
        String[] projection = createProjection();

        String selection = Note.NoteEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = null;

        if (mDatabase != null) cursor = mDatabase.query(
                Note.NoteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );



        if (cursor != null && cursor.moveToFirst()) {
            result = createNoteFromeCursor(cursor);

        }

        if (cursor != null) {
            cursor.close();
        }

        return result;
    }

    public Note getByWidgetId(int widgetId) {
        Note result = null;
        String[] projection = createProjection();

        String selection = Note.NoteEntry.COLUMN_NAME_WIDGET_ID + " = ?";
        String[] selectionArgs = {String.valueOf(widgetId)};
        Cursor cursor = null;

        if (mDatabase != null) cursor = mDatabase.query(
                Note.NoteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            result = createNoteFromeCursor(cursor);

        }

        if (cursor != null) {
            cursor.close();
        }

        return result;
    }

    private Note createNoteFromeCursor(Cursor cursor) {
        Note result;
        result = new Note();
        result.setId(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry._ID)));
        result.setParentId(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_PARENT_ID)));
        result.setType(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_TYPE)));
        result.setCreateTime(cursor.getLong(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_CREATE_TIME)));

        if (Note.TYPE_NOTE == result.getType()) {
            result.setModifyTime(cursor.getLong(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME)));
            result.setParentId(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_PARENT_ID)));
            result.setColor(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_COLOR)));
            result.setContent(cursor.getString(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_CONTENT)));
            result.setAlertTime(cursor.getLong(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_ALERT_TIME)));
        }

        if (Note.TYPE_FOLDER == result.getType()) {
            String subject = cursor.getString(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_SUBJECT));
            result.setSubject(subject);
            result.setChildCount(getChildCount(result));
            result.setModifyTime(getChildrenModifyTime(result));
        }

        result.setEnterDesktopFlag(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_ENTER_DESKTOP_FLAG)));
        result.setWidgetId(cursor.getInt(cursor.getColumnIndex(Note.NoteEntry.COLUMN_NAME_WIDGET_ID)));
        return result;
    }

    private String[] createProjection() {
        return new String[]{
                    Note.NoteEntry._ID,
                    Note.NoteEntry.COLUMN_NAME_TYPE,
                    Note.NoteEntry.COLUMN_NAME_PARENT_ID,
                    Note.NoteEntry.COLUMN_NAME_COLOR,
                    Note.NoteEntry.COLUMN_NAME_CONTENT,
                    Note.NoteEntry.COLUMN_NAME_SUBJECT,
                    Note.NoteEntry.COLUMN_NAME_MODIFY_TIME,
                    Note.NoteEntry.COLUMN_NAME_CREATE_TIME,
                    Note.NoteEntry.COLUMN_NAME_ALERT_TIME,
                    Note.NoteEntry.COLUMN_NAME_ENTER_DESKTOP_FLAG,
                    Note.NoteEntry.COLUMN_NAME_WIDGET_ID
            };
    }
}
