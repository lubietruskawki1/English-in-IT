package com.example.english_in_it;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Word {
    String word;
    String meaning;
    int days_we_waited_previously;
    Date when_to_remind;

    public Word(String word, String meaning, int days_we_waited_previously, String when_to_remind) throws ParseException {
        this.word = word;
        this.meaning = meaning;
        this.days_we_waited_previously = days_we_waited_previously;

        if (when_to_remind.equals("0/0/0")) this.when_to_remind = null;
        else {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            this.when_to_remind = formatter.parse(when_to_remind);
        }
    }

    public static Date addDays(Date date, int days) {
        System.out.println("data:" + date);
        System.out.println("lizcba dni do dodania: " + days);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        System.out.println("otrzymana data: " + cal.getTime());
        return cal.getTime();
    }

    void set_date_to_remind(boolean correct_answer) {
        //Calendar myCalendarInstance = new GregorianCalendar();
        //Date today = new Date(myCalendarInstance.getTimeInMillis());
        Date today = new Date();
        int days_to_wait_until_next_remind = 1;
        if(correct_answer) {
            days_to_wait_until_next_remind = 2 * days_we_waited_previously;
            days_we_waited_previously *= 2;
        }
        else {
            //days_to_wait_until_next_remind zostaje 1
            days_we_waited_previously = 1;
        }
        when_to_remind = addDays(today, days_to_wait_until_next_remind);
        System.out.println("data na koniec funkcji: " + when_to_remind);
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }
}
