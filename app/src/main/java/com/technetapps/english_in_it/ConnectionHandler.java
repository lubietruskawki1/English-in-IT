package com.technetapps.english_in_it;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.technetapps.english_in_it.R;

import learning_sets.Set;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

/** Class creates database and handles connection.
 * */


public class ConnectionHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "glossary";
    private static final int DB_VERSION = 1;
    Context context;
    public ConnectionHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_gloss = "create table if not exists glossary (id integer, term text unique, definition text unique, days_waited_prev integer, repetition_date text)";
        String query_sets_contents = "create table if not exists learning_sets_contents (term text, set_name text)";
        String query_sets = "create table if not exists learning_sets (set_name text)";

        db.execSQL(query_gloss);
        db.execSQL(query_sets_contents);
        db.execSQL(query_sets);

        fillDatabase(db);
    }

    public void fillDatabase(SQLiteDatabase db) {
        InputStream inserts = this.context.getResources().openRawResource(R.raw.nowe_inserty);
        Scanner s = new Scanner(inserts).useDelimiter("\\A");
        String sql_inserts = s.hasNext() ? s.next() : "";

        String[] single_inserts = sql_inserts.split(";");
        //BufferedReader reader = new BufferedReader(new InputStreamReader(inserts));

        //sql_inserts = new Scanner(new File(path)).useDelimiter("\\Z").next();
        db.execSQL(sql_inserts);
        for (int i = 1; i < single_inserts.length - 1; i++) {
            db.execSQL(single_inserts[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists glossary");
        onCreate(db);
    }
}