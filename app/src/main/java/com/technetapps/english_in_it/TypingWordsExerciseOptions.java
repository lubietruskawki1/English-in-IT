package com.technetapps.english_in_it;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.technetapps.english_in_it.*;
import activities_menu.StartListActivity;
import learning_sets.Set;

import java.util.ArrayList;

/** Class implements choice of word set for typing word exercise.
 */

public class TypingWordsExerciseOptions extends AppCompatActivity {
    private String selectedSet;
    private Spinner setSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_typing_exercise_options);

        setSpinner = findViewById(R.id.setSpinner);

        ConnectionHandler c = new ConnectionHandler(this);
        ConnectionHandlerUtils connection_handler_utils = new ConnectionHandlerUtils(c);
        ArrayList<Set> setsAndTermNumbers = connection_handler_utils.getAllLearningSets();
        ArrayList<String> sets = new ArrayList<>();
        for (Set set : setsAndTermNumbers) {
            sets.add(set.getName() + " (" + set.getTerms_number() + " terms)");
        }

        ArrayAdapter<String> setsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                sets
        );

        setSpinner.setAdapter(setsAdapter);
        setSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSet = setsAndTermNumbers.get(i).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        Button goToExercise = findViewById(R.id.btnExercise);
        goToExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypingWordsExerciseOptions.this, TypingWordsExercise.class);
                Bundle repetitions_bundle = new Bundle();
                repetitions_bundle.putBoolean("repetitions", false);
                repetitions_bundle.putString("selectedSet", selectedSet);
                intent.putExtras(repetitions_bundle);
                startActivity(intent);
            }
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
                Intent settings_intent = new Intent(TypingWordsExerciseOptions.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(TypingWordsExerciseOptions.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}