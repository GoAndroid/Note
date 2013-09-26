package com.augmentum.note.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.augmentum.note.R;

public class ExportDialogFragment extends DialogFragment {

    public static final String TAG_SD = "exportSdFragment";
    public static final String TAG_TXT = "exportTxtFragment";

    public interface OnExportListener {
        public void onItemClick(int which);
    }

    private OnExportListener mListener;

    public void setListener(OnExportListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(R.array.note_list_export, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onItemClick(which);
            }
        });

        return builder.create();
    }
}
