package com.augmentum.note.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import org.json.JSONException;
import org.json.JSONObject;

public class Note implements Parcelable {

    private static final long serialVersionUID = -2454978783620245981L;
    public static final String TAG = "note";
    public static final String PARENT_TAG = "parent_tag";
    public static final String NOTE_TAG = "note_tag";

    public static final int NO_PARENT = -1;
    public static final int TYPE_NOTE = 1;
    public static final int TYPE_FOLDER = 2;
    public static final int ENTER_DESKTOP_FLAG_FALSE = 0;
    public static final int ENTER_DESKTOP_FLAG_TRUE = 1;

    private int mChildCount;
    private long mId;
    private long mParentId;
    private int mType;
    private int mColor;
    private int mEnterDesktopFlag;
    private int mWidgetId;
    private long mCreateTime;
    private long mModifyTime;
    private long mAlertTime;
    private String mContent = null;
    private String mSubject = null;

    public Note() {
    }

    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (!(other instanceof Note)) {
            return false;
        }

        final Note note = (Note) other;

        return (getId() == note.getId()) && (getType() == note.getType());

    }

    @Override
    public int hashCode() {
        return 29 * (int) getId() + 17 * getType();
    }

    public int getChildCount() {
        return mChildCount;
    }

    public void setChildCount(int childCount) {
        mChildCount = childCount;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
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

    public long getParentId() {
        return mParentId;
    }

    public void setParentId(long parentId) {
        mParentId = parentId;
    }

    public int getWidgetId() {
        return mWidgetId;
    }

    public void setWidgetId(int widgetId) {
        mWidgetId = widgetId;
    }

    public long getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(long createTime) {
        mCreateTime = createTime;
    }

    public long getModifyTime() {
        return mModifyTime;
    }

    public void setModifyTime(long modifyTime) {
        mModifyTime = modifyTime;
    }

    public long getAlertTime() {
        return mAlertTime;
    }

    public void setAlertTime(long alertTime) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mParentId);
        dest.writeInt(mType);
        dest.writeLong(mCreateTime);

        if (TYPE_NOTE == mType) {
            dest.writeLong(mModifyTime);
            dest.writeLong(mAlertTime);
            dest.writeInt(mColor);
            dest.writeString(mContent);
        } else {
            dest.writeString(mSubject);
        }

        dest.writeInt(mEnterDesktopFlag);
        dest.writeInt(mWidgetId);
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {

        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    private Note(Parcel source) {
        mId = source.readLong();
        mParentId = source.readLong();
        mType = source.readInt();
        mCreateTime = source.readLong();

        if (TYPE_NOTE == mType) {
            mModifyTime = source.readLong();
            mAlertTime = source.readLong();
            mColor = source.readInt();
            mContent = source.readString();
        } else {
            mSubject = source.readString();
        }

        mEnterDesktopFlag = source.readInt();
        mWidgetId = source.readInt();
    }

    public String toJSON() {
        JSONObject noteJSON = new JSONObject();

        try {
            noteJSON.put(NoteEntry._ID, mId);
            noteJSON.put(NoteEntry.COLUMN_NAME_TYPE, mType);
            noteJSON.put(NoteEntry.COLUMN_NAME_PARENT_ID, mParentId);
            noteJSON.put(NoteEntry.COLUMN_NAME_CREATE_TIME, mCreateTime);

            if (TYPE_NOTE == mType) {
                noteJSON.put(NoteEntry.COLUMN_NAME_COLOR, mColor);
                noteJSON.put(NoteEntry.COLUMN_NAME_MODIFY_TIME, mModifyTime);
                noteJSON.put(NoteEntry.COLUMN_NAME_ALERT_TIME, mAlertTime);
                noteJSON.put(NoteEntry.COLUMN_NAME_CONTENT, mContent);
            } else {
                noteJSON.put(NoteEntry.COLUMN_NAME_SUBJECT, mSubject);
            }

            noteJSON.put(NoteEntry.COLUMN_NAME_ENTER_DESKTOP_FLAG, mEnterDesktopFlag);
            noteJSON.put(NoteEntry.COLUMN_NAME_widget_ID, mWidgetId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return noteJSON.toString();
    }

    public static Note getFromJSON(String json) {

        Note result = new Note();

        try {
            JSONObject noteJSON = new JSONObject(json);
            result.setId(noteJSON.getLong(NoteEntry._ID));
            result.setType(noteJSON.getInt(NoteEntry.COLUMN_NAME_TYPE));
            result.setParentId(noteJSON.getLong(NoteEntry.COLUMN_NAME_PARENT_ID));
            result.setCreateTime(noteJSON.getLong(NoteEntry.COLUMN_NAME_CREATE_TIME));

            if (TYPE_NOTE == result.getType()) {
                result.setColor(noteJSON.getInt(NoteEntry.COLUMN_NAME_COLOR));
                result.setModifyTime(noteJSON.getLong(NoteEntry.COLUMN_NAME_MODIFY_TIME));
                result.setAlertTime(noteJSON.getLong(NoteEntry.COLUMN_NAME_ALERT_TIME));
                result.setContent(noteJSON.getString(NoteEntry.COLUMN_NAME_CONTENT));
            } else {
                result.setSubject(noteJSON.getString(NoteEntry.COLUMN_NAME_SUBJECT));
            }

            result.setWidgetId(noteJSON.getInt(NoteEntry.COLUMN_NAME_widget_ID));
            result.setEnterDesktopFlag(noteJSON.getInt(NoteEntry.COLUMN_NAME_ENTER_DESKTOP_FLAG));
        } catch (JSONException e) {
            e.printStackTrace();
            result = null;
        }

        return result;
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
