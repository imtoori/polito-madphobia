package com.mad.delivery.restaurant_app;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDateFormat extends SimpleDateFormat {

    public MyDateFormat(String format) {
        super(format);
    }

    public String parse(Date date) {
        String dateString;
        Calendar c1 = Calendar.getInstance(); // today
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DAY_OF_YEAR, -1); // yesterday
        Calendar c3 = Calendar.getInstance();
        c3.setTime(date); // my date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        if(c1.get(Calendar.YEAR) == c3.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c3.get(Calendar.DAY_OF_YEAR)) {
            // returns Today, hh:ss
            dateString = "Today, " + simpleDateFormat.format(date);
        } else if(c2.get(Calendar.YEAR) == c3.get(Calendar.YEAR) && c2.get(Calendar.DAY_OF_YEAR) == c3.get(Calendar.DAY_OF_YEAR)) {
            dateString = "Yesterday, " + simpleDateFormat.format(date);
        } else {
            dateString = super.format(date);
        }
        return dateString;

    }
}
