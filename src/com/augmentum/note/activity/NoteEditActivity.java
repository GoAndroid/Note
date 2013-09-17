package com.augmentum.note.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.augmentum.note.R;
import com.augmentum.note.dao.NoteDao;
import com.augmentum.note.dao.impl.NoteDaoImpl;
import com.augmentum.note.database.NoteDbHelper;
import com.augmentum.note.fragment.DatePickerDialogFragment;
import com.augmentum.note.fragment.DeleteDiaogFragment;
import com.augmentum.note.fragment.RemindDialogFragment;
import com.augmentum.note.model.Note;

import java.util.Date;

public class NoteEditActivity extends FragmentActivity implements RemindDialogFragment.OnNoteTimePickerListener, DatePickerDialogFragment.OnDateListener {

    private RadioGroup mChangeColorRadioGroup;
    private LinearLayout mChangeFontDialog;
    private NoteDbHelper mDbHelper;
    private Note mNote;
    private RelativeLayout mHeaderLayout;
    private ScrollView mScrollView;
    private EditText mEditText;
    private NoteDao noteDao;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        mDbHelper = new NoteDbHelper(this);
        noteDao = new NoteDaoImpl();

        initView();

    }

    private void initView() {
        mChangeColorRadioGroup = (RadioGroup) findViewById(R.id.note_edit_change_color_dialog);
        mChangeFontDialog = (LinearLayout) findViewById(R.id.note_edit_change_font_dialog);
        mHeaderLayout = (RelativeLayout) findViewById(R.id.note_edit_header);
        mScrollView = (ScrollView) findViewById(R.id.note_edit_scroll);
        mEditText = (EditText) findViewById(R.id.note_edit_content);

        Intent intent = getIntent();
        mNote = (Note) intent.getSerializableExtra("note");

        if (null == mNote) {
            mNote = new Note();
            mNote.setCreateTime(new Date());
            mNote.setType(Note.TYPE_NOTE);
            mNote.setParentId(Note.NO_PARENT);
            mNote.setColor(Color.YELLOW);
        } else {
            mEditText.setText(mNote.getContent());

            switch (mNote.getColor()) {
                case Color.YELLOW:
                    mHeaderLayout.setBackground(getResources().getDrawable(R.drawable.notes_header_yellow));
                    mScrollView.setBackground(getResources().getDrawable(R.drawable.notes_bg_yellow));
                    mNote.setColor(Color.YELLOW);
                    break;
                case Color.BLUE:
                    mHeaderLayout.setBackground(getResources().getDrawable(R.drawable.notes_header_blue));
                    mScrollView.setBackground(getResources().getDrawable(R.drawable.notes_bg_blue));
                    mNote.setColor(Color.BLUE);
                    break;
                case Color.RED:
                    mHeaderLayout.setBackground(getResources().getDrawable(R.drawable.notes_header_pink));
                    mScrollView.setBackground(getResources().getDrawable(R.drawable.notes_bg_pink));
                    mNote.setColor(Color.RED);
                    break;
                case Color.GREEN:
                    mHeaderLayout.setBackground(getResources().getDrawable(R.drawable.notes_header_green));
                    mScrollView.setBackground(getResources().getDrawable(R.drawable.notes_bg_green));
                    mNote.setColor(Color.GREEN);
                    break;
                case Color.GRAY:
                    mHeaderLayout.setBackground(getResources().getDrawable(R.drawable.notes_header_gray));
                    mScrollView.setBackground(getResources().getDrawable(R.drawable.notes_bg_gray));
                    mNote.setColor(Color.GRAY);
                    break;
            }
        }

        mChangeColorRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.note_edit_yellow_radio_btn:
                        mHeaderLayout.setBackground(getResources().getDrawable(R.drawable.notes_header_yellow));
                        mScrollView.setBackground(getResources().getDrawable(R.drawable.notes_bg_yellow));
                        mNote.setColor(Color.YELLOW);
                        break;
                    case R.id.note_edit_blue_radio_btn:
                        mHeaderLayout.setBackground(getResources().getDrawable(R.drawable.notes_header_blue));
                        mScrollView.setBackground(getResources().getDrawable(R.drawable.notes_bg_blue));
                        mNote.setColor(Color.BLUE);
                        break;
                    case R.id.note_edit_pink_radio_btn:
                        mHeaderLayout.setBackground(getResources().getDrawable(R.drawable.notes_header_pink));
                        mScrollView.setBackground(getResources().getDrawable(R.drawable.notes_bg_pink));
                        mNote.setColor(Color.RED);
                        break;
                    case R.id.note_edit_green_radio_btn:
                        mHeaderLayout.setBackground(getResources().getDrawable(R.drawable.notes_header_green));
                        mScrollView.setBackground(getResources().getDrawable(R.drawable.notes_bg_green));
                        mNote.setColor(Color.GREEN);
                        break;
                    case R.id.note_edit_grey_radio_btn:
                        mHeaderLayout.setBackground(getResources().getDrawable(R.drawable.notes_header_gray));
                        mScrollView.setBackground(getResources().getDrawable(R.drawable.notes_bg_gray));
                        mNote.setColor(Color.GRAY);
                        break;
                }
            }
        });
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
        super.onBackPressed();

        mNote.setContent(mEditText.getText().toString());

        if (0 < mNote.getId()) {
            noteDao.update(mDbHelper, mNote);
        }  else {
            noteDao.insert(mDbHelper, mNote);
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    public void onShowChangeColor(View view) {
        if (View.GONE == mChangeColorRadioGroup.getVisibility()) {
            switch (mNote.getColor()) {
                case Color.YELLOW :
                    mChangeColorRadioGroup.check(R.id.note_edit_yellow_radio_btn);
                    break;
                case Color.BLUE:
                    mChangeColorRadioGroup.check(R.id.note_edit_blue_radio_btn);
                    break;
                case Color.RED:
                    mChangeColorRadioGroup.check(R.id.note_edit_pink_radio_btn);
                    break;
                case Color.GREEN:
                    mChangeColorRadioGroup.check(R.id.note_edit_green_radio_btn);
                    break;
                case Color.GRAY:
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