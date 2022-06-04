package com.example.english_in_it;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Word implements Parcelable {
    String word;
    String meaning;
    int good_answers;
    int bad_answers;
    Date when_to_remind;

    protected Word(Parcel in) {
        word= in.readString();
        meaning = in.readString();
        good_answers = in.readInt();
        bad_answers = in.readInt();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
        dest.writeString(meaning);
        dest.writeInt(good_answers);
        dest.writeInt(bad_answers);
    }

    public Word(String word, String meaning, int good_answers, int bad_answers, Date when_to_remind) {
        this.word = word;
        this.meaning = meaning;
        this.good_answers = good_answers;
        this.bad_answers = bad_answers;
        this.when_to_remind = when_to_remind;
    }

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
