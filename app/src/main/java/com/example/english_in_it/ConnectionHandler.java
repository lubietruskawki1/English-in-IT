package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
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
        db.execSQL("delete from glossary");
        db.execSQL("drop table if exists glossary"); // to będzie do wykasowania gdy już będzie gotowa baza
        String query = "create table if not exists glossary (term text, definition text)";
        db.execSQL(query);
        fillDatabase(db);
    }

    public void fillDatabase(SQLiteDatabase db) {
        // tak jest ładniej ale nie chce mi sie xd
//        ContentValues values = new ContentValues();
//        values.put("term", "abstract data type");
//        values.put("definition", "A mathematical model for data types in which a data type is defined by its behavior (semantics).");
//        db.insert("glossary", null, values);


        InputStream inserts = this.context.getResources().openRawResource(R.raw.inserty_do_bazuchy);
        Scanner s = new Scanner(inserts).useDelimiter("\\A");
        String sql_inserts = s.hasNext() ? s.next() : "";

        String[] single_inserts = sql_inserts.split(";");
        //BufferedReader reader = new BufferedReader(new InputStreamReader(inserts));

        //sql_inserts = new Scanner(new File(path)).useDelimiter("\\Z").next();
        db.execSQL(sql_inserts);
        for (int i = 0; i < single_inserts.length - 1; i++) {
            System.out.println(i);
            db.execSQL(single_inserts[i]);
        }

        //db.execSQL("insert into glossary values('abstract data type', 'A mathematical model for data types in which a data type is defined by its behavior (semantics) from the point of view of a user of the data, specifically in terms of possible values, possible operations on data of this type, and the behavior of these operations.')");
        /*db.execSQL("insert into glossary values('abstract method', 'abstract method definition')");
        db.execSQL("insert into glossary values('abstraction', 'abstraction definition')");
        db.execSQL("insert into glossary values('agent architecture', 'agent architecture definition')");
        db.execSQL("insert into glossary values('agent-based model', 'agent-based model definition')");
        db.execSQL("insert into glossary values('aggregate function', 'aggregate function definition')");
        db.execSQL("insert into glossary values('agile software development', 'agile software development definition')");
        db.execSQL("insert into glossary values('algorithm', 'algorithm definition')");
        db.execSQL("insert into glossary values('algorithm design', 'algorithm design definition')");
        db.execSQL("insert into glossary values('algorithmic efficiency', 'algorithmic efficiency definition')");*/
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

        //this.onCreate(db);
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
}