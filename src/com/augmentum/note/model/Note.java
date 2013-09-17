package com.augmentum.note.model;

import android.provider.BaseColumns;

import java.util.Calendar;

public class Note {

    public static final int NO_PARENT = -1;
    public static final int TYPE_NOTE = 1;
    public static final int TYPE_FOLDER = 2;
    public static final int ENTER_DESKTOP_FLAG_FALSE = 0;
    public static final int ENTER_DESKTOP_FLAG_TRUE = 1;

    private int mId;
    private int mType;
    private int mColor;
    private int mEnterDesktopFlag;
    private int mParentId;
    private int mWidgetId;
    private Calendar mCreateTime;
    private Calendar mModifyTime;
    private Calendar mAlertTime;
    private String mContent;
    private String mSubject;

    public Note(){}

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public int getEnterDesktopFlag() {
        return mEnterDesktopFlag;
    }

    public void setEnterDesktopFlag(int enterDesktopFlag) {
        mEnterDesktopFlag = enterDesktopFlag;
    }

    public int getParentId() {
        return mParentId;
    }

    public void setParentId(int parentId) {
        mParentId = parentId;
    }

    public int getWidgetId() {
        return mWidgetId;
    }

    public void setWidgetId(int widgetId) {
        mWidgetId = widgetId;
    }

    public Calendar getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(Calendar createTime) {
        mCreateTime = createTime;
    }

    public Calendar getModifyTime() {
        return mModifyTime;
    }

    public void setModifyTime(Calendar modifyTime) {
        mModifyTime = modifyTime;
    }

    public Calendar getAlertTime() {
        return mAlertTime;
    }

    public void setAlertTime(Calendar alertTime) {
        mAlertTime = alertTime;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public static abstract class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "note";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_COLOR = "color";
        public static final String COLUMN_NAME_PARENT_ID = "parent_id";
        public static final String COLUMN_NAME_widget_ID = "widget_id";
        public static final String COLUMN_NAME_ENTER_DESKTOP_FLAG = "enter_desktop_flag";
        public static final String COLUMN_NAME_CREATE_TIME = "create_time";
        public static final String COLUMN_NAME_MODIFY_TIME = "modify_time";
        public static final String COLUMN_NAME_ALERT_TIME = "alert_time";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_SUBJECT = "subject";

    }
}
