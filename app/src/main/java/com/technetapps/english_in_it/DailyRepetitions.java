package com.technetapps.english_in_it;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.technetapps.english_in_it.R;

import activities_menu.StartListActivity;

/**Class implements activity daily repetitions.
 * It calls activity typing words exercise with repetitions option.*/


public class DailyRepetitions extends AppCompatActivity {
    private ConnectionHandler connection_handler;

    Button start_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connection_handler = new ConnectionHandler(DailyRepetitions.this);

        /*connection_handler.newLearningSet("set1");
        connection_handler.newLearningSet("set2");
        connection_handler.addWordToLearningSet("bit", "set1");
        connection_handler.addWordToLearningSet("blacklist", "set1");
        connection_handler.addWordToLearningSet("byte", "set2");
        connection_handler.setWordDaysWaitedPrev("bit", 1);
        connection_handler.setWordDaysWaitedPrev("blacklist", 1);
        connection_handler.setWordDaysWaitedPrev("byte", 1);
        Date today = new Date();
        connection_handler.setWordRepetitionDate("bit", today);
        connection_handler.setWordRepetitionDate("blacklist", today);
        connection_handler.setWordRepetitionDate("byte", today);
        System.out.println("Dodawanie do bazy zakońćzone");*/

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_repetitions);
        start_button = findViewById(R.id.start);

        start_button.setOnClickListener( v -> {
            Intent intent = new Intent(DailyRepetitions.this, TypingWordsExercise.class);
            Bundle repetitions_bundle = new Bundle();
            repetitions_bundle.putBoolean("repetitions", true);
            intent.putExtras(repetitions_bundle);
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
