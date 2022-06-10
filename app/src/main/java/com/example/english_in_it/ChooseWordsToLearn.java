package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;

import activities_menu.StartListActivity;


public class ChooseWordsToLearn extends AppCompatActivity {
    ArrayList<Word> chosen_words; //= new ArrayList<Word>();
    private ConnectionHandler connection_handler;
    private ListView wordsList;
    private Button startLearningButton;

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(chosen_words);
        editor.putString("task list", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Word>>() {}.getType();
        chosen_words = gson.fromJson(json, type);

        if (chosen_words == null) {
            chosen_words = new ArrayList<>();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadData();
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_choose_words);

        connection_handler = new ConnectionHandler(ChooseWordsToLearn.this);

        final ArrayList<String> glossary = ConnectionHandlerUtils.getGlossary(connection_handler);

        wordsList = findViewById(R.id.wordsList);
        ArrayAdapter<String> words_adapter = new ArrayAdapter<>(
                ChooseWordsToLearn.this, android.R.layout.simple_list_item_1, glossary);

        wordsList.setAdapter(words_adapter);

        Bundle extras = getIntent().getExtras();
        String set_name = extras.getString("set_name");
        ArrayList<String> glossaryJustTerms = ConnectionHandlerUtils.getGlossaryJustTerms(connection_handler);

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
                                Toast.makeText(ChooseWordsToLearn.this, "Added " + glossary.get(position), Toast.LENGTH_SHORT).show();
                                //TODO sprawdzenie czy nie dodaje sie 2 razy to samo
                                ConnectionHandlerUtils.addWordToLearningSet(connection_handler, glossaryJustTerms.get(position), set_name);
                                String firstDat = "2/11/2023";
                                Word to_add = null;
                                try {
                                    to_add = new Word(glossary.get(position),  glossary.get(position), 0, firstDat);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                chosen_words.add(to_add);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(ChooseWordsToLearn.this, "Cancelled.", Toast.LENGTH_SHORT).show();
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
                saveData();
                for(int i = 0; i < chosen_words.size(); i++) {
                    //Toast pokazujący, że wartości tablicy chosen_words zachowują się pomiedzy wywołaniami
                    Toast.makeText(ChooseWordsToLearn.this, chosen_words.get(i).word, Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(ChooseWordsToLearn.this, TypingWordsExercise.class);

                Bundle args = new Bundle();
                args.putSerializable("words",(Serializable)chosen_words);
                intent.putExtra("bundle",args);

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
    public boolean onCreateOptionsMenu (Menu menu){
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