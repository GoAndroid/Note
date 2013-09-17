package com.augmentum.note.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.augmentum.note.R;
import com.augmentum.note.database.NoteDbHelper;
import com.augmentum.note.fragment.DatePickerDialogFragment;
import com.augmentum.note.fragment.DeleteDiaogFragment;
import com.augmentum.note.fragment.RemindDialogFragment;
import com.augmentum.note.model.Note;

import java.util.Calendar;

public class NoteEditActivity extends FragmentActivity implements RemindDialogFragment.OnNoteTimePickerListener, DatePickerDialogFragment.OnDateListener {

    private RadioGroup mChangeColorRadioGroup;
    private LinearLayout mChangeFontDialog;
    private NoteDbHelper mDbHelper;
    private Note mNote;
    private RelativeLayout mHeaderLayout;
    private ScrollView mScrollView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);

        mChangeColorRadioGroup = (RadioGroup) findViewById(R.id.note_edit_change_color_dialog);
        mChangeFontDialog = (LinearLayout) findViewById(R.id.note_edit_change_font_dialog);
        mHeaderLayout = (RelativeLayout) findViewById(R.id.note_edit_header);
        mScrollView = (ScrollView) findViewById(R.id.note_edit_scroll);

        mNote = new Note();
        mNote.setCreateTime(Calendar.getInstance());
        mNote.setType(Note.TYPE_NOTE);
        mNote.setParentId(Note.NO_PARENT);
        mNote.setColor(R.drawable.notes_bg_yellow);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.note_edit_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.note_edit_menu_add_shortcut:
                return true;
            case R.id.note_edit_menu_change_font_size:
                mChangeFontDialog.setVisibility(View.VISIBLE);
                return true;
            case R.id.note_edit_menu_checklist:
                return true;
            case R.id.note_edit_menu_delete:
                DialogFragment deleteDialog = new DeleteDiaogFragment();
                deleteDialog.show(getSupportFragmentManager(), "deleteDialog");
                return true;
            case R.id.note_edit_menu_new_note:
                return true;
            case R.id.note_edit_menu_remind:
                DialogFragment remindDialog = new RemindDialogFragment();
                remindDialog.show(getSupportFragmentManager(), "remindDialog");
                return true;
            case R.id.note_edit_menu_share:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        SQLiteDatabase db =  mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Note.NoteEntry.COLUMN_NAME_TYPE, mNote.getType());
        values.put(Note.NoteEntry.COLUMN_NAME_PARENT_ID, mNote.getParentId());
        values.put(Note.NoteEntry.COLUMN_NAME_COLOR, mNote.getColor());
        values.put(Note.NoteEntry.COLUMN_NAME_CREATE_TIME, mNote.getCreateTime().getTimeInMillis());
        mNote.setModifyTime(Calendar.getInstance());
        values.put(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME, mNote.getModifyTime().getTimeInMillis());

        long newRowId = db.insert(Note.NoteEntry.TABLE_NAME, null, values);

        super.onBackPressed();
    }

    public void onShowChangeColor(View view) {
        if (View.GONE == mChangeColorRadioGroup.getVisibility()) {
            switch (mNote.getColor()) {
                case R.drawable.notes_bg_yellow :
                    mChangeColorRadioGroup.check(R.id.note_edit_yellow_radio_btn);
                    break;
                case R.drawable.notes_bg_blue:
                    mChangeColorRadioGroup.check(R.id.note_edit_blue_radio_btn);
                    break;
                case R.drawable.notes_bg_pink:
                    mChangeColorRadioGroup.check(R.id.note_edit_pink_radio_btn);
                    break;
                case R.drawable.notes_bg_green:
                    mChangeColorRadioGroup.check(R.id.note_edit_green_radio_btn);
                    break;
                case R.drawable.notes_bg_gray:
                    mChangeColorRadioGroup.check(R.id.note_edit_grey_radio_btn);
            }
            mChangeColorRadioGroup.setVisibility(View.VISIBLE);

        } else {
            mChangeColorRadioGroup.setVisibility(View.GONE);
        }
    }

    public void onScrollView(View view) {
        onShowChangeColor(view);
        if (View.GONE == mChangeFontDialog.getVisibility()) {
            mChangeFontDialog.setVisibility(View.VISIBLE);
        } else {
            mChangeFontDialog.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowDatePicker() {
        RemindDialogFragment remindDialog = (RemindDialogFragment) getSupportFragmentManager().findFragmentByTag("remindDialog");
        DialogFragment datePickerDialog = new DatePickerDialogFragment(remindDialog.getCalendar());
        datePickerDialog .show(getSupportFragmentManager(), "datePickerDialog ");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        RemindDialogFragment remindDialog = (RemindDialogFragment) getSupportFragmentManager().findFragmentByTag("remindDialog");
        remindDialog.onDateSet(view, year, monthOfYear, dayOfMonth);
    }
}