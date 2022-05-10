package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
    public TextView good_bad;
    public TextView correct_answer;
    private Iterator<Word> iter = words.iterator();
    private Word current_word;
    private int counter_for_clicks;
    private boolean correct_current_answer;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Word word1 = new Word("abstract method","Method with only a signature and no implementation body.",0,0,new Date());
        Word word2 = new Word("algorithm","An unambiguous specification of how to solve a class of problems.",0,0,new Date());
        words.add(word1);
        words.add(word2);
        Iterator<Word> iter = words.iterator();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typing_words);

        connection_handler = new ConnectionHandler(TypingWordsExercise.this);

        meaning = findViewById(R.id.meaning);
        check_button = findViewById(R.id.check_button);
        word = findViewById(R.id.word);
        good_bad = findViewById(R.id.good_bad);
        correct_answer = findViewById(R.id.correct_answer);

        current_word = iter.next();
        meaning.setText(current_word.meaning);
        counter_for_clicks = 0;
        correct_answer.setText("");
        good_bad.setText("");
        check_button.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {
                if(counter_for_clicks%2 == 0) {//pojawia się nowe słówko, użytkownik ma możliwośćc wpisania tłumaczenia
                    //check_button.setText("CHECK");
                    correct_answer.setText("");
                    good_bad.setText("");
                    String entered_word = word.getText().toString();
                    if (entered_word.equals(current_word.word)) {
                        //Toast.makeText(TypingWordsExercise.this, "CORRECT!", Toast.LENGTH_SHORT).show();
                        //correct_current_answer = true;
                        good_bad.setText("CORRECT!");
                        //good_bad.setTextColor(Color.parseColor("0xff00ff00"));
                    } else {
                        //Toast.makeText(TypingWordsExercise.this, "Incorrect, correct answer is: " + current_word.word, Toast.LENGTH_SHORT).show();
                        //correct_current_answer = false;
                        good_bad.setText("INCORRECT");
                        correct_answer.setText("correct answer: " + current_word.word);
                        //good_bad.setTextColor(Color.parseColor("0xffff0000"));
                    }
                    check_button.setText("CONTINUE");
                    counter_for_clicks++;
                }
                else {//pojawia się nowe słówko
                    //good_bad.setTextColor(Color.parseColor("0xff000000"));
                    correct_answer.setText("");
                    good_bad.setText("");
                    check_button.setText("CHECK");
                    if (!iter.hasNext()) {
                        //Toast.makeText(TypingWordsExercise.this, "FINISHED", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TypingWordsExercise.this, StartListActivity.class);
                        startActivity(intent);
                        return;
                    }
                    current_word = iter.next();
                    meaning.setText(current_word.meaning);
                    word.setText("");

                    counter_for_clicks++;
                }
            }
        }));
    }


}
