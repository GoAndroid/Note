package com.augmentum.note.fragment;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeleteDialogFragment extends DialogFragment {

    public interface OnDeleteListener {
        public void onPositiveClick();
    }

    private int mMessageId;
    private OnDeleteListener mListener;

    public int getMessageId() {
        return mMessageId;
    }

    public void setMessage(int messageId) {
        mMessageId = messageId;
    }

    public OnDeleteListener getListener() {
        return mListener;
    }

    public void setListener(OnDeleteListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getMessageId());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onPositiveClick();
            }
        });

        builder.setNegativeButton(R.string.cancel, null);

        return builder.create();
    }
}
