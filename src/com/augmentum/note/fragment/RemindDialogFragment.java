package com.augmentum.note.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;
import com.augmentum.note.R;
import com.augmentum.note.ui.NoteTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RemindDialogFragment extends DialogFragment implements NoteTimePickerDialog.OnTimeSetListener, DatePickerDialogFragment.OnDateListener {

    private NoteTimePickerDialog mTimeDialog;
    private OnNoteTimePickerListener mListener;
    private Calendar mCalendar;

    public interface OnNoteTimePickerListener {
        public void onShowDatePicker();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (OnNoteTimePickerListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mCalendar = Calendar.getInstance();
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);

        mTimeDialog = new NoteTimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

        String currentTimeFormat = getActivity().getResources().getString(R.string.format_date_ymdw);
        SimpleDateFormat sdf = new SimpleDateFormat(currentTimeFormat);
        mTimeDialog.setDatePickerBtnText(sdf.format(mCalendar.getTime()));

        return mTimeDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
    }

    @Override
    public void onShowDatePick() {
        mListener.onShowDatePicker();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(year, monthOfYear, dayOfMonth);
        String currentTimeFormat = getActivity().getResources().getString(R.string.format_date_ymdw);
        SimpleDateFormat sdf = new SimpleDateFormat(currentTimeFormat);
        mTimeDialog.setDatePickerBtnText(sdf.format(mCalendar.getTime()));
    }

    public Calendar getCalendar() {
        return mCalendar;
    }
}
