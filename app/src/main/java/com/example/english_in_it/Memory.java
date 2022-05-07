package com.example.english_in_it;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

public class Memory extends AppCompatActivity {
    private ConnectionHandler connection_handler;
    private Button startButton;
    //private HashMap<String, String> glossary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        connection_handler = new ConnectionHandler(Memory.this);

        // glossary = connection_handler.getGlossaryMap();
        // todo: dziedziczenie

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(view -> {
            Intent intent = new Intent(Memory.this, StartMemoryGame.class);
            startActivity(intent);
        });
    }
}