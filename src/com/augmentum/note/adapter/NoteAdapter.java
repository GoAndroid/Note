package com.augmentum.note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.augmentum.note.R;
import com.augmentum.note.model.Note;

import java.util.List;

public class NoteAdapter extends BaseAdapter {

    public interface OnDeleteListener {
        public boolean isDelete();
    }

    private Context mContext;
    private OnDeleteListener mCallback;
    private List<Note> mList;

    public NoteAdapter(Context context, OnDeleteListener callback, List<Note> list) {
        mContext = context;
        mCallback = callback;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || !isMatch(convertView)) {
            if (Note.TYPE_NOTE == mList.get(position).getType()) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_note_item, null);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_folder_item, null);
            }

            if (mCallback.isDelete()) {
                convertView.findViewById(R.id.note_item_checkbox).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.note_item_create_time).setVisibility(View.GONE);
            } else {
                convertView.findViewById(R.id.note_item_checkbox).setVisibility(View.GONE);
                convertView.findViewById(R.id.note_item_create_time).setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

    private boolean isMatch(View convertView) {
        boolean result = false;
        if (View.VISIBLE == convertView.findViewById(R.id.note_item_checkbox).getVisibility() && mCallback.isDelete()) {
            result = true;
        }

        return result;
    }
}