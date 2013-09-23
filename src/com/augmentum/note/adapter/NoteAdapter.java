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
import java.util.*;

public class NoteAdapter extends BaseAdapter {

    private Context mContext;
    private List<Note> mList;
    private Set<Note> mEditSet = new HashSet<Note>();
    private boolean mIsDeleteState;
    private boolean mIsMoveState;

    public NoteAdapter(Context context, List<Note> list) {
        mContext = context;
        mList = list;
    }

    public void setDeleteState(boolean isDeleteState) {
        mIsDeleteState = isDeleteState;
    }

    public void setMoveState(boolean isMoveState) {
        mIsMoveState = isMoveState;
    }

    public Set<Note> getEditSet() {
        return mEditSet;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_note_item, null);
            holder = new ViewHolder(convertView);

            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mIsDeleteState || mIsMoveState) {
                        if (isChecked) {
                            mEditSet.add(mList.get(position));
                        } else {
                            mEditSet.remove(mList.get(position));
                        }
                    }
                }
            });

            if (convertView != null) {
                convertView.setTag(holder);
            }

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (Note.TYPE_NOTE == mList.get(position).getType()) {

            switch (mList.get(position).getColor()) {
                case Color.YELLOW:
                    holder.mWrapperLayout.setBackgroundResource(R.drawable.note_item_bg_yellow_selector);
                    break;
                case Color.BLUE:
                    holder.mWrapperLayout.setBackgroundResource(R.drawable.note_item_bg_blue_selector);
                    break;
                case Color.RED:
                    holder.mWrapperLayout.setBackgroundResource(R.drawable.note_item_bg_pink_selector);
                    break;
                case Color.GREEN:
                    holder.mWrapperLayout.setBackgroundResource(R.drawable.note_item_bg_green_selector);
                case Color.GRAY:
                    holder.mWrapperLayout.setBackgroundResource(R.drawable.note_item_bg_grey_selector);
                    break;
            }

            setTimeText(position, holder);
            holder.mTitleTextView.setText(mList.get(position).getContent());
        } else {
            holder.mWrapperLayout.setBackgroundResource(R.drawable.folder_item_selector);

            if (0 < mList.get(position).getModifyTime()) {
                setTimeText(position, holder);
            } else {
                holder.mTimeTextView.setText(null);
            }

            holder.mTitleTextView.setText(mList.get(position).getSubject() +
                    " (" + mList.get(position).getChildCount() + ") ");
        }

        if (!isMatch(convertView)) {

            if (mIsDeleteState) {
                holder.mCheckBox.setVisibility(View.VISIBLE);
                holder.mTimeTextView.setVisibility(View.GONE);
            } else if (mIsMoveState) {

                if (Note.TYPE_NOTE == mList.get(position).getType()) {
                    holder.mCheckBox.setVisibility(View.VISIBLE);
                    holder.mTimeTextView.setVisibility(View.GONE);
                }

            } else {
                holder.mCheckBox.setVisibility(View.GONE);
                holder.mCheckBox.setChecked(false);
                holder.mTimeTextView.setVisibility(View.VISIBLE);
            }

        }

        if (0 < mList.get(position).getAlertTime()) {
            holder.mAlertImageView.setVisibility(View.VISIBLE);
        } else {
            holder.mAlertImageView.setVisibility(View.GONE);
        }

        if (position == mList.size() - 1 && Note.TYPE_NOTE == mList.get(position).getType()) {
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

        if (View.VISIBLE == convertView.findViewById(R.id.note_item_checkbox).getVisibility()
                && (mIsDeleteState || mIsMoveState)) {
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