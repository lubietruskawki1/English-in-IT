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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import activities_menu.StartListActivity;
import comet.CometTimerActivity;

public class DailyRepetitions extends AppCompatActivity {
    private ConnectionHandler connection_handler;

    //TODO trzeba podać mu arraylistę (edytowalną) ze słówkami do powtórzenia
    ArrayList<Word> list_to_repeat = new ArrayList<Word>();
    Button start_button;// = findViewById();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connection_handler = new ConnectionHandler(DailyRepetitions.this);
        ArrayList<String> all_sets2 = connection_handler.getAllLearningSets();
        for(int i = 0; i < all_sets2.size(); i++) {
            Date today = new Date();
            connection_handler.setWordRepetitionDate("bit", today);
            try {
                ArrayList<Word> current_list = connection_handler.getLearningSetList(all_sets2.get(i));
                for(int j = 0; j < current_list.size(); j++) {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                    String formated_today = formatter.format(today);
                    if(current_list.get(j).when_to_remind != null && formated_today.equals(formatter.format(current_list.get(j).when_to_remind))) {
                        list_to_repeat.add(current_list.get(j));
                    }
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
