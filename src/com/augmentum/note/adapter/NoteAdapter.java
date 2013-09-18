package com.augmentum.note.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.augmentum.note.R;
import com.augmentum.note.model.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NoteAdapter extends BaseAdapter {

    public interface OnDeleteListener {
        public boolean isEdit();
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_note_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (Note.TYPE_NOTE == mList.get(position).getType()) {

            switch (mList.get(position).getColor()) {
                case Color.YELLOW:
                    holder.mWrapperLayout.setBackground(mContext.getResources().getDrawable(R.drawable.note_item_bg_yellow_selector));
                    break;
                case Color.BLUE:
                    holder.mWrapperLayout.setBackground(mContext.getResources().getDrawable(R.drawable.note_item_bg_blue_selector));
                    break;
                case Color.RED:
                    holder.mWrapperLayout.setBackground(mContext.getResources().getDrawable(R.drawable.note_item_bg_pink_selector));
                    break;
                case Color.GREEN:
                    holder.mWrapperLayout.setBackground(mContext.getResources().getDrawable(R.drawable.note_item_bg_green_selector));
                case Color.GRAY:
                    holder.mWrapperLayout.setBackground(mContext.getResources().getDrawable(R.drawable.note_item_bg_grey_selector));
                    break;
            }

            setTimeText(position, holder);
            holder.mTitleTextView.setText(mList.get(position).getContent());
        } else {
            holder.mWrapperLayout.setBackground(mContext.getResources().getDrawable(R.drawable.folder_item_selector));

            if (0 != mList.get(position).getModifyTime()) {
                setTimeText(position,holder);
            }

            holder.mTitleTextView.setText(mList.get(position).getSubject());
        }

        if (!isMatch(convertView)) {

            if (mCallback.isEdit()) {
                holder.mCheckBox.setVisibility(View.VISIBLE);
                holder.mTimeTextView.setVisibility(View.GONE);
            } else {
                holder.mCheckBox.setVisibility(View.GONE);
                holder.mTimeTextView.setVisibility(View.VISIBLE);
            }

        }

        if (0 != mList.get(position).getAlertTime()) {
            holder.mAlertImageView.setVisibility(View.VISIBLE);
        } else {
            holder.mAlertImageView.setVisibility(View.GONE);
        }

        if (position == mList.size() - 1) {
            holder.mShadowImageView.setVisibility(View.VISIBLE);
        } else {
            holder.mShadowImageView.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void setTimeText(int position, ViewHolder holder) {
        SimpleDateFormat sdf;
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeInMillis(System.currentTimeMillis());
        Calendar modifyTime = Calendar.getInstance();
        modifyTime.setTimeInMillis(mList.get(position).getModifyTime());

        if (currentTime.get(Calendar.DATE) == modifyTime.get(Calendar.DATE)) {
            sdf = new SimpleDateFormat(mContext.getResources().getString(R.string.format_time_hm));
        } else {
            sdf = new SimpleDateFormat(mContext.getResources().getString(R.string.format_week));
        }

        holder.mTimeTextView.setText(sdf.format(new Date(mList.get(position).getModifyTime())));
    }

    private boolean isMatch(View convertView) {
        boolean result = false;

        if (View.VISIBLE == convertView.findViewById(R.id.note_item_checkbox).getVisibility() && mCallback.isEdit()) {
            result = true;
        }

        return result;
    }

    private class ViewHolder {
        public TextView mTitleTextView;
        public TextView mTimeTextView;
        public CheckBox mCheckBox;
        public ImageView mAlertImageView;
        public LinearLayout mWrapperLayout;
        public ImageView mShadowImageView;

        public ViewHolder(View convertView) {
            mTitleTextView = (TextView) convertView.findViewById(R.id.note_item_title);
            mTimeTextView = (TextView) convertView.findViewById(R.id.note_item_modify_Time);
            mCheckBox = (CheckBox) convertView.findViewById(R.id.note_item_checkbox);
            mAlertImageView = (ImageView) convertView.findViewById(R.id.note_item_alert);
            mWrapperLayout = (LinearLayout) convertView.findViewById(R.id.note_item_wrapper);
            mShadowImageView = (ImageView) convertView.findViewById(R.id.note_item_shadow);
        }
    }
}