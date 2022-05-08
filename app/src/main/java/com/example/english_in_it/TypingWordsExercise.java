package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


public class TypingWordsExercise extends AppCompatActivity {

    ArrayList<Word> words = new ArrayList<Word>();

    private ConnectionHandler connection_handler;

    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    public TextView meaning;
    public Button check_button;
    public EditText word;
    private Iterator<Word> iter = words.iterator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Word word1 = new Word("a","b",0,0,new Date());
        Word word2 = new Word("c","d",0,0,new Date());
        words.add(word1);
        words.add(word2);
        Iterator<Word> iter = words.iterator();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typing_words);

        connection_handler = new ConnectionHandler(TypingWordsExercise.this);

        meaning = findViewById(R.id.meaning);
        check_button = findViewById(R.id.check_button);
        word = findViewById(R.id.word);

        Word current_word = iter.next();
        meaning.setText(current_word.meaning);
        check_button.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {
                if(!iter.hasNext()) {
                    Toast.makeText(TypingWordsExercise.this, "FINISHED", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TypingWordsExercise.this, StartListActivity.class);
                    startActivity(intent);
                    return;
                }
                Word current_word = iter.next();
                meaning.setText(current_word.meaning);
                String entered_word = word.getText().toString();
                Toast.makeText(TypingWordsExercise.this, "entered: " + entered_word, Toast.LENGTH_SHORT).show();
            }
        }));
    }


}
