package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import activities_menu.StartListActivity;


public class ChooseWordsToLearn extends AppCompatActivity {
    ArrayList<Word> chosen_words = new ArrayList<Word>();
    private ConnectionHandler connection_handler;
    private ListView wordsList;
    private Button startLearningButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_choose_words);

        connection_handler = new ConnectionHandler(ChooseWordsToLearn.this);

        final ArrayList<String> glossary = connection_handler.getGlossary();

        wordsList = findViewById(R.id.wordsList);
        ArrayAdapter<String> words_adapter = new ArrayAdapter<>(
                ChooseWordsToLearn.this, android.R.layout.simple_list_item_1, glossary);

        wordsList.setAdapter(words_adapter);

        wordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(ChooseWordsToLearn.this, "selected", Toast.LENGTH_SHORT).show();
                //showSnackbar();
                //showDialog();
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseWordsToLearn.this);
                builder.setMessage("Add this word to your base?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(ChooseWordsToLearn.this, "Added.", Toast.LENGTH_SHORT).show();
                                //TODO dodawanie do bazy
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(ChooseWordsToLearn.this, "Canceled.", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        startLearningButton = findViewById(R.id.startLearning);
        startLearningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseWordsToLearn.this, TypingWordsExercise.class);
                startActivity(intent);
            }
        });
    }


    private void showSnackbar() {
        Snackbar.make(wordsList,"Add to your base?", Snackbar.LENGTH_INDEFINITE)
                .setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(ChooseWordsToLearn.this, "Added.", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
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
                Intent settings_intent = new Intent(ChooseWordsToLearn.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(ChooseWordsToLearn.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}