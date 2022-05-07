package com.example.english_in_it;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Word {
    String word;
    String meaning;
    int good_answers;
    int bad_answers;
    Date when_to_remind;

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    void set_date_to_remind() {
        Calendar myCalendarInstance = new GregorianCalendar();
        Date today = new Date(myCalendarInstance.getTimeInMillis());
        int days_to_wait_until_next_remind = 2 * good_answers + bad_answers;
        when_to_remind = addDays(today, days_to_wait_until_next_remind);
    }
}
