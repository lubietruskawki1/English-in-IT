package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ConnectionHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "glossary";
    private static final int DB_VERSION = 1;
    Context context;

    public ConnectionHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        onCreate(this.getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("drop table if exists glossary"); // to będzie do wykasowania gdy już będzie gotowa baza
        String query = "create table if not exists glossary (term text, definition text)";
        db.execSQL(query);
        fillDatabase(db);
    }

    public void fillDatabase(SQLiteDatabase db) {
        InputStream inserts = this.context.getResources().openRawResource(R.raw.inserty_do_bazuchy);
        Scanner s = new Scanner(inserts).useDelimiter("\\A");
        String sql_inserts = s.hasNext() ? s.next() : "";

        String[] single_inserts = sql_inserts.split(";");
        //BufferedReader reader = new BufferedReader(new InputStreamReader(inserts));

        //sql_inserts = new Scanner(new File(path)).useDelimiter("\\Z").next();
        db.execSQL(sql_inserts);
        for (int i = 0; i < single_inserts.length - 1; i++) {
            db.execSQL(single_inserts[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists glossary");
        onCreate(db);
    }

    public ArrayList<String> getGlossary() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> glossary = new ArrayList<>();
        String selectQuery = "select * from glossary";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                glossary.add(cursor.getString(0) + ": " + cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return glossary;
    }

    public HashMap<String, String> getGlossaryMapTermToDef(int term_to_def) { // term -> definition
        SQLiteDatabase db = this.getReadableDatabase();

        HashMap<String, String> glossary = new HashMap<>();
        String selectQuery = "select * from glossary";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                glossary.put(cursor.getString(1-term_to_def), cursor.getString(term_to_def));
            } while (cursor.moveToNext());
        }

        return glossary;
    }

    public HashMap<String, String> getGlossaryMapDefToTerm() { // definition -> term
        return getGlossaryMapTermToDef(0);
    }

    public HashMap<String, String> getFilteredGlossaryMapTermToDef(int maxLength) { // term -> definition
        SQLiteDatabase db = this.getReadableDatabase();

        HashMap<String, String> glossary = new HashMap<>();
        String selectQuery = "select * from glossary";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String term = cursor.getString(0);
                String definition = cursor.getString(1);
                if (term.length() <= maxLength && definition.length() <= maxLength) {
                    glossary.put(term, definition);
                }
            } while (cursor.moveToNext());
        }

        return glossary;
    }
}