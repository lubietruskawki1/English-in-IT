package memory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.Button;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.technetapps.english_in_it.ConnectionHandler;
import com.technetapps.english_in_it.ConnectionHandlerUtils;
import com.technetapps.english_in_it.R;
import com.technetapps.english_in_it.Settings;
import com.technetapps.english_in_it.Utils;

import activities_menu.StartListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Starting screen of Memory game.
 * @param[in] difficulty : user-selected game difficulty (easy - 12 cards, medium - 16 cards, hard - 20 cards)
 */
public class Memory extends AppCompatActivity {
    private static final int LEVEL_NOT_SET = -1;
    private static final int LEVEL_EASY = 1;
    private static final int LEVEL_MEDIUM = 2;
    private static final int LEVEL_HARD = 3;

    private static final int CARDS_LEVEL_EASY = 12; // 3x4
    private static final int CARDS_LEVEL_MEDIUM = 16; // 4x4
    private static final int CARDS_LEVEL_HARD = 20; // 4x5

    private static final int MAX_LENGTH_LEVEL_EASY = 110;
    private static final int MAX_LENGTH_LEVEL_MEDIUM = 85;
    private static final int MAX_LENGTH_LEVEL_HARD = 65;

    private static final int COLUMNS_LEVEL_EASY = 3; // 3x4
    private static final int COLUMNS_LEVEL_MEDIUM = 4; // 4x4
    private static final int COLUMNS_LEVEL_HARD = 4; // 4x5

    private final HashMap<String, String> glossary = new HashMap<>();
    private int level = LEVEL_NOT_SET;
    private int cards;
    private int maxLength;
    private int columns;
    private int buttonHeight;
    private final Random randomGenerator = new Random();

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_memory);

        RadioGroup difficulty = findViewById(R.id.difficultySelection);
        difficulty.setOnCheckedChangeListener((radioGroup, selection) -> {
            switch(selection) {
                case R.id.easy:
                    level = LEVEL_EASY;
                    break;
                case R.id.medium:
                    level = LEVEL_MEDIUM;
                    break;
                case R.id.hard:
                    level = LEVEL_HARD;
                    break;
            }
        });

        Button buttonStart = findViewById(R.id.btnStart);
        buttonStart.setOnClickListener(view -> {
            switch (level) {
                case LEVEL_NOT_SET:
                    // User hasn't chosen difficulty yet, nothing happens.
                    return;
                case LEVEL_EASY:
                    cards = CARDS_LEVEL_EASY;
                    maxLength = MAX_LENGTH_LEVEL_EASY;
                    columns = COLUMNS_LEVEL_EASY;
                    break;
                case LEVEL_MEDIUM:
                    cards = CARDS_LEVEL_MEDIUM;
                    maxLength = MAX_LENGTH_LEVEL_MEDIUM;
                    columns = COLUMNS_LEVEL_MEDIUM;
                    break;
                case LEVEL_HARD:
                    cards = CARDS_LEVEL_HARD;
                    maxLength = MAX_LENGTH_LEVEL_HARD;
                    columns = COLUMNS_LEVEL_HARD;
                    break;
            }
            int height = getDisplayContentHeight();
            int rows = cards / columns;
            buttonHeight = height / rows;

            ConnectionHandler connectionHandler = new ConnectionHandler(Memory.this);
            ConnectionHandlerUtils connectionHandlerUtils = new ConnectionHandlerUtils(connectionHandler);
            HashMap<String, String> fullGlossary =
                    connectionHandlerUtils.getFilteredGlossaryMapTermToDef(maxLength);

            ArrayList<String> keys = new ArrayList<>(fullGlossary.keySet());

            // Generating a random glossary subset for current game.
            while (2 * glossary.size() != cards) {
                String key = keys.get(randomGenerator.nextInt(keys.size()));
                while (glossary.containsKey(key)) {
                    key = keys.get(randomGenerator.nextInt(keys.size()));
                }
                glossary.put(key, fullGlossary.get(key));
            }

            Intent intent = new Intent(Memory.this, MemoryGame.class);
            intent.putExtra("glossary", glossary);
            intent.putExtra("columns", columns);
            intent.putExtra("buttonHeight", buttonHeight);
            startActivity(intent);
        });
    }

    private int getNavigationBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    public int getDisplayContentHeight() {
        final WindowManager windowManager = getWindowManager();
        final Point size = new Point();
        int actionBarHeight =  getNavigationBarHeight();
        int contentTop = findViewById(android.R.id.content).getTop();
        windowManager.getDefaultDisplay().getSize(size);
        int screenHeight = size.y;
        return screenHeight - contentTop - actionBarHeight;
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
                Intent settings_intent = new Intent(Memory.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(Memory.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}