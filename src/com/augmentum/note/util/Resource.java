package com.augmentum.note.util;

import android.graphics.drawable.Drawable;
import com.augmentum.note.NoteApplication;

public class Resource {

    public static String getString(int resId) {
        return NoteApplication.getInstance().getString(resId);
    }

    public static String getString(int restId, Object... formatArgs) {
        return NoteApplication.getInstance().getString(restId, formatArgs);
    }

    public static Drawable getDrawable(int resId) {
        return NoteApplication.getInstance().getResources().getDrawable(resId);
    }

    public static Boolean getBoolean(int resId) {
        return NoteApplication.getInstance().getResources().getBoolean(resId);
    }

    public static float getSp(int resId) {
        float scaledDensity = NoteApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return NoteApplication.getInstance().getResources().getDimension(resId) / scaledDensity;
    }
}
