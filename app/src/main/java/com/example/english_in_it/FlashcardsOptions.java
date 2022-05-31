package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class FlashcardsOptions extends AppCompatActivity {
    private boolean defToTerm = true;
    private RadioGroup HowToLearn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_flashcards_options);

        HowToLearn = findViewById(R.id.termDefinitionChoice);
        HowToLearn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.defToTerm:
                        defToTerm = true;
                        break;
                    case R.id.termToDef:
                        defToTerm = false;
                        break;
                }
            }
        });

        Button goToFlashcardsButton = findViewById(R.id.btnFlashcards);
        goToFlashcardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FlashcardsOptions.this, Flashcards.class);

                Bundle flashcards_bundle = new Bundle();
                flashcards_bundle.putBoolean("defToTerm", defToTerm);
                intent.putExtras(flashcards_bundle);

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
                Intent settings_intent = new Intent(FlashcardsOptions.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(FlashcardsOptions.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}