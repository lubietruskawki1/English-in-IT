package com.example.english_in_it;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ChooseWordsToLearn extends AppCompatActivity {
    ArrayList<Word> str = new ArrayList<Word>();
    private ConnectionHandler connection_handler;
    private ListView wordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_words);

        connection_handler = new ConnectionHandler(ChooseWordsToLearn.this);

        final ArrayList<String> glossary = connection_handler.getGlossary();

        wordsList = findViewById(R.id.wordsList);
        ArrayAdapter<String> words_adapter = new ArrayAdapter<>(
                ChooseWordsToLearn.this, android.R.layout.simple_list_item_1, glossary);

        wordsList.setAdapter(words_adapter);

        wordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(ChooseWordsToLearn.this, "selected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}