package com.augmentum.note.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.augmentum.note.R;
import com.augmentum.note.dao.NoteDao;
import com.augmentum.note.dao.impl.NoteDaoImpl;
import com.augmentum.note.fragment.AlertTimeDialogFragment;
import com.augmentum.note.fragment.DeleteDialogFragment;
import com.augmentum.note.model.Note;
import com.augmentum.note.util.CalendarUtil;
import com.augmentum.note.util.Resource;

import java.util.Calendar;

public class NoteEditActivity extends FragmentActivity implements AlertTimeDialogFragment.OnNoteTimePickerListener {

    public static final String ALERT_DIALOG_FRAGMENT = "alertDialogFragment";

    private RadioGroup mChangeColorRadioGroup;
    private LinearLayout mChangeFontDialog;
    private Note mNote;
    private RelativeLayout mHeaderLayout;
    private ScrollView mScrollView;
    private EditText mEditText;
    private TextView mAlertTimeTextView;
    private ImageView mAlertImage;
    private SeekBar mChangeFontSeekBar;
    private NoteDao mNoteDao;
    private Note mParent;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        mNoteDao = NoteDaoImpl.getInstance();

        initView();
    }

    /**
     * get the instance of the view elements. and get the mNote from the intent, change the
     * background and editText text by the mNote.
     */
    private void initView() {
        mChangeColorRadioGroup = (RadioGroup) findViewById(R.id.note_edit_change_color_dialog);
        mChangeFontDialog = (LinearLayout) findViewById(R.id.note_edit_change_font_dialog);
        mHeaderLayout = (RelativeLayout) findViewById(R.id.note_edit_header);
        mScrollView = (ScrollView) findViewById(R.id.note_edit_scroll);
        mEditText = (EditText) findViewById(R.id.note_edit_content);
        mAlertTimeTextView = (TextView) findViewById(R.id.note_edit_header_alert_time);
        mAlertImage = (ImageView) findViewById(R.id.note_edit_header_alert_Image);
        mChangeFontSeekBar = (SeekBar) findViewById(R.id.note_edit_change_font_seek_bar);
        TextView modifyTimeTextView = (TextView) findViewById(R.id.note_edit_header_modify_time);

        Intent intent = getIntent();
        mParent = (Note) intent.getSerializableExtra(NoteListActivity.PARENT_TAG);
        mNote = (Note) intent.getSerializableExtra(NoteListActivity.NOTE_TAG);

        if (null == mNote) {
            mNote = new Note();
            mNote.setCreateTime(System.currentTimeMillis());
            mNote.setType(Note.TYPE_NOTE);
            mNote.setParentId(Note.NO_PARENT);
            mNote.setColor(Color.YELLOW);
            modifyTimeTextView.setText(CalendarUtil.getMdhm(System.currentTimeMillis()));
        } else {
            mEditText.setText(mNote.getContent());
            modifyTimeTextView.setText(CalendarUtil.getMdhm(mNote.getModifyTime()));

            switch (mNote.getColor()) {
                case Color.YELLOW:
                    mHeaderLayout.setBackgroundResource(R.drawable.notes_header_yellow);
                    mScrollView.setBackgroundResource(R.drawable.notes_bg_yellow);
                    mNote.setColor(Color.YELLOW);
                    break;
                case Color.BLUE:
                    mHeaderLayout.setBackgroundResource(R.drawable.notes_header_blue);
                    mScrollView.setBackgroundResource(R.drawable.notes_bg_blue);
                    mNote.setColor(Color.BLUE);
                    break;
                case Color.RED:
                    mHeaderLayout.setBackgroundResource(R.drawable.notes_header_pink);
                    mScrollView.setBackgroundResource(R.drawable.notes_bg_pink);
                    mNote.setColor(Color.RED);
                    break;
                case Color.GREEN:
                    mHeaderLayout.setBackgroundResource(R.drawable.notes_header_green);
                    mScrollView.setBackgroundResource(R.drawable.notes_bg_green);
                    mNote.setColor(Color.GREEN);
                    break;
                case Color.GRAY:
                    mHeaderLayout.setBackgroundResource(R.drawable.notes_header_gray);
                    mScrollView.setBackgroundResource(R.drawable.notes_bg_gray);
                    mNote.setColor(Color.GRAY);
                    break;
            }

        }

        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (!mEditText.hasFocus()) {
                    mEditText.requestFocus();
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, 0);

                mChangeColorRadioGroup.setVisibility(View.GONE);
                mChangeFontDialog.setVisibility(View.GONE);

                return false;
            }
        });

        mChangeColorRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.note_edit_yellow_radio_btn:
                        mHeaderLayout.setBackgroundResource(R.drawable.notes_header_yellow);
                        mScrollView.setBackgroundResource(R.drawable.notes_bg_yellow);
                        mNote.setColor(Color.YELLOW);
                        mChangeColorRadioGroup.setVisibility(View.GONE);
                        break;
                    case R.id.note_edit_blue_radio_btn:
                        mHeaderLayout.setBackgroundResource(R.drawable.notes_header_blue);
                        mScrollView.setBackgroundResource(R.drawable.notes_bg_blue);
                        mNote.setColor(Color.BLUE);
                        mChangeColorRadioGroup.setVisibility(View.GONE);
                        break;
                    case R.id.note_edit_pink_radio_btn:
                        mHeaderLayout.setBackgroundResource(R.drawable.notes_header_pink);
                        mScrollView.setBackgroundResource(R.drawable.notes_bg_pink);
                        mNote.setColor(Color.RED);
                        mChangeColorRadioGroup.setVisibility(View.GONE);
                        break;
                    case R.id.note_edit_green_radio_btn:
                        mHeaderLayout.setBackgroundResource(R.drawable.notes_header_green);
                        mScrollView.setBackgroundResource(R.drawable.notes_bg_green);
                        mNote.setColor(Color.GREEN);
                        mChangeColorRadioGroup.setVisibility(View.GONE);
                        break;
                    case R.id.note_edit_grey_radio_btn:
                        mHeaderLayout.setBackgroundResource(R.drawable.notes_header_gray);
                        mScrollView.setBackgroundResource(R.drawable.notes_bg_gray);
                        mNote.setColor(Color.GRAY);
                        mChangeColorRadioGroup.setVisibility(View.GONE);
                        break;
                }

            }
        });

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        float textSize = sharedPref.getFloat("fontSize", Resource.getSp(R.dimen.default_edit_text_font_size));
        mEditText.setTextSize(textSize);

        mChangeFontSeekBar.setMax(20);
        mChangeFontSeekBar.setProgress(Math.round(textSize - 12));
        mChangeFontSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mEditText.setTextSize(progress + 12);
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat("fontSize", mEditText.getTextSize());
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //To change body of implemented methods use File | Settings | File Templates.
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
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (menu.hasVisibleItems()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
        ;

        return super.onPrepareOptionsMenu(menu);
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
                DeleteDialogFragment deleteDialog = new DeleteDialogFragment();
                deleteDialog.setMessage(R.string.dialog_delete_confirm);

                deleteDialog.setListener(new DeleteDialogFragment.OnDeleteListener() {

                    @Override
                    public void onPositiveClick() {

                        if (0 < mNote.getId()) {
                            mNoteDao.delete(mNote);
                        }

                        finish();
                    }
                });

                deleteDialog.show(getSupportFragmentManager(), "deleteDialog");
                return true;
            case R.id.note_edit_menu_new_note:
                return true;
            case R.id.note_edit_menu_remind:
                Calendar alertCalendar = Calendar.getInstance();

                if (0 < mNote.getAlertTime()) {
                    alertCalendar.setTimeInMillis(mNote.getAlertTime());
                } else {
                    alertCalendar.setTimeInMillis(System.currentTimeMillis());
                }

                AlertTimeDialogFragment alertDialog = new AlertTimeDialogFragment();
                alertDialog.setCalendar(alertCalendar);
                alertDialog.show(getSupportFragmentManager(), ALERT_DIALOG_FRAGMENT);
                return true;
            case R.id.note_edit_menu_share:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if (null == mEditText.getText() || "".equals(mEditText.getText().toString())) {
            return;
        }

        mNote.setContent(mEditText.getText().toString());
        mNote.setModifyTime(System.currentTimeMillis());

        if (null != mParent) {
            mNote.setParentId(mParent.getId());
        }

        if (0 < mNote.getId()) {
            mNoteDao.update(mNote);
        } else {
            mNoteDao.insert(mNote);
        }

    }

    /**
     * the changeColor ImageView click listener, when the change color RadioGroup is
     * visible change it to gone, when the change color RadioGroup is gone change it
     * to visible and change the checked button of the RadioGroup.
     *
     * @param view from system
     */
    public void onShowChangeColor(View view) {

        if (View.GONE == mChangeColorRadioGroup.getVisibility()) {

            switch (mNote.getColor()) {
                case Color.YELLOW:
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

            // Hidden the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            mChangeColorRadioGroup.setVisibility(View.VISIBLE);
        } else {
            mChangeColorRadioGroup.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAlertSet(long alertTime) {

        if (alertTime < System.currentTimeMillis()) {
            Toast.makeText(this, R.string.note_edit_use_alert_time_error, Toast.LENGTH_SHORT).show();
            return;
        }

        mNote.setAlertTime(alertTime);
        mAlertImage.setVisibility(View.VISIBLE);
        mAlertTimeTextView.setText(CalendarUtil.getMdhm(alertTime));
        mAlertTimeTextView.setVisibility(View.VISIBLE);
    }

}