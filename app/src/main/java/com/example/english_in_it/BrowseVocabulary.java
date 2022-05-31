package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BrowseVocabulary extends AppCompatActivity {
    private ConnectionHandler connection_handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_browse_vocabulary);

        connection_handler = new ConnectionHandler(BrowseVocabulary.this);

        ArrayList<String> glossary = connection_handler.getGlossary();

        ListView browseVocabularyList = findViewById(R.id.browseVocabularyList);
        ArrayAdapter<String> browseVocabularyAdapter = new ArrayAdapter<>(
                BrowseVocabulary.this, android.R.layout.simple_list_item_1, glossary);

        browseVocabularyList.setAdapter(browseVocabularyAdapter);
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