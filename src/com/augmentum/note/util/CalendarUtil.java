package com.augmentum.note.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 13-9-23.
 */
public class CalendarUtil {

    public static String getCurrentFormatText(String formatText) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatText);
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    public static String getFormatText(String formatText, long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatText);
        return sdf.format(new Date(millis));
    }

}
