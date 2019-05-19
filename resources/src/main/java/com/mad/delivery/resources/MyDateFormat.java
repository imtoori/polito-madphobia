package com.mad.delivery.resources;

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
        DateTime threeDaysAgo = today.minus(Period.days(3));
        DateTime twoDaysAgo = today.minus(Period.days(2));
        DateTime yesterday = today.minus(Period.days(1));
        DateTime tomorrow = today.plus(Period.days(1));
        DateTime inTwoDays = today.plus(Period.days(2));
        DateTime inThreeDays = today.plus(Period.days(3));
        DateTimeFormatter partial = DateTimeFormat.forPattern("HH:mm");
        DateTimeFormatter complete = DateTimeFormat.forPattern("dd/MM/yy HH:mm");

        if(date.getDayOfYear() == threeDaysAgo.getDayOfYear() && date.getYear() == threeDaysAgo.getYear()) return dateString = "Three days ago, " + partial.print(date);
        else if(date.getDayOfYear() == twoDaysAgo.getDayOfYear() && date.getYear() == twoDaysAgo.getYear()) return dateString = "Two days ago, " + partial.print(date);
        else if(date.getDayOfYear() == today.getDayOfYear() && date.getYear() == today.getYear()) return dateString = "Today, " + partial.print(date);
        else if(date.getDayOfYear() == yesterday.getDayOfYear() && date.getYear() == yesterday.getYear()) return dateString = "Yesterday, " + partial.print(date);
        else if(date.getDayOfYear() == tomorrow.getDayOfYear() && date.getYear() == tomorrow.getYear()) return dateString = "Tomorrow, " + partial.print(date);
        else if(date.getDayOfYear() == inTwoDays.getDayOfYear() && date.getYear() == inTwoDays.getYear()) return dateString = "In two days, " + partial.print(date);
        else if(date.getDayOfYear() == inThreeDays.getDayOfYear() && date.getYear() == inThreeDays.getYear()) return dateString = "In three days, " + partial.print(date);
        else dateString = complete.print(date);
        return dateString;

    }
}
