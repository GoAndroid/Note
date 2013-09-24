package com.augmentum.note.util;

import com.augmentum.note.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 13-9-23.
 */
public class CalendarUtil {

    public static Calendar getCurrent() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        return c;
    }
    public static String getFormatYmdw(long millis) {
        return getFormat(R.string.format_date_ymdw, millis);
    }

    public static String getFormatYmd(long millis) {
        return getFormat(R.string.format_date_ymd, millis);
    }

    public static String getFormatMdhm(long millis) {
        return getFormat(R.string.format_datetime_mdhm, millis);
    }

    public static String getFormatMd(long millis) {
        return getFormat(R.string.format_date_md, millis);
    }

    public static String getFormatW(long millis) {
        return getFormat(R.string.format_week, millis);
    }

    public static String getFormatHm(long millis) {
        return getFormat(R.string.format_time_hm, millis);
    }

    public static String getFormat(int resId, long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat(Resource.getString(resId));
        return sdf.format(new Date(millis));
    }

}
