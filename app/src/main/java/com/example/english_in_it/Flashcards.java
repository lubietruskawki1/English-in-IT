package com.example.english_in_it;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Flashcards extends AppCompatActivity {
    private ConnectionHandler connection_handler;
    private Button next_btn;
    private Button prev_btn;
    private CardView flashcard;
    private TextView flashcard_txt;
    private TextView progress_txt;
    int current_flashcard = 1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);
        flashcard = findViewById(R.id.flashcard);
        next_btn = findViewById(R.id.btnNextFlashcard);
        prev_btn = findViewById(R.id.btnPrevFlashcard);
        flashcard_txt = findViewById(R.id.termOrDefTxt);
        progress_txt = findViewById(R.id.progressNumberTxt);

        connection_handler = new ConnectionHandler(Flashcards.this);
        HashMap<String, String> glossary;

        Bundle flashcards_bundle = getIntent().getExtras();
        Boolean defToTerm = flashcards_bundle.getBoolean("defToTerm");

        if (defToTerm) {
            glossary = connection_handler.getGlossaryMapTermToDef(0);
            Toast.makeText(Flashcards.this, "definition to term", Toast.LENGTH_SHORT).show();
        }
        else {
            glossary = connection_handler.getGlossaryMapTermToDef(1);
            Toast.makeText(Flashcards.this, "term to definition", Toast.LENGTH_SHORT).show();
        }

        Set<String> keys = glossary.keySet();
        Integer cards_number = keys.size();
        String[] front_sides = keys.toArray(new String[cards_number]);

        progress_txt.setText("1/" + cards_number.toString());
        flashcard_txt.setText(front_sides[current_flashcard - 1]);
        flashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            //tu bedzie flip jeśli zachce mi sie go zrobić
            public void onClick(View view) {
                flashcard_txt.setText(glossary.get(front_sides[current_flashcard - 1]));
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_flashcard != cards_number) {
                    current_flashcard +=1;

                    flashcard_txt.setText(front_sides[current_flashcard - 1]);
                    progress_txt.setText(current_flashcard + "/" + cards_number.toString());
                }
            }
        });

        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_flashcard != 1) {
                    current_flashcard -=1;

                    flashcard_txt.setText(front_sides[current_flashcard - 1]);
                    progress_txt.setText(current_flashcard + "/" + cards_number.toString());
                }
            }
        });
    }
}