package com.augmentum.note.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private boolean mIsDelete;
    private NoteAdapter mNoteAdapter;
    private List<Note> mList = new ArrayList<Note>();
    private NoteDbHelper mDbHelper;
    private EditText mFolderDialogText;
    private NoteDao mNoteDao;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);

        mNoteListView = (ListView) findViewById(R.id.note_list_note_list);
        mDeleteDialog = (LinearLayout) findViewById(R.id.note_list_delete_dialog);
        mAddFolderDialog = (RelativeLayout) findViewById(R.id.note_list_add_folder_dialog);
        mFolderDialogText = (EditText) findViewById(R.id.note_list_folder_dialog_text);
        mDbHelper = new NoteDbHelper(this);
        mNoteDao = new NoteDaoImpl();

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
        getMenuInflater().inflate(R.menu.note_list_menu, menu);

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
                mIsDelete = true;
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
                return true;
            case R.id.note_list_menu_restore_from_sd:
                return true;
            case R.id.note_list_menu_set_password:
                DialogFragment dialog = new SetPasswordDialogFragment();
                dialog.show(getSupportFragmentManager(), "setPasswordDialog");
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
        mIsDelete = false;
        mNoteAdapter.notifyDataSetChanged();
        mDeleteDialog.setVisibility(View.GONE);
    }

    public void onDeleteCancel(View view) {
        mIsDelete = false;
        mNoteAdapter.notifyDataSetChanged();
        mDeleteDialog.setVisibility(View.GONE);
    }

    public void onAddFolderOk(View view) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Note.NoteEntry.COLUMN_NAME_SUBJECT, mFolderDialogText.getText().toString());
        values.put(Note.NoteEntry.COLUMN_NAME_CREATE_TIME, System.currentTimeMillis());
        mAddFolderDialog.setVisibility(View.GONE);
    }

    public void onAddFolderCancel(View view) {
        mAddFolderDialog.setVisibility(View.GONE);
    }


    @Override
    public boolean isEdit() {
        return mIsDelete;
    }

    private void initList() {

        mList.clear();
        mList.addAll(mNoteDao.query(mDbHelper));

    }
}
