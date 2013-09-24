package com.augmentum.note.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import com.augmentum.note.R;
import com.augmentum.note.util.CalendarUtil;

import java.util.Calendar;

public class AlertTimeDialogFragment extends DialogFragment {

    public static final String DATE_PICKER_DIALOG_FRAGMENT = "datePickerDialogFragment";

    private OnNoteTimePickerListener mCallback;
    private Calendar mCalendar;
    private TimePicker mTimePicker;

    private static final String CALENDAR = "calendar";

    public interface OnNoteTimePickerListener {

        /**
         * when finish remind time set callback this method
         */
        public void onAlertSet(long alertTime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mCallback = (OnNoteTimePickerListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement AlertTimeDialogFragment.OnNoteTimePickerListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.note_edit_set_alert_time_time);
        View view = View.inflate(getActivity(), R.layout.note_time_picker_dialog, null);
        builder.setView(view);

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallback != null) {
                    mTimePicker.clearFocus();
                    mCalendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
                    mCalendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
                    mCallback.onAlertSet(mCalendar.getTimeInMillis());
                }
            }
        });

        mTimePicker = (TimePicker) view.findViewById(R.id.note_time_picker_dialog_timePicker);
        mTimePicker.setIs24HourView(DateFormat.is24HourFormat(getActivity()));

        if (null != savedInstanceState) {
            mCalendar = (Calendar) savedInstanceState.getSerializable(CALENDAR);
        }

        if (null == mCalendar) {
            mCalendar = CalendarUtil.getCurrent();
        }

        mTimePicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
        final Button datePickerBtn = (Button) view.findViewById(R.id.note_time_picker_dialog_show_datePicker_btn);

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment datePickerDialog = new DatePickerDialogFragment();
                datePickerDialog.setCalendar(mCalendar);

                datePickerDialog.setCallback(new DatePickerDialogFragment.OnDateListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mCalendar.set(year, monthOfYear, dayOfMonth);
                        datePickerBtn.setText(CalendarUtil.getYmdw( mCalendar.getTimeInMillis()));
                    }
                });

                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATE_PICKER_DIALOG_FRAGMENT);
            }
        });

        datePickerBtn.setText(CalendarUtil.getYmdw(mCalendar.getTimeInMillis()));

        return builder.create();
    }

    public void setCalendar(Calendar calendar) {
        mCalendar = calendar;
    }

}
