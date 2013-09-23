package com.augmentum.note;

import android.app.Application;
import com.augmentum.note.database.NoteDbHelper;

public class NoteApplication extends Application {

    private static NoteApplication sInstance;

    private NoteDbHelper mDbHelper;

    public static NoteApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mDbHelper = new NoteDbHelper(this);
    }

    public NoteDbHelper getDbHelper() {
        return mDbHelper;
    }
}
