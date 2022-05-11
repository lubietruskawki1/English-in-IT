package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "sample";
    private static final int DB_VERSION = 1;

    public ConnectionHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("drop table if exists glossary");
        String query = "create table glossary (term text, definition text)";
        db.execSQL(query);
        fillDatabase(db);
    }

    public void fillDatabase(SQLiteDatabase db) {
        //SQLiteDatabase db = this.getWritableDatabase();

        // tak jest ładniej ale nie chce mi sie xd
        ContentValues values = new ContentValues();
        values.put("term", "abstract data type");
        values.put("definition", "A mathematical model for data types in which a data type is defined by its behavior (semantics) from the point of view of a user of the data, specifically in terms of possible values, possible operations on data of this type, and the behavior of these operations.");
        db.insert("glossary", null, values);

        //db.execSQL("insert into glossary values('abstract data type', 'A mathematical model for data types in which a data type is defined by its behavior (semantics) from the point of view of a user of the data, specifically in terms of possible values, possible operations on data of this type, and the behavior of these operations.')");
        db.execSQL("insert into glossary values('abstract method', 'One with only a signature and no implementation body.')");
        db.execSQL("insert into glossary values('abstraction', 'In software engineering and computer science, the process of removing physical, spatial, or temporal details or attributes in the study of objects or systems in order to more closely attend to other details of interest; it is also very similar in nature to the process of generalization.')");
        db.execSQL("insert into glossary values('agent architecture', 'A blueprint for software agents and intelligent control systems depicting the arrangement of components.')");
        db.execSQL("insert into glossary values('agent-based model', 'A class of computational models for simulating the actions and interactions of autonomous agents (both individual or collective entities such as organizations or groups) with a view to assessing their effects on the system as a whole.')");
        db.execSQL("insert into glossary values('aggregate function', 'In database management, a function in which the values of multiple rows are grouped together to form a single value of more significant meaning or measurement, such as a sum, count, or max.')");
        db.execSQL("insert into glossary values('agile software development', 'An approach to software development under which requirements and solutions evolve through the collaborative effort of self-organizing and cross-functional teams and their customer(s)/end user(s).')");
        db.execSQL("insert into glossary values('algorithm', 'An unambiguous specification of how to solve a class of problems.')");
        db.execSQL("insert into glossary values('algorithm design', 'A method or mathematical process for problem-solving and for engineering algorithms.')");
        db.execSQL("insert into glossary values('algorithmic efficiency', 'A property of an algorithm which relates to the number of computational resources used by the algorithm.')");

        //db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists glossary");
        onCreate(db);
    }

    public ArrayList<String> getGlossary() {
        ArrayList<String> glossary = new ArrayList<>();
        String selectQuery = "select * from glossary";

        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                glossary.add(cursor.getString(0) + ": " + cursor.getString(1));
            } while (cursor.moveToNext());
        }

        return glossary;
    }

    public HashMap<String, String> getGlossaryMapTermToDef() { // term -> definition
        HashMap<String, String> glossary = new HashMap<>();
        String selectQuery = "select * from glossary";

        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                glossary.put(cursor.getString(0), cursor.getString(1));
            } while (cursor.moveToNext());
        }

        return glossary;
    }
}