package com.technetapps.english_in_it;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class represents word, is used to store words as objects.
 * Is contains methods which are used to calculate repetitions.
 */


public class Word implements Serializable {
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

    public static Date add_days_to_today(int days) {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    void set_date_to_remind(boolean correct_answer) {
        //Date today = new Date();
        int days_to_wait_until_next_remind = 1;
        if(correct_answer) {
            days_to_wait_until_next_remind = 2 * days_we_waited_previously;
            days_we_waited_previously *= 2;
        }
        else {
            //days_to_wait_until_next_remind zostaje 1
            days_we_waited_previously = 1;
        }
        when_to_remind = add_days_to_today(days_to_wait_until_next_remind);
        //when_to_remind = addDays(today, days_to_wait_until_next_remind);
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }
}
