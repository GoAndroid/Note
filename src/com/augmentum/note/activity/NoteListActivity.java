package com.augmentum.note.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.augmentum.note.R;
import com.augmentum.note.adapter.NoteAdapter;
import com.augmentum.note.dao.NoteDao;
import com.augmentum.note.dao.impl.NoteDaoImpl;
import com.augmentum.note.database.NoteDbHelper;
import com.augmentum.note.fragment.DeleteDialogFragment;
import com.augmentum.note.fragment.MoveDialogFragment;
import com.augmentum.note.fragment.SetPasswordDialogFragment;
import com.augmentum.note.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends FragmentActivity {

    public static final String PARENT_TAG = "Parent";
    public static final String NOTE_TAG = "note";
    private ListView mNoteListView;
    private LinearLayout mDeleteDialog;
    private LinearLayout mMoveDialog;
    private RelativeLayout mAddFolderDialog;
    private NoteAdapter mNoteAdapter;
    private List<Note> mList = new ArrayList<Note>();
    private NoteDbHelper mDbHelper;
    private EditText mFolderDialogText;
    private NoteDao mNoteDao;
    private boolean mIsFolderState;
    private Note mParent;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.note_list);
        mDbHelper = new NoteDbHelper(this);
        mNoteDao = NoteDaoImpl.getInstance();

        initView();

        Intent intent = getIntent();
        mParent = (Note) intent.getSerializableExtra(PARENT_TAG);

        if (null != mParent) {
            mIsFolderState = true;
            TextView headerTextView = (TextView) findViewById(R.id.note_list_title);
            headerTextView.setText(mParent.getSubject());
        }

    }

    private void initView() {
        mNoteListView = (ListView) findViewById(R.id.note_list_note_list);
        mDeleteDialog = (LinearLayout) findViewById(R.id.note_list_delete_dialog);
        mMoveDialog = (LinearLayout) findViewById(R.id.note_list_move_dialog);
        mAddFolderDialog = (RelativeLayout) findViewById(R.id.note_list_add_folder_dialog);
        mFolderDialogText = (EditText) findViewById(R.id.note_list_folder_dialog_text);

        mNoteAdapter = new NoteAdapter(NoteListActivity.this, mList);
        mNoteListView.setAdapter(mNoteAdapter);
        mNoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Note.TYPE_NOTE == mList.get(position).getType()) {
                    Intent intent = new Intent();
                    intent.setClass(NoteListActivity.this, NoteEditActivity.class);
                    intent.putExtra(NOTE_TAG, mList.get(position));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(NoteListActivity.this, NoteListActivity.class);
                    intent.putExtra(PARENT_TAG, mList.get(position));
                    startActivity(intent);
                }
            }
        });

        mFolderDialogText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mFolderDialogText.getWindowToken(), 0);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        initList();
        mNoteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        if (mIsFolderState) {
            getMenuInflater().inflate(R.menu.note_folder_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.note_list_menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();

        switch (item.getItemId()) {
            case R.id.note_list_menu_add:
                intent.setClass(NoteListActivity.this, NoteEditActivity.class);
                startActivity(intent);
                return true;
            case R.id.note_list_menu_about:
                intent.setClass(NoteListActivity.this, NoteAboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.note_list_menu_backup_to_sd:
                return true;
            case R.id.note_list_menu_delete:
                mNoteAdapter.setDeleteState(true);
                mNoteAdapter.notifyDataSetChanged();
                mDeleteDialog.setVisibility(View.VISIBLE);
                return true;
            case R.id.note_list_menu_export_txt_file:
                return true;
            case R.id.note_list_menu_get_more:
                return true;
            case R.id.note_list_menu_move_into_folder:
                mNoteAdapter.setMoveState(true);
                mNoteAdapter.notifyDataSetChanged();
                mMoveDialog.setVisibility(View.VISIBLE);
                return true;
            case R.id.note_list_menu_new_folder:
                mAddFolderDialog.setVisibility(View.VISIBLE);
                mFolderDialogText.requestFocus();
                return true;
            case R.id.note_list_menu_restore_from_sd:
                return true;
            case R.id.note_list_menu_set_password:
                DialogFragment dialog = new SetPasswordDialogFragment();
                dialog.show(getSupportFragmentManager(), "setPasswordDialog");
                return true;
            case R.id.note_folder_menu_new_note:
                return true;
            case R.id.note_folder_menu_edit_folder_title:
                return true;
            case R.id.note_folder_menu_move_out_of_folder:
                mNoteAdapter.setMoveState(true);
                mNoteAdapter.notifyDataSetChanged();
                Button moveOkBtn = (Button) mMoveDialog.findViewById(R.id.note_list_move_ok_btn);
                moveOkBtn.setText(R.string.note_folder_menu_move_out_of_folder);
                mMoveDialog.setVisibility(View.VISIBLE);
                return true;
            case R.id.note_folder_menu_delete:
                mNoteAdapter.setDeleteState(true);
                mNoteAdapter.notifyDataSetChanged();
                mDeleteDialog.setVisibility(View.VISIBLE);
                return true;
            case R.id.note_folder_menu_add_shortcut:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onAddNote(View view) {
        Intent intent = new Intent();
        intent.setClass(NoteListActivity.this, NoteEditActivity.class);
        intent.putExtra(PARENT_TAG, mParent);
        startActivity(intent);
    }

    public void onDeleteOk(View view) {
        boolean hasNoEmptyFolder = false;

        for (Note note : mNoteAdapter.getEditSet()) {
            if (Note.TYPE_FOLDER == note.getType() && 0 < note.getChildCount()) {
                hasNoEmptyFolder = true;
                DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
                deleteDialogFragment.setMessage(R.string.note_edit_delete_confirm_hasNoEmptyFolder);
                deleteDialogFragment.setListener(new DeleteDialogFragment.OnDeleteListener() {
                    @Override
                    public void onPositiveClick() {
                        deleteNote();
                    }
                });
                deleteDialogFragment.show(getSupportFragmentManager(), "deleteDialogFragment");
            }
        }

        if (!hasNoEmptyFolder) {
            deleteNote();
        }
    }

    private void deleteNote() {

        for (Note note : mNoteAdapter.getEditSet()) {
            mNoteDao.delete(mDbHelper, note);
        }

        mNoteAdapter.getEditSet().clear();
        initList();
        mNoteAdapter.setDeleteState(false);
        mNoteAdapter.notifyDataSetChanged();
        mDeleteDialog.setVisibility(View.GONE);
    }

    public void onDeleteCancel(View view) {

        mNoteAdapter.setDeleteState(false);
        mNoteAdapter.notifyDataSetChanged();
        mDeleteDialog.setVisibility(View.GONE);
    }

    public void onMoveOk(View view) {

        if (!mIsFolderState) {
            MoveDialogFragment moveDialogFragment = new MoveDialogFragment();
            moveDialogFragment.setListener(new MoveDialogFragment.OnMoveListener() {

                @Override
                public void onPositiveClick(Note parent) {

                    for (Note note : mNoteAdapter.getEditSet()) {
                        note.setParentId(parent.getId());
                        mNoteDao.update(mDbHelper, note);
                    }

                    mNoteAdapter.getEditSet().clear();
                    initList();
                    mNoteAdapter.setMoveState(false);
                    mNoteAdapter.notifyDataSetChanged();
                    mMoveDialog.setVisibility(View.GONE);
                }
            });

            moveDialogFragment.show(getSupportFragmentManager(), "moveDialogFragment");
        } else {

            for (Note note : mNoteAdapter.getEditSet()) {
                note.setParentId(Note.NO_PARENT);
                mNoteDao.update(mDbHelper, note);
            }

            mNoteAdapter.getEditSet().clear();
            initList();
            mNoteAdapter.setMoveState(false);
            mNoteAdapter.notifyDataSetChanged();
            mMoveDialog.setVisibility(View.GONE);
        }

    }

    public void onMoveCancel(View view) {
        mNoteAdapter.setMoveState(false);
        mNoteAdapter.notifyDataSetChanged();
        mMoveDialog.setVisibility(View.GONE);
    }

    public void onAddFolderOk(View view) {
        Note note = new Note();
        note.setSubject(mFolderDialogText.getText().toString());
        note.setCreateTime(System.currentTimeMillis());
        note.setType(Note.TYPE_FOLDER);
        note.setParentId(Note.NO_PARENT);
        mNoteDao.insert(mDbHelper, note);
        initList();
        mNoteAdapter.notifyDataSetChanged();
        mAddFolderDialog.setVisibility(View.GONE);
    }

    public void onAddFolderCancel(View view) {
        mAddFolderDialog.setVisibility(View.GONE);
    }

    private void initList() {
        mList.clear();
        if (mIsFolderState) {
            mList.addAll(mNoteDao.getChildren(mDbHelper, mParent));
        } else {
            mList.addAll(mNoteDao.getALL(mDbHelper));
        }
    }
}
