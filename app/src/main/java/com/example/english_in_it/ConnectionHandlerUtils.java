package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import learning_sets.Set;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ConnectionHandlerUtils {
    public static ArrayList<String> getGlossary(ConnectionHandler connection_handler) {
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

    public static ArrayList<String> getGlossaryJustTerms(ConnectionHandler connection_handler) {
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

    public static ArrayList<Word> getLearningSetList(ConnectionHandler connection_handler, String set_name) throws ParseException {
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

    public static HashMap<String, String> getSetGlossaryMapTermToDef(ConnectionHandler connection_handler, String set_name, int term_to_def) throws ParseException {
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

    public static HashMap<String, String> getSetGlossaryMapDefToTerm(ConnectionHandler connection_handler, String set_name) throws ParseException { // definition -> term
        return getSetGlossaryMapTermToDef(connection_handler, set_name, 0);
    }

    public static void setWordDaysWaitedPrev(ConnectionHandler connection_handler, String word_term, int new_value) {
        SQLiteDatabase db = connection_handler.getWritableDatabase();

        String query_update = "update glossary set days_waited_prev = '" + new_value + " where term = " + word_term + "';";
        db.execSQL(query_update);
    }

    public static void setWordRepetitionDate(ConnectionHandler connection_handler, String word_term, Date new_value) {
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

    public static void newLearningSet(ConnectionHandler connection_handler, String set_name) {
        SQLiteDatabase db = connection_handler.getWritableDatabase();
        String query = "insert into learning_sets values('" + set_name + "');";
        db.execSQL(query);
    }

    public static void deleteLearningSet(ConnectionHandler connection_handler, String set_name) {
        SQLiteDatabase db = connection_handler.getWritableDatabase();

        String query = "delete from learning_sets where set_name = '" + set_name + "';";
        db.execSQL(query);

        query = "delete from learning_sets_contents where set_name = '" + set_name + "';";
        db.execSQL(query);
    }

    public static Cursor getData(ConnectionHandler connection_handler, String sql){
        SQLiteDatabase database = connection_handler.getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public static void addWordToLearningSet(ConnectionHandler connection_handler, String word, String set_name) {
        SQLiteDatabase db = connection_handler.getWritableDatabase();
        String select_query = "select * from learning_sets_contents where set_name = '" + set_name + "' and term ='" + word + "';";
        if (getData(connection_handler, select_query).getCount() == 0) {
            String insert_query = "insert into learning_sets_contents values('" + word + "','" + set_name + "');";
            db.execSQL(insert_query);
        }
    }

    public static void deleteWordFromLearningSet(ConnectionHandler connection_handler, String word, String set_name) {
        SQLiteDatabase db = connection_handler.getWritableDatabase();
        String delete_query = "delete from learning_sets_contents where set_name = '" + set_name + "' and term ='" + word + "';";
        db.execSQL(delete_query);
    }

    public static ArrayList<String> getAllLearningSetNames(ConnectionHandler connection_handler) {
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

    public static ArrayList<String> getEmptySets(ConnectionHandler connection_handler) {
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

    public static ArrayList<Set> getAllLearningSets(ConnectionHandler connection_handler) {
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

    public static void deleteAllSets(ConnectionHandler connection_handler) {
        SQLiteDatabase db = connection_handler.getWritableDatabase();
        String delete_query = "delete from learning_sets";

        db.execSQL(delete_query);
    }

    public static HashMap<String, String> getGlossaryMapTermToDef(ConnectionHandler connection_handler, int term_to_def) { // term -> definition
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

    public static HashMap<String, String> getGlossaryMapDefToTerm(ConnectionHandler connection_handler) { // definition -> term
        return getGlossaryMapTermToDef(connection_handler, 0);
    }

    public static HashMap<String, String> getFilteredGlossaryMapTermToDef(ConnectionHandler connection_handler, int maxLength) { // term -> definition
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
