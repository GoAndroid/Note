package com.augmentum.note.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.augmentum.note.model.Note;

public class NoteDbHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Note.NoteEntry.TABLE_NAME + " (" +
                    Note.NoteEntry._ID + " INTEGER PRIMARY KEY," +
                    Note.NoteEntry.COLUMN_NAME_TYPE + INTEGER_TYPE + COMMA_SEP +
                    Note.NoteEntry.COLUMN_NAME_COLOR + INTEGER_TYPE + COMMA_SEP +
                    Note.NoteEntry.COLUMN_NAME_PARENT_ID + INTEGER_TYPE + COMMA_SEP +
                    Note.NoteEntry.COLUMN_NAME_widget_ID + INTEGER_TYPE + COMMA_SEP +
                    Note.NoteEntry.COLUMN_NAME_ENTER_DESKTOP_FLAG + INTEGER_TYPE + COMMA_SEP +
                    Note.NoteEntry.COLUMN_NAME_CREATE_TIME + INTEGER_TYPE + COMMA_SEP +
                    Note.NoteEntry.COLUMN_NAME_MODIFY_TIME + INTEGER_TYPE + COMMA_SEP +
                    Note.NoteEntry.COLUMN_NAME_ALERT_TIME + INTEGER_TYPE + COMMA_SEP +
                    Note.NoteEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    Note.NoteEntry.COLUMN_NAME_SUBJECT + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Note.NoteEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Note.db";

    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
