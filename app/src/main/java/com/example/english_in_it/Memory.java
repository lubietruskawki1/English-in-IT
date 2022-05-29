package com.example.english_in_it;

import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

public class Memory extends AppCompatActivity {
    protected ConnectionHandler connection_handler;
    private Button buttonStart;
    protected HashMap<String, String> glossary;
    // todo: wybór levelu - hard: 10 pojęć (4x5), medium: 8 pojęć (4x4), easy: 6 pojęć (3x4)
    // todo: na tej podstawie calculate columns, height
    // todo: instrukcja

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        connection_handler = new ConnectionHandler(Memory.this);
        glossary = connection_handler.getGlossaryMapTermToDef(1);

        buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(view -> {
            Intent intent = new Intent(Memory.this, StartMemoryGame.class);
            intent.putExtra("glossary", glossary);
            startActivity(intent);
        });
    }
}