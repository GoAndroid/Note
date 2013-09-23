package com.augmentum.note.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public interface OnDateListener {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth);
    }

    private OnDateListener mCallback;
    private Calendar mCalendar;

    private static final String CALENDAR = "calendar";

    public void setCalendar(Calendar calendar) {
        mCalendar = calendar;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreateDialog(savedInstanceState);

        if (null != savedInstanceState) {
            mCalendar = (Calendar) savedInstanceState.getSerializable(CALENDAR);
        }

        if (null == mCalendar) {
            mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(System.currentTimeMillis());
        }

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCallback.onDateSet(view, year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mCallback = (OnDateListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DatePickerDialogFragment.OnDateListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CALENDAR, mCalendar);
        super.onSaveInstanceState(outState);
    }
}
