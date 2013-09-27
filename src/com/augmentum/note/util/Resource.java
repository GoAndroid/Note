package com.augmentum.note.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.augmentum.note.NoteApplication;

public class Resource {


    public static String getString(int resId) {
        return getResource().getString(resId);
    }

    public static String getString(int restId, Object... formatArgs) {
        return getResource().getString(restId, formatArgs);
    }

    public static String[] getStringArray(int resId) {
        return getResource().getStringArray(resId);
    }

    public static Drawable getDrawable(int resId) {
        return getResource().getDrawable(resId);
    }

    public static Boolean getBoolean(int resId) {
        return getResource().getBoolean(resId);
    }

    public static float getPx(int resId) {
        return getResource().getDimension(resId);
    }

    public static float getDp(int resId) {
        float scale = getResource().getDisplayMetrics().density;
        return getResource().getDimension(resId) / scale;
    }

    public static float getPt(int resId) {
        float xdpi = getResource().getDisplayMetrics().xdpi;
        return getResource().getDimension(resId) / xdpi * 72;
    }

    public static float getSp(int resId) {
        float scaledDensity = getResource().getDisplayMetrics().scaledDensity;
        return getResource().getDimension(resId) / scaledDensity;
    }

    public static float getIn(int resId) {
        float xdpi = getResource().getDisplayMetrics().xdpi;
        return getResource().getDimension(resId) / xdpi;
    }

    public static float getMm(int resId) {
        float xdpi = getResource().getDisplayMetrics().xdpi;
        return getResource().getDimension(resId) / xdpi * 25.4f;
    }

    private static Resources getResource() {
        return NoteApplication.getInstance().getResources();
    }

}
