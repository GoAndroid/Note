package com.augmentum.note.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.augmentum.note.NoteApplication;
import com.augmentum.note.R;
import com.augmentum.note.dao.NoteDao;
import com.augmentum.note.dao.impl.NoteDaoImpl;
import com.augmentum.note.fragment.AlertTimeDialogFragment;
import com.augmentum.note.fragment.ConfirmDialogFragment;
import com.augmentum.note.fragment.SelectDialogFragment;
import com.augmentum.note.model.Note;
import com.augmentum.note.receiver.AlarmReceiver;
import com.augmentum.note.util.CalendarUtil;
import com.augmentum.note.util.Resource;
import com.augmentum.note.widget.NoteWidget2x2;
import com.augmentum.note.widget.NoteWidget4x4;

import java.util.Calendar;

public class NoteEditActivity extends FragmentActivity implements AlertTimeDialogFragment.OnNoteTimePickerListener {

    public static final String SHARE_DIALOG_FRAGMENT = "shareDialogFragment";
    public static final String TAG = "NoteEditActivity";

    private RadioGroup mChangeColorRadioGroup;
    private LinearLayout mChangeFontDialog;
    private Note mNote;
    private RelativeLayout mHeaderLayout;
    private ScrollView mScrollView;
    private EditText mEditText;
    private TextView mAlertTimeTextView;
    private ImageView mAlertImage;
    private NoteDao mNoteDao;
    private Note mParent;
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    boolean hasAlarm;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        mNoteDao = NoteDaoImpl.getInstance();

        initView();

        appWidgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

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
        SeekBar changeFontSeekBar = (SeekBar) findViewById(R.id.note_edit_change_font_seek_bar);
        TextView modifyTimeTextView = (TextView) findViewById(R.id.note_edit_header_modify_time);

        Intent intent = getIntent();
        mParent = intent.getParcelableExtra(Note.PARENT_TAG);
        mNote = intent.getParcelableExtra(Note.NOTE_TAG);

        if (null == mNote) {
            String json = intent.getStringExtra(Note.NOTE_TAG);
            if (json != null) {
                mNote = Note.getFromJSON(json);
            }
        }

        if (null == mNote) {
            mNote = new Note();
            mNote.setCreateTime(System.currentTimeMillis());
            mNote.setType(Note.TYPE_NOTE);
            mNote.setParentId(Note.NO_PARENT);
            mNote.setColor(Color.YELLOW);
            modifyTimeTextView.setText(CalendarUtil.getFormatMdhm(System.currentTimeMillis()));
        } else {
            mEditText.setText(mNote.getContent());
            modifyTimeTextView.setText(CalendarUtil.getFormatMdhm(mNote.getModifyTime()));

            if (System.currentTimeMillis() < mNote.getAlertTime()) {
                hasAlarm = true;
                mAlertImage.setVisibility(View.VISIBLE);
                mAlertTimeTextView.setText(CalendarUtil.getFormatMdhm(mNote.getAlertTime()));
                mAlertTimeTextView.setVisibility(View.VISIBLE);
            }

            initEditTextBackground();
        }

        mScrollView.setOnTouchListener(new ScrollViewOnTouchListener());

        mChangeColorRadioGroup.setOnCheckedChangeListener(new ChangeColorOnCheckedChangeListener());

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        float textSize = sharedPref.getFloat("fontSize", Resource.getSp(R.dimen.default_edit_text_font_size));
        mEditText.setTextSize(textSize);

        changeFontSeekBar.setMax(20);
        changeFontSeekBar.setProgress(Math.round(textSize - 12));
        changeFontSeekBar.setOnSeekBarChangeListener(new ChangeFontOnSeekBarChangeListener());

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

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.note_edit_menu_add_shortcut:
                if (0 < mNote.getId()) {
                    Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
                    shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, mNote.getContent());
                    shortcutIntent.putExtra("duplicate", false);
                    Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.icon_one);
                    shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
                    Intent callIntent = new Intent(this, NoteListActivity.class);
                    callIntent.putExtra(Note.TAG, mNote.getId());
                    shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, callIntent);
                    sendBroadcast(shortcutIntent);
                }
                return true;
            case R.id.note_edit_menu_change_font_size:
                mChangeFontDialog.setVisibility(View.VISIBLE);
                return true;
            case R.id.note_edit_menu_checklist:
                return true;
            case R.id.note_edit_menu_delete:
                ConfirmDialogFragment deleteDialog = new ConfirmDialogFragment();
                deleteDialog.setMessage(R.string.dialog_delete_confirm);

                deleteDialog.setPositiveClickListener(new ConfirmDialogFragment.OnPositiveClickListener() {

                    @Override
                    public void onClick() {

                        if (0 < mNote.getId()) {
                            mNoteDao.delete(mNote);
                        }

                        finish();
                    }
                });

                deleteDialog.show(getSupportFragmentManager(), "deleteDialog");
                return true;
            case R.id.note_edit_menu_new_note:
                Intent intent = new Intent();
                intent.setClass(this, NoteEditActivity.class);
                startActivity(intent);
                return true;
            case R.id.note_edit_menu_remind:
                Calendar alertCalendar = Calendar.getInstance();

                if (System.currentTimeMillis() < mNote.getAlertTime()) {
                    alertCalendar.setTimeInMillis(mNote.getAlertTime());
                } else {
                    alertCalendar.setTimeInMillis(System.currentTimeMillis());
                }

                AlertTimeDialogFragment alertDialog = new AlertTimeDialogFragment();
                alertDialog.setCalendar(alertCalendar);
                alertDialog.show(getSupportFragmentManager(), AlertTimeDialogFragment.TAG);
                return true;
            case R.id.note_edit_menu_share:
                SelectDialogFragment selectDialogFragment = new SelectDialogFragment();
                String[] items = Resource.getStringArray(R.array.note_edit_share);
                selectDialogFragment.setItems(items);
                selectDialogFragment.setListener(new SelectDialogFragment.OnSelectListener() {
                    @Override
                    public void onItemClick(int which) {

                        if (null == mEditText.getText() || "".equals(mEditText.getText().toString())) {
                            return;
                        }

                        switch (which) {
                            case 0:
                                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
                                smsIntent.putExtra("sms_body", mNote.getContent());
                                startActivity(smsIntent);
                                break;
                            case 1:
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.putExtra(Intent.EXTRA_TEXT, mNote.getContent());
                                emailIntent.setType("plain/text");
                                startActivity(Intent.createChooser(emailIntent, "Your email client"));
                                break;
                            case 2:
                                break;
                        }
                    }
                });
                selectDialogFragment.show(getSupportFragmentManager(), SHARE_DIALOG_FRAGMENT);
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

        insertOrUpdateNote();

        if (hasAlarm) {
            Log.v("NoteEditActivity", "alertTime" + mNote.getAlertTime());
            Log.v("NoteEditActivity", "currentTime" + System.currentTimeMillis());
            initAlarm();
        } else {
            cancelAlarm();
        }
    }

    private void insertOrUpdateNote() {
        mNote.setContent(mEditText.getText().toString());
        mNote.setModifyTime(System.currentTimeMillis());

        if (null != mParent) {
            mNote.setParentId(mParent.getId());
        }

        if (0 < mNote.getId()) {
            mNoteDao.update(mNote);
        } else {
            long id = mNoteDao.insert(mNote);
            mNote.setId(id);
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

        hasAlarm = true;
        mNote.setAlertTime(alertTime);
        mAlertImage.setVisibility(View.VISIBLE);
        mAlertTimeTextView.setText(CalendarUtil.getFormatMdhm(alertTime));
        mAlertTimeTextView.setVisibility(View.VISIBLE);
    }

    private class ChangeColorOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

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
    }

    private class ScrollViewOnTouchListener implements View.OnTouchListener {
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
    }

    private class ChangeFontOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mEditText.setTextSize(progress + 12);
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat("fontSize", progress + 12);
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
    }

    private void initEditTextBackground() {
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

    private void initAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int alarmType = AlarmManager.RTC_WAKEUP;

        long alarmTime = mNote.getAlertTime();
        Calendar c = Calendar.getInstance();

        Intent alarmIntent = new Intent();
        alarmIntent.setAction(AlarmReceiver.ALARM_ACTION);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarmManager.set(alarmType, alarmTime, alarmPendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent();
        alarmIntent.setAction(AlarmReceiver.ALARM_ACTION);
        alarmIntent.setData(Uri.parse("note://id=" + mNote.getId()));
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarmManager.cancel(alarmPendingIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {

        if (null != mEditText.getText() && !"".equals(mEditText.getText().toString())) {

            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                mNote.setWidgetId(appWidgetId);
                insertOrUpdateNote();
                Log.v(TAG, "widgetId: " + mNote.getWidgetId());
                Log.v(TAG, "id" + mNote.getId());
                createWidget();
                updateWidget();
                finish();
            } else if (0 < mNote.getWidgetId()) {
                insertOrUpdateNote();
                updateWidget();
            }

        }

        super.onBackPressed();
    }

    private void createWidget() {
        Intent createIntent = new Intent();
        createIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, createIntent);
    }

    private void updateWidget() {
        Intent intent = null;

        if(NoteWidget2x2.TAG.equals(NoteApplication.sWidgetType)) {
            intent = new Intent(NoteWidget2x2.WIDGET_UPDATE);
        } else {
            intent = new Intent(NoteWidget4x4.WIDGET_UPDATE);
        }

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mNote.getWidgetId());
        sendBroadcast(intent);
    }

}