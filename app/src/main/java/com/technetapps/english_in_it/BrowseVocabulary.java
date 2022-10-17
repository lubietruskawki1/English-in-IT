package com.technetapps.english_in_it;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.technetapps.english_in_it.R;

import java.util.ArrayList;
import java.util.HashMap;

import activities_menu.StartListActivity;

/**
 * Class implements browsing vocabulary.
 */

public class BrowseVocabulary extends AppCompatActivity {
    private ConnectionHandler connection_handler;
    private RecyclerView vocabulary_view;
    private VocabularyRecViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_browse_vocabulary);

        connection_handler = new ConnectionHandler(BrowseVocabulary.this);
        ConnectionHandlerUtils connection_handler_utils = new ConnectionHandlerUtils(connection_handler);

        HashMap<String, String> glossary = connection_handler_utils.getGlossaryMapTermToDef(1);
        ArrayList<String> glossary_terms = connection_handler_utils.getGlossaryJustTerms();
        ArrayList<TermAndDef> vocab = new ArrayList<>();

        for (String term : glossary_terms) {
            vocab.add(new TermAndDef(term, glossary.get(term)));
        }

        vocabulary_view = findViewById(R.id.VocabularyRecRecView);
        adapter = new VocabularyRecViewAdapter(this);
        adapter.setVocabulary(vocab);
        vocabulary_view.setAdapter(adapter);
        vocabulary_view.setLayoutManager(new LinearLayoutManager(this));
        vocabulary_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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
                Intent settings_intent = new Intent(BrowseVocabulary.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(BrowseVocabulary.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}