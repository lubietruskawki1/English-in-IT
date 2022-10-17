package com.technetapps.english_in_it;

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
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.technetapps.english_in_it.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

import activities_menu.StartListActivity;
import learning_sets.CreateOwnTermSets;

/**Class implements the choice of words added to set.
 */

public class ChooseWordsToLearn extends AppCompatActivity {
    ArrayList<Word> chosen_words; //= new ArrayList<Word>();
    private ConnectionHandler connection_handler;
    // private ListView wordsList;
    private Button startLearningButton;
    private RecyclerView vocabulary_view;
    private VocabularyRecViewAdapter adapter;
    // private ArrayList<String> glossaryJustTerms;


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
        ConnectionHandlerUtils connection_handler_utils = new ConnectionHandlerUtils(connection_handler);

        HashMap<String, String> glossary = connection_handler_utils.getGlossaryMapTermToDef(1);
        ArrayList<String> glossary_terms = connection_handler_utils.getGlossaryJustTerms();
        ArrayList<TermAndDef> vocab = new ArrayList<>();

        for (String term : glossary_terms) {
            vocab.add(new TermAndDef(term, glossary.get(term)));
        }

        Bundle extras = getIntent().getExtras();
        String set_name = extras.getString("set_name");

        vocabulary_view = findViewById(R.id.wordsList);
        adapter = new VocabularyRecViewAdapter(this);
        adapter.setVocabulary(vocab);
        vocabulary_view.setAdapter(adapter);
        vocabulary_view.setLayoutManager(new LinearLayoutManager(this));
        vocabulary_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //glossaryJustTerms = connection_handler_utils.getGlossaryJustTerms();

        vocabulary_view.addOnItemTouchListener(new RecyclerItemClickListener(this, vocabulary_view, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseWordsToLearn.this);
                builder.setMessage("Add this word to your set?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //TODO sprawdzenie czy nie dodaje sie 2 razy to samo
                                TermAndDef word = adapter.getVocabulary().get(position);
                                connection_handler_utils.addWordToLearningSet(word.getTerm(), set_name);
                                Toast.makeText(ChooseWordsToLearn.this, "Added " + word.getTerm(), Toast.LENGTH_SHORT).show();
                                String firstDat = "2/11/2023";
                                Word to_add = null;
                                try {
                                    to_add = new Word(word.getTerm(),  word.getDef(), 0, firstDat);
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

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        startLearningButton = findViewById(R.id.startLearning);
        startLearningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Toast.makeText(ChooseWordsToLearn.this, "Saved.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChooseWordsToLearn.this, CreateOwnTermSets.class);
                startActivity(intent);
            }
        });
    }

    private void showSnackbar() {
        Snackbar.make(vocabulary_view,"Add to your set?", Snackbar.LENGTH_INDEFINITE)
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
        inflater.inflate(R.menu.browse_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
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