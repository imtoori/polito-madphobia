package com.mad.delivery.biker_app;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import org.joda.time.DateTime;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private TimePickedListener listener;
    private DateTime oldDate;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        oldDate = (DateTime) getArguments().get("datetime");
        Log.d("MADAPP", "old date = " + oldDate.toString());
        int hour = oldDate.getHourOfDay();
        int minute = oldDate.getMinuteOfHour();
        listener = (TimePickedListener) getActivity();

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        listener.onTimePicked(i, i1);
    }

    public interface TimePickedListener {
        void onTimePicked(int i, int i1);
    }
}
