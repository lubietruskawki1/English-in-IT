package comet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.technetapps.english_in_it.ConnectionHandler;
import com.technetapps.english_in_it.ConnectionHandlerUtils;
import com.technetapps.english_in_it.R;
import com.technetapps.english_in_it.Settings;
import com.technetapps.english_in_it.Utils;

import activities_menu.StartListActivity;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Falling Comet game and, its starting and winning screen.
 */
public class FallingCometGame extends AppCompatActivity {
    private ConnectionHandler connection_handler;
    private ImageView cometImg;
    private Button submitButton;
    private Button playAgainButton;
    private EditText answerEdtTxt;
    private TextView fallingDefinition;
    private TextView gameOverTxt;
    private TextView scoreTxt;
    private static boolean lost = false;
    private HashMap<String, String> glossary;
    private static int score = 0;
    private static int definition_index = 0;
    private final int falling_time = 20000;
    private AtomicBoolean guessed;

    ObjectAnimator fallingCometAnimation(View view, int height) {
        view.setVisibility(View.VISIBLE);
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationY", height + 1100);
        animation.setDuration(falling_time);
        return animation;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        overridePendingTransition(0, 0);
    }

    // Make unnecessary elements invisible and clear score.
    private void finish_game() {
        submitButton.setVisibility(View.GONE);
        answerEdtTxt.setVisibility(View.GONE);
        fallingDefinition.setVisibility(View.GONE);
        cometImg.setVisibility(View.GONE);
        if (guessed.get()) gameOverTxt.setText("You guessed all terms!");
        gameOverTxt.setVisibility(View.VISIBLE);
        playAgainButton.setVisibility(View.VISIBLE);
        String scoreInfo = "Your Score: " + score;
        lost = true;
        scoreTxt.setText(scoreInfo);
        scoreTxt.setVisibility(View.VISIBLE);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(0,0);
                score = 0;
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
            }
        });
        // Show score.
        // finish();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_falling_comet_game);

        guessed = new AtomicBoolean(false);

        submitButton = findViewById(R.id.btnSubmitComet);
        playAgainButton = findViewById(R.id.btnPlayAgainComet);
        playAgainButton.setVisibility(View.INVISIBLE);
        answerEdtTxt = findViewById(R.id.AnswerEdtTxt);
        fallingDefinition = findViewById(R.id.fallingDefinition);
        scoreTxt = findViewById(R.id.scoreTxt);
        scoreTxt.setVisibility(View.INVISIBLE);
        gameOverTxt = findViewById(R.id.gameOverTxt);
        gameOverTxt.setVisibility(View.INVISIBLE);
        cometImg = findViewById(R.id.cometImg);
        cometImg.setRotation(-35);

        connection_handler = new ConnectionHandler(FallingCometGame.this);
        ConnectionHandlerUtils connection_handler_utils = new ConnectionHandlerUtils(connection_handler);

        Bundle flashcards_bundle = getIntent().getExtras();
        String selectedSet = flashcards_bundle.getString("selectedSet");

        if (!selectedSet.equals("All terms")) {
            try {
                glossary = connection_handler_utils.getSetGlossaryMapDefToTerm(selectedSet);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            glossary = connection_handler_utils.getGlossaryMapTermToDef(0);
        }

        Set<String> definitions_set = glossary.keySet();
        int definitions_number = definitions_set.size();
        String[] definitions = definitions_set.toArray(new String[definitions_number]);

        if (lost) definition_index = 0;
        fallingDefinition.setText(definitions[definition_index]);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        submitButton.setOnClickListener(v -> {
            String answer = answerEdtTxt.getText().toString();
            String definition = fallingDefinition.getText().toString();
            if (answer.equals(glossary.get(definition))) {
                lost = false;
                definition_index = (definition_index + 1);
                guessed.set(true);
                score++;
                Toast.makeText(getApplicationContext(),
                        "Correct! Your score: " + score, Toast.LENGTH_LONG).show();
                if (definition_index < definitions_number) {
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                }
                else finish_game();
            }
            else {
                Toast.makeText(getApplicationContext(), answer, Toast.LENGTH_LONG).show();
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(this::finish_game, falling_time);

        ObjectAnimator falling_comet = fallingCometAnimation(cometImg, height);
        falling_comet.start();
    }

    public void onBackPressed() {
        score = 0;
        lost = true;
        finish();
        overridePendingTransition(0, 0);
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
                score = 0;
                lost = true;
                Intent settings_intent = new Intent(FallingCometGame.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                score = 0;
                lost = true;
                Intent home_intent = new Intent(FallingCometGame.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}