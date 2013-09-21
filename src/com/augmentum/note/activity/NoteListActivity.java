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
import com.augmentum.note.fragment.SetPasswordDialogFragment;
import com.augmentum.note.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends FragmentActivity implements NoteAdapter.OnDeleteListener {

    private ListView mNoteListView;
    private LinearLayout mDeleteDialog;
    private RelativeLayout mAddFolderDialog;
    private boolean mIsEditState;
    private NoteAdapter mNoteAdapter;
    private List<Note> mList = new ArrayList<Note>();
    private NoteDbHelper mDbHelper;
    private EditText mFolderDialogText;
    private NoteDao mNoteDao;
    private boolean mIsFolderState;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.note_list);
        mDbHelper = new NoteDbHelper(this);
        mNoteDao = NoteDaoImpl.getInstance();
        Intent intent = getIntent();
        Note parent = (Note) intent.getSerializableExtra("parent");

        if (null != parent) {
            mIsFolderState = true;
        }

        initView();

    }

    private void initView() {
        mNoteListView = (ListView) findViewById(R.id.note_list_note_list);
        mDeleteDialog = (LinearLayout) findViewById(R.id.note_list_delete_dialog);
        mAddFolderDialog = (RelativeLayout) findViewById(R.id.note_list_add_folder_dialog);
        mFolderDialogText = (EditText) findViewById(R.id.note_list_folder_dialog_text);

        mNoteAdapter = new NoteAdapter(NoteListActivity.this, NoteListActivity.this, mList);
        mNoteListView.setAdapter(mNoteAdapter);
        mNoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Note.TYPE_NOTE == mList.get(position).getType()) {
                    Intent intent = new Intent();
                    intent.setClass(NoteListActivity.this, NoteEditActivity.class);
                    intent.putExtra("note", mList.get(position));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(NoteListActivity.this, NoteListActivity.class);
                    intent.putExtra("parent", mList.get(position));
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
                mIsEditState = true;
                mNoteAdapter.notifyDataSetChanged();
                mDeleteDialog.setVisibility(View.VISIBLE);
                return true;
            case R.id.note_list_menu_export_txt_file:
                return true;
            case R.id.note_list_menu_get_more:
                return true;
            case R.id.note_list_menu_move_into_folder:
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
                return true;
            case R.id.note_folder_menu_delete:
                mIsEditState = true;
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
        startActivity(intent);
    }

    public void onDeleteOk(View view) {
        mIsEditState = false;
        mNoteAdapter.notifyDataSetChanged();
        mDeleteDialog.setVisibility(View.GONE);
    }

    public void onDeleteCancel(View view) {
        mIsEditState = false;
        mNoteAdapter.notifyDataSetChanged();
        mDeleteDialog.setVisibility(View.GONE);
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


    @Override
    public boolean isEdit() {
        return mIsEditState;
    }

    private void initList() {
        mList.clear();
        mList.addAll(mNoteDao.getALL(mDbHelper));
    }
}
