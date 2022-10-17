package com.technetapps.english_in_it;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import learning_sets.Set;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *Class shares methods of connection handler, that are used in other classes.
 * It provides convenient access to database in the whole application.*/

public class ConnectionHandlerUtils {
    private final ConnectionHandler connection_handler;

    public ConnectionHandlerUtils(ConnectionHandler connection_handler) {
        this.connection_handler = connection_handler;
    }
    public ArrayList<String> getGlossary() {
        SQLiteDatabase db = connection_handler.getReadableDatabase();

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
        SQLiteDatabase db = connection_handler.getReadableDatabase();

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
        SQLiteDatabase db = connection_handler.getReadableDatabase();
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
        SQLiteDatabase db = connection_handler.getReadableDatabase();
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

    public void setWordDaysWaitedPrev(String word_term, int new_value) {
        SQLiteDatabase db = connection_handler.getWritableDatabase();

        String query_update = "update glossary set days_waited_prev = '" + new_value + " where term = " + word_term + "';";
        db.execSQL(query_update);
    }

    public void setWordRepetitionDate(String word_term, Date new_value) {
        SQLiteDatabase db = connection_handler.getWritableDatabase();

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
        SQLiteDatabase db = connection_handler.getWritableDatabase();
        String query = "insert into learning_sets values('" + set_name + "');";
        db.execSQL(query);
    }

    public void deleteLearningSet(String set_name) {
        SQLiteDatabase db = connection_handler.getWritableDatabase();

        String query = "delete from learning_sets where set_name = '" + set_name + "';";
        db.execSQL(query);

        query = "delete from learning_sets_contents where set_name = '" + set_name + "';";
        db.execSQL(query);
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = connection_handler.getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public void addWordToLearningSet(String word, String set_name) {
        SQLiteDatabase db = connection_handler.getWritableDatabase();
        String select_query = "select * from learning_sets_contents where set_name = '" + set_name + "' and term ='" + word + "';";
        if (getData(select_query).getCount() == 0) {
            String insert_query = "insert into learning_sets_contents values('" + word + "','" + set_name + "');";
            db.execSQL(insert_query);
        }
    }

    public void deleteWordFromLearningSet(String word, String set_name) {
        SQLiteDatabase db = connection_handler.getWritableDatabase();
        String delete_query = "delete from learning_sets_contents where set_name = '" + set_name + "' and term ='" + word + "';";
        db.execSQL(delete_query);
    }

    public ArrayList<String> getAllLearningSetNames() {
        SQLiteDatabase db = connection_handler.getReadableDatabase();
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
        SQLiteDatabase db = connection_handler.getReadableDatabase();
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
        SQLiteDatabase db = connection_handler.getReadableDatabase();
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
        SQLiteDatabase db = connection_handler.getWritableDatabase();
        String delete_query = "delete from learning_sets";

        db.execSQL(delete_query);
    }

    public HashMap<String, String> getGlossaryMapTermToDef(int term_to_def) { // term -> definition
        SQLiteDatabase db = connection_handler.getReadableDatabase();

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
        SQLiteDatabase db = connection_handler.getReadableDatabase();

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
