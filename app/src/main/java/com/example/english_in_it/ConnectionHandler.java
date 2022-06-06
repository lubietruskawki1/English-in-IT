package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import learning_sets.Set;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class ConnectionHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "glossary";
    private static final int DB_VERSION = 1;
    Context context;

    public ConnectionHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        //onCreate(this.getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("drop table if exists glossary"); // to będzie do wykasowania gdy już będzie gotowa baza
        db.execSQL("drop table if exists learning_sets_contents");
        db.execSQL("drop table if exists learning_sets");

        String query_gloss = "create table if not exists glossary (id integer, term text unique, definition text, days_waited_prev integer, repetition_date text)";
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

    public ArrayList<String> getGlossary() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> glossary = new ArrayList<>();
        String selectQuery = "select * from glossary";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                glossary.add(cursor.getString(1) + ": " + cursor.getString(2));
            } while (cursor.moveToNext());
        }
        return glossary;
    }

    public ArrayList<String> getGlossaryJustTerms() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> glossary = new ArrayList<>();
        String selectQuery = "select * from glossary";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                glossary.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return glossary;
    }

    public ArrayList<Word> getLearningSetList(String set_name) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Word> result = new ArrayList<>();

        String selectQuery = "select * from glossary join learning_sets_contents s on glossary.term = s.term where s.set_name = '" + set_name + "';";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                result.add(new Word(cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        return result;
    }

    public HashMap<String, String> getSetGlossaryMapTermToDef(String set_name, int term_to_def) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, String> result = new HashMap<>();

        String selectQuery = "select * from glossary join learning_sets_contents s on glossary.term = s.term where s.set_name = '" + set_name + "';";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                result.put(cursor.getString(1 - term_to_def + 1), cursor.getString(term_to_def + 1));
            } while (cursor.moveToNext());
        }
        return result;
    }

    public HashMap<String, String> getSetGlossaryMapDefToTerm(String set_name) throws ParseException { // definition -> term
        return getSetGlossaryMapTermToDef(set_name, 0);
    }


    /*
        public HashMap<String, String> getGlossaryMapTermToDef(int term_to_def) { // term -> definition
        SQLiteDatabase db = this.getReadableDatabase();

        HashMap<String, String> glossary = new HashMap<>();
        String selectQuery = "select * from glossary";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                glossary.put(cursor.getString(1-term_to_def + 1), cursor.getString(term_to_def + 1));
            } while (cursor.moveToNext());
        }
        return glossary;
    }

    public HashMap<String, String> getGlossaryMapDefToTerm() { // definition -> term
        return getGlossaryMapTermToDef(0);
    }

     */

    public void setWordDaysWaitedPrev(String word_term, int new_value) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query_update = "update glossary set days_waited_prev = '" + new_value + " where term = " + word_term + "';";
        db.execSQL(query_update);
    }

    public void setWordRepetitionDate(String word_term, Date new_value) {
        SQLiteDatabase db = this.getWritableDatabase();

        String new_date;
        if (new_value == null) new_date = new String("0/0/0");
        else {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            new_date = formatter.format(new_value);
        }

        String query_update = "update glossary set repetition_date = '" + new_date + "' where term = '" + word_term + "';";
        db.execSQL(query_update);
    }

    public void newLearningSet(String set_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "insert into learning_sets values('" + set_name + "');";
        db.execSQL(query);
    }

    public void deleteLearningSet(String set_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "delete from learning_sets where set_name = '" + set_name + "';";
        db.execSQL(query);

        query = "delete from learning_sets_contents where set_name = '" + set_name + "';";
        db.execSQL(query);
    }

    public void addWordToLearningSet(String word, String set_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insert_query = "insert into learning_sets_contents values('" + word + "','" + set_name + "');";
        db.execSQL(insert_query);
    }

    public void deleteWordFromLearningSet(String word, String set_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete_query = "delete from learning_sets_contents where set_name = '" + set_name + "' and term ='" + word + "';";
        db.execSQL(delete_query);
    }

    public ArrayList<String> getAllLearningSetNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> result = new ArrayList<>();

        String selectQuery = "select * from learning_sets;";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return result;
    }

    public ArrayList<String> getEmptySets() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> result = new ArrayList<>();

        String selectQuery = "select * from learning_sets where set_name not in (select set_name from learning_sets_contents);";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return result;
    }

    public ArrayList<Set> getAllLearningSets() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Set> result = new ArrayList<>();

        String countQuery = "select set_name, count(term) from learning_sets_contents " +
                            "group by set_name;";

        @SuppressLint("Recycle") Cursor count_cursor = db.rawQuery(countQuery, null);
        if (count_cursor.moveToFirst()) {
            do {
                result.add(new Set(count_cursor.getString(0), count_cursor.getInt(1)));
            } while (count_cursor.moveToNext());
        }
        return result;
    }

    public void deleteAllSets() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete_query = "delete from learning_sets";

        db.execSQL(delete_query);
    }

    public HashMap<String, String> getGlossaryMapTermToDef(int term_to_def) { // term -> definition
        SQLiteDatabase db = this.getReadableDatabase();

        HashMap<String, String> glossary = new HashMap<>();
        String selectQuery = "select * from glossary";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                glossary.put(cursor.getString(1-term_to_def + 1), cursor.getString(term_to_def + 1));
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
                String term = cursor.getString(1);
                String definition = cursor.getString(2);
                if (term.length() <= maxLength && definition.length() <= maxLength) {
                    glossary.put(term, definition);
                }
            } while (cursor.moveToNext());
        }

        return glossary;
    }
}