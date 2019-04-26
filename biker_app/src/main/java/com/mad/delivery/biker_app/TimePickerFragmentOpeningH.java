package com.mad.delivery.biker_app;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import org.joda.time.DateTime;

import java.util.Calendar;

import androidx.fragment.app.DialogFragment;

public class TimePickerFragmentOpeningH extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        private TimePickedOpeningHListener listener;
        private DateTime oldDate;
        private int day;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            oldDate = (DateTime) getArguments().get("datetime");
            day= (int)getArguments().get("day");
            Log.d("MADAPP", "old date = " + oldDate.toString());
            int hour = oldDate.getHourOfDay();
            int minute = oldDate.getMinuteOfHour();
            listener = (TimePickedOpeningHListener) getActivity();

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            listener.onTimePicked(i, i1, day);
        }

        public interface TimePickedOpeningHListener {
            void onTimePicked(int i, int i1, int day);
        }
    }

