package com.example.english_in_it;

import android.util.Pair;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class StartMemoryGame extends AppCompatActivity {
    private ConnectionHandler connection_handler;
    private final Button[] buttons = new Button[20];
    private HashMap<String, String> glossary;
    private int columns = 4;
    private int buttonTextSize = 4;
    private int buttonWidth = 250;
    private int buttonHeight = 300;
    private int total_cards = 20;
    private final Random random_generator = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);

        connection_handler = new ConnectionHandler(StartMemoryGame.this);

        glossary = connection_handler.getGlossaryMap();
        ArrayList<Pair<String, Boolean>> cards = new ArrayList<>(); // karta, czy użyta
        for (String key: glossary.keySet()) {
            cards.add(new Pair<>(key, false));
            cards.add(new Pair<>(glossary.get(key), false));
        }

        TableLayout table = findViewById(R.id.tableLayout);

        // todo: XD
        TableRow row = new TableRow(this);
        table.addView(row);
        int buttonsInCurrentRow = 0;
        for (int i = 0; i < total_cards; i++) {
            int random_card = random_generator.nextInt(total_cards);
            while (cards.get(random_card).second) {
                random_card = random_generator.nextInt(total_cards);
            }

            if (buttonsInCurrentRow == columns) {
                row = new TableRow(this);
                table.addView(row);
                buttonsInCurrentRow = 0;
            }

            // buttons[i] = findViewById(R.id.button);
            buttons[i] = new Button(this);
            buttons[i].setText(cards.get(random_card).first);
            buttons[i].setTextSize(buttonTextSize);
            row.addView(buttons[i], buttonWidth, buttonHeight);

            cards.set(random_card, new Pair<>(cards.get(random_card).first, true));
            buttonsInCurrentRow++;
        }
    }
}