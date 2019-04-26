package com.mad.delivery.bikerApp.orders;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class MyDateFormat {

    public MyDateFormat() {

    }

    public static String parse(DateTime date) {
        String dateString;
        DateTime today = new DateTime();
        DateTime yesterday = today.minus(Period.days(1));
        DateTimeFormatter partial = DateTimeFormat.forPattern("HH:mm");
        DateTimeFormatter complete = DateTimeFormat.forPattern("dd/MM/yy HH:mm");

        if(date.getDayOfYear() == today.getDayOfYear() && date.getYear() == today.getYear()) return dateString = "Today, " + partial.print(date);
        else if(date.getDayOfYear() == yesterday.getDayOfYear() && date.getYear() == yesterday.getYear()) return dateString = "Yesterday, " + partial.print(date);
        else dateString = complete.print(date);
        return dateString;

    }
}
