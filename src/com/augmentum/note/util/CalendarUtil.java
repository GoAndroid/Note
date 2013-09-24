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
    public static String getYmdw(long millis) {
        return getFormat(R.string.format_date_ymdw, millis);
    }

    public static String getYmd(long millis) {
        return getFormat(R.string.format_date_ymd, millis);
    }

    public static String getMdhm(long millis) {
        return getFormat(R.string.format_datetime_mdhm, millis);
    }

    public static String getMd(long millis) {
        return getFormat(R.string.format_date_md, millis);
    }

    public static String getW(long millis) {
        return getFormat(R.string.format_week, millis);
    }

    public static String getHm(long millis) {
        return getFormat(R.string.format_time_hm, millis);
    }

    public static String getFormat(int resId, long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat(Resource.getString(resId));
        return sdf.format(new Date(millis));
    }

}
