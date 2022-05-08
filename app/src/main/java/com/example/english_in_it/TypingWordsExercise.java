package com.example.english_in_it;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TypingWordsExercise extends AppCompatActivity {

    ArrayList<Word> words = new ArrayList<Word>();

    private ConnectionHandler connection_handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typing_words);

        connection_handler = new ConnectionHandler(TypingWordsExercise.this);

        TextView meaning = findViewById(R.id.meaning);
        Button check_button = findViewById(R.id.check_button);
        EditText word = findViewById(R.id.word);
    }

}
