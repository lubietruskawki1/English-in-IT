package com.example.english_in_it;

import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.widget.*;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class StartMemoryGame extends AppCompatActivity {
    protected HashMap<String, String> glossary;
    private TableLayout table;
    private TableRow row;
    private int total_cards = 0;
    private int columns = 4;
    private int buttonHeight = 308;
    private int paddingLeftAndRight = 15;
    private int paddingTopAndBottom = 0;
    private TextView[] buttons;
    private final Random random_generator = new Random();
    private boolean purgatory = false;
    private int level = 0; // liczba par do dopasowania
    protected int score = 0; // todo: im większy tym lepszy lub you made x mistakes
    private int pairs_left_to_match = 0;
    private String[] board;
    private Boolean shown = true;
    private int first_card = -1;
    private int second_card = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);

        glossary = (HashMap<String, String>) getIntent().getSerializableExtra("glossary");

        pairs_left_to_match = glossary.size();
        total_cards = 2 * glossary.size();
        buttons = new TextView[total_cards];
        board = new String[total_cards];

        ArrayList<Pair<String, Boolean>> cards = new ArrayList<>(); // (karta, czy_użyta)
        for (String key: glossary.keySet()) {
            cards.add(new Pair<>(key, false));
            cards.add(new Pair<>(glossary.get(key), false));
        }

        table = findViewById(R.id.tableLayout);

        //buttonWidth = 250;//Math.round(getResources().getDimension(R.dimen.button_width));
        //buttonHeight = 300;//Math.round(getResources().getDimension(R.dimen.button_height));
        //System.out.println(buttonHeight + " "+ buttonWidth);
        //rowWidth = Math.round(getResources().getDimension(R.dimen.row_width));

        // todo: XD
        //row = new TableRow(this);
        //row.setWeightSum(4);
//        table.addView(row, new ViewGroup.LayoutParams(
//               rowWidth, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
//        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                1.0f
//        );
//        LinearLayout.LayoutParams paramBtn = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1.0f
//        );
       // row.setLayoutParams(param);
        row = (TableRow) LayoutInflater.from(this).
                inflate(R.layout.table_row_template, null);

//        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
//                TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.setPadding(paddingLeftAndRight, paddingTopAndBottom,
                paddingLeftAndRight, paddingTopAndBottom);
        table.addView(row);
        int buttonsInCurrentRow = 0;
        for (int i = 0; i < total_cards; i++) {
            int random_card = random_generator.nextInt(total_cards);
            while (cards.get(random_card).second) { // while czy_użyta == true
                random_card = random_generator.nextInt(total_cards); // losowanie nowej karty
            }

            if (buttonsInCurrentRow == columns) {
                row = (TableRow) LayoutInflater.from(this).
                        inflate(R.layout.table_row_template, null);
//                row = new TableRow(this);
//                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
//                        TableRow.LayoutParams.WRAP_CONTENT, 1f));
                //table.addView(row, new ViewGroup.LayoutParams(
                 //       rowWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
                //row.setLayoutParams(param);
               // row.setWeightSum(4);
                row.setPadding(paddingLeftAndRight, paddingTopAndBottom,
                        paddingLeftAndRight, paddingTopAndBottom);
                table.addView(row);
                buttonsInCurrentRow = 0;
            }

            board[i] = cards.get(random_card).first;

            //buttons[i] = (TextView)getLayoutInflater().inflate(R.layout.button_template, null);
            buttons[i] = (TextView) LayoutInflater.from(this).
                    inflate(R.layout.button_template, null);
            //buttons[i] = new TextView(this);
            buttons[i].setText(cards.get(random_card).first);
            //buttons[i].setTextSize(buttonTextSize);
            buttons[i].setId(i);
            //buttons[i].setSingleLine(false);
            //buttons[i].setMaxLines(20);
            //buttons[i].setHeight(300);
            //buttons[i].setLayoutParams(paramBtn);
            //buttons[i].setEms(4);
            //buttons[i].setMinHeight(200);
//            buttons[i].setLayoutParams(new ViewGroup.LayoutParams(
//                    250, 300));
                    //ViewGroup.LayoutParams.WRAP_CONTENT));
            //row.addView(buttons[i]);//, buttonWidth, buttonHeight);
            //buttons[i].setWidth(250);
            //buttons[i].setHeight(310);
            //row.addView(buttons[i]);
            row.addView(buttons[i], new TableRow.LayoutParams(0,
                    buttonHeight, 1f));
                    //TableRow.LayoutParams.MATCH_PARENT, 1f));

            cards.set(random_card, new Pair<>(cards.get(random_card).first, true));
            buttonsInCurrentRow++;
        }

        for (int i = 0; i < total_cards; i++) {
            int finalI = i;
            buttons[i].setOnClickListener(view -> {
                if (purgatory) {
                    score++;
                    System.out.println("score: "+score);
                    switchSpot(first_card);
                    switchSpot(second_card);
                    first_card = -1;
                    second_card = -1;
                    purgatory = false;
                }

                if (shown) {
                    hideFields();
                } else {
                    switchSpot(finalI);
                    if (first_card == -1) {
                        first_card = finalI;
                    } else {
                        if ((!Objects.equals(glossary.get(board[first_card]), board[finalI]) &&
                                !Objects.equals(glossary.get(board[finalI]), board[first_card])) ||
                                first_card == finalI) { // niepoprawne
                            second_card = finalI;
                            purgatory = true;
                        } else { // poprawne
                            // todo: też można by jakoś ładniej ewentualnie kiedyś zrobić
                            board[finalI] = "done";
                            board[first_card] = "done";
                            pairs_left_to_match -= pairs_left_to_match; // todo debug
                            checkWin();
                            first_card = -1;
                        }
                    }
                }
            });
        }
    }

    public void hideFields() {
        for (int i = 0; i < total_cards; i++){
            buttons[i].setText("");
        }
        shown = false;
    }

    public void switchSpot(int i) {
        if (!Objects.equals(board[i], "done")) {
            if (Objects.equals(buttons[i].getText(), "")){
                buttons[i].setText(board[i]);
            } else {
                buttons[i].setText("");
            }
        }
    }

    public void checkWin() {
        if (pairs_left_to_match > 0) {
            return;
        }
        winner();
    }

    public void winner() {
        Intent intent = new Intent(StartMemoryGame.this, MemoryWin.class);
        intent.putExtra("playerScore", Integer.toString(score));
        // if int extras.getInt("new_variable_name")
        startActivity(intent);
    }
}