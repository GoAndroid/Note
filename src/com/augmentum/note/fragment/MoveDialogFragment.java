package com.augmentum.note.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.augmentum.note.dao.impl.NoteDaoImpl;
import com.augmentum.note.database.NoteDbHelper;
import com.augmentum.note.model.Note;

import java.util.List;

public class MoveDialogFragment extends DialogFragment {

    public interface OnMoveListener {
        public void onItemClick(Note parent);
    }

    private OnMoveListener mListener;

    public void setListener(OnMoveListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final List<Note> folders = NoteDaoImpl.getInstance().getFolder(new NoteDbHelper(getActivity()));

        String[] folderSubjects = new String[folders.size()];

        for (int i = 0; i < folderSubjects.length; i++) {
            folderSubjects[i] = folders.get(i).getSubject();
        }

        builder.setItems(folderSubjects, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 mListener.onItemClick(folders.get(which));
            }
        });

        return builder.create();
    }
}
