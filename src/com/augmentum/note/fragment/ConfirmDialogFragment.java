package com.augmentum.note.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.augmentum.note.util.Resource;

public class ConfirmDialogFragment extends DialogFragment {

    public static final String TAG = "confirmDialogFragment";

    public interface OnPositiveClickListener {
        public void onClick();
    }

    public interface OnNegativeClickListener {
        public void onClick();
    }

    private String mMessage;
    private String mTitle;
    private String mPositiveMessage = Resource.getString(android.R.string.ok);
    private String mNegativeMessage = Resource.getString(android.R.string.cancel);
    private OnPositiveClickListener mPositiveClickListener;
    private OnNegativeClickListener mNegativeClickListener;

    public void setMessage(int resId) {
        mMessage = Resource.getString(resId);
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public void setTitle(int resId) {
        mTitle = Resource.getString(resId);
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setPositiveMessage(int resId) {
        mPositiveMessage = Resource.getString(resId);
    }

    public void setNegativeMessage(int resId) {
        mNegativeMessage = Resource.getString(resId);
    }

    public void setPositiveClickListener(OnPositiveClickListener positiveClickListener) {
        mPositiveClickListener = positiveClickListener;
    }

    public void setNegativeClickListener(OnNegativeClickListener negativeClickListener) {
        mNegativeClickListener = negativeClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (null != mMessage) {
            builder.setMessage(mMessage);
        }

        if (null != mTitle) {
            builder.setTitle(mTitle);
        }

        builder.setPositiveButton(mPositiveMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (null != mNegativeClickListener) {
                    mPositiveClickListener.onClick();
                }

            }
        });

        builder.setNegativeButton(mNegativeMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (null != mNegativeClickListener) {
                    mNegativeClickListener.onClick();
                }

            }
        });

        return builder.create();
    }
}
