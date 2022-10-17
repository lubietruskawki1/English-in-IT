package memory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.technetapps.english_in_it.R;
import com.technetapps.english_in_it.Settings;
import activities_menu.StartListActivity;
import com.technetapps.english_in_it.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

/**
 * Actual game of Memory.
 */
public class MemoryGame extends AppCompatActivity {
    private static final int PADDING_LEFT_AND_RIGHT = 15;
    private static final int PADDING_TOP_AND_BOTTOM = 0;

    private HashMap<String, String> glossary;
    private int totalCards;
    private TextView[] buttons;
    private final Random randomGenerator = new Random();
    private boolean purgatory = false;
    private float correctAnswers = 0;
    private float totalAnswers = 0;
    private int pairsLeftToMatch = 0;
    private String[] board;
    private Boolean shown = true;
    private int firstCard = -1;
    private int secondCard = -1;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_memory_game);

        glossary = (HashMap<String, String>) getIntent().getSerializableExtra("glossary");
        Bundle extras = getIntent().getExtras();
        int columns = extras.getInt("columns");
        int buttonHeight = extras.getInt("buttonHeight");

        pairsLeftToMatch = glossary.size();
        totalCards = 2 * glossary.size();
        buttons = new TextView[totalCards];
        board = new String[totalCards];

        ArrayList<Pair<String, Boolean>> cards = new ArrayList<>(); // (karta, czy_użyta)
        for (String key: glossary.keySet()) {
            cards.add(new Pair<>(key, false));
            cards.add(new Pair<>(glossary.get(key), false));
        }

        TableLayout table = findViewById(R.id.tableLayout);

        @SuppressLint("InflateParams") TableRow row = (TableRow) LayoutInflater.from(this).
                inflate(R.layout.table_row_template, null);
        row.setPadding(PADDING_LEFT_AND_RIGHT, PADDING_TOP_AND_BOTTOM,
                PADDING_LEFT_AND_RIGHT, PADDING_TOP_AND_BOTTOM);
        table.addView(row);
        int buttonsInCurrentRow = 0;

        for (int i = 0; i < totalCards; i++) {
            // Generating a random place on board to place current card,
            int random_card = randomGenerator.nextInt(totalCards);
            while (cards.get(random_card).second) {
                random_card = randomGenerator.nextInt(totalCards);
            }

            if (buttonsInCurrentRow == columns) {
                row = (TableRow) LayoutInflater.from(this).
                        inflate(R.layout.table_row_template, null);
                row.setPadding(PADDING_LEFT_AND_RIGHT, PADDING_TOP_AND_BOTTOM,
                        PADDING_LEFT_AND_RIGHT, PADDING_TOP_AND_BOTTOM);
                table.addView(row);
                buttonsInCurrentRow = 0;
            }

            board[i] = cards.get(random_card).first;

            buttons[i] = (TextView) LayoutInflater.from(this).
                    inflate(R.layout.button_template, null);
            buttons[i].setText(cards.get(random_card).first);
            buttons[i].setId(i);
            row.addView(buttons[i], new TableRow.LayoutParams(0,
                    buttonHeight, 1f));

            cards.set(random_card, new Pair<>(cards.get(random_card).first, true));
            buttonsInCurrentRow++;
        }

        for (int i = 0; i < totalCards; i++) {
            int finalI = i;
            buttons[i].setOnClickListener(view -> {
                if (purgatory) {
                    switchSpot(firstCard);
                    switchSpot(secondCard);
                    firstCard = -1;
                    secondCard = -1;
                    purgatory = false;
                }

                if (shown) {
                    hideFields();
                } else {
                    switchSpot(finalI);
                    if (firstCard == -1) {
                        firstCard = finalI;
                    } else {
                        totalAnswers++;
                        if ((!Objects.equals(glossary.get(board[firstCard]), board[finalI]) &&
                                !Objects.equals(glossary.get(board[finalI]), board[firstCard])) ||
                                firstCard == finalI) {
                            // Incorrect.
                            secondCard = finalI;
                            purgatory = true;
                        } else {
                            // Correct.
                            board[finalI] = "done";
                            board[firstCard] = "done";
                            correctAnswers++;
                            pairsLeftToMatch--;
                            checkWin();
                            firstCard = -1;
                        }
                    }
                }
            });
        }
    }

    public void hideFields() {
        for (int i = 0; i < totalCards; i++){
            buttons[i].setText("");
        }
        shown = false;
    }

    // If card wasn't matched yet, switches to hidden if was shown and vice versa.
    public void switchSpot(int i) {
        if (!Objects.equals(board[i], "done")) {
            if (Objects.equals(buttons[i].getText(), "")){
                buttons[i].setText(board[i]);
            } else {
                buttons[i].setText("");
            }
        }
    }

    // Checks if all matches have been made and switches to winner screen if so.
    public void checkWin() {
        if (pairsLeftToMatch > 0) {
            return;
        }
        winner();
    }

    public void winner() {
        Intent intent = new Intent(MemoryGame.this, MemoryWin.class);
        int allowedMistakes = totalCards / 2;
        int score = Math.max(Math.round((correctAnswers + allowedMistakes) * 100 / totalAnswers), 0);
        score = Math.min(score, 100);
        intent.putExtra("score", Integer.toString(score));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu:
                Intent settings_intent = new Intent(MemoryGame.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(MemoryGame.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}