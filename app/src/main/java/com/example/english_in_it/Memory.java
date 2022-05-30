package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

public class Memory extends AppCompatActivity {
    SharedPreferences pref;
    protected ConnectionHandler connection_handler;
    private Button buttonStart;
    protected HashMap<String, String> glossary;
    // todo: wybór levelu - hard: 10 pojęć (4x5), medium: 8 pojęć (4x4), easy: 6 pojęć (3x4)
    // todo: na tej podstawie calculate columns, height
    // todo: instrukcja

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_memory);

        connection_handler = new ConnectionHandler(Memory.this);
        glossary = connection_handler.getGlossaryMapTermToDef(1);

        buttonStart = findViewById(R.id.btnStart);
        buttonStart.setOnClickListener(view -> {
            Intent intent = new Intent(Memory.this, StartMemoryGame.class);
            intent.putExtra("glossary", glossary);
            startActivity(intent);
        });
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
                Intent settings_intent = new Intent(Memory.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(Memory.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}