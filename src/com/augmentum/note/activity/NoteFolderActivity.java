package com.augmentum.note.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.augmentum.note.R;

public class NoteFolderActivity extends Activity {

    private LinearLayout mDeleteDialog;
    private RelativeLayout mModifyFolderDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_folder);
        mModifyFolderDialog = (RelativeLayout) findViewById(R.id.note_folder_modify_folder_dialog);
        mDeleteDialog = (LinearLayout) findViewById(R.id.note_folder_delete_dialog);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.note_folder_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent();

        switch (item.getItemId()) {
            case R.id.note_folder_menu_add_shortcut:
                return true;
            case R.id.note_folder_menu_delete:
                mDeleteDialog.setVisibility(View.VISIBLE);
                return true;
            case R.id.note_folder_menu_edit_folder_title:
                mModifyFolderDialog.setVisibility(View.VISIBLE);
                return true;
            case R.id.note_folder_menu_move_out_of_folder:
                return true;
            case R.id.note_folder_menu_new_note:
                intent.setClass(NoteFolderActivity.this, NoteEditActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onAddNote(View view) {
        Intent intent = new Intent();
        intent.setClass(NoteFolderActivity.this, NoteEditActivity.class);
        startActivity(intent);
    }
}