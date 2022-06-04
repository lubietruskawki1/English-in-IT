package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.util.ArrayList;

import activities_menu.StartListActivity;
import comet.CometTimerActivity;

public class DailyRepetitions extends AppCompatActivity {
    private ConnectionHandler connection_handler;

    //TODO trzeba podać mu arraylistę (edytowalną) ze słówkami do powtórzenia

    Button start_button;// = findViewById();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connection_handler = new ConnectionHandler(DailyRepetitions.this);

        System.out.println("przed pobraniem listy setów");
        ArrayList<String> all_sets2 = connection_handler.getAllLearningSets();
        if(all_sets2.isEmpty()) System.out.println("pusty set 2");
        else System.out.println("NIie pusty set 2");

        for(int i = 0; i < all_sets2.size(); i++) {
            System.out.println("set w liście setów: " + all_sets2.get(i));
            //connection_handler.addWordToLearningSet("bit", all_sets2.get(i));
            //System.out.println("Dodało bit");
            //connection_handler.addWordToLearningSet("blacklist", all_sets2.get(i));
            //System.out.println("Dodało blacklist");
            try {
                ArrayList<Word> aktualna_lista = connection_handler.getLearningSetList(all_sets2.get(i));
                System.out.println("Printujemy listę:");
                for(int j = 0; j < aktualna_lista.size(); j++) {
                    System.out.println(aktualna_lista.get(j).word);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_repetitions);
        start_button = findViewById(R.id.start);

        start_button.setOnClickListener( v -> {
                Intent intent = new Intent(DailyRepetitions.this, TypingWordsExercise.class);
                startActivity(intent);
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_menu, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu:
                Intent settings_intent = new Intent(DailyRepetitions.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(DailyRepetitions.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
