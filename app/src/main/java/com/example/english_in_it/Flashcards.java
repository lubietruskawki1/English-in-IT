package com.example.english_in_it;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Flashcards extends AppCompatActivity {
    private ConnectionHandler connection_handler;
    private Button next_btn;
    private CardView flashcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);

        connection_handler = new ConnectionHandler(Flashcards.this);
        HashMap<String, String> glossary = connection_handler.getGlossaryMapTermToDef();

        Bundle flashcards_bundle = getIntent().getExtras();
        Boolean defToTerm = flashcards_bundle.getBoolean("defToTerm");

        if (defToTerm) {
            Toast.makeText(Flashcards.this, "definition to term", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(Flashcards.this, "term to definition", Toast.LENGTH_SHORT).show();
        }
    }
}