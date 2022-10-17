package flashcards;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.technetapps.english_in_it.R;
import com.technetapps.english_in_it.ConnectionHandler;
import com.technetapps.english_in_it.ConnectionHandlerUtils;
import com.technetapps.english_in_it.Settings;
import com.technetapps.english_in_it.Utils;

import activities_menu.StartListActivity;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Set;

/**
 * A class handling the flashcard cardview.
 * Implements prev/next buttons and card "flipping" (switching text from term to def).
 */
public class Flashcards extends AppCompatActivity {
    private ConnectionHandler connection_handler;
    private Button next_btn;
    private Button prev_btn;
    private CardView flashcard;
    private TextView flashcard_txt;
    private TextView progress_txt;
    int current_flashcard = 1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_flashcards);
        flashcard = findViewById(R.id.flashcard);
        next_btn = findViewById(R.id.btnNextFlashcard);
        prev_btn = findViewById(R.id.btnPrevFlashcard);
        flashcard_txt = findViewById(R.id.termOrDefTxt);
        progress_txt = findViewById(R.id.progressNumberTxt);

        connection_handler = new ConnectionHandler(Flashcards.this);
        ConnectionHandlerUtils connection_handler_utils = new ConnectionHandlerUtils(connection_handler);
        HashMap<String, String> glossary;

        // get data from flashcards options - selected set and def to term/term to def
        Bundle flashcards_bundle = getIntent().getExtras();
        Boolean defToTerm = flashcards_bundle.getBoolean("defToTerm");
        String selectedSet = flashcards_bundle.getString("selectedSet");

        if (defToTerm) {
            glossary = connection_handler_utils.getGlossaryMapTermToDef(0);
            if (!selectedSet.equals("All terms")) {
                try {
                    glossary = connection_handler_utils.getSetGlossaryMapDefToTerm(selectedSet);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(Flashcards.this, "definition to term", Toast.LENGTH_SHORT).show();
        }
        else {
            glossary = connection_handler_utils.getGlossaryMapTermToDef(1);
            if (!selectedSet.equals("All terms")) {
                try {
                    glossary = connection_handler_utils.getSetGlossaryMapTermToDef(selectedSet, 1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(Flashcards.this, "term to definition", Toast.LENGTH_SHORT).show();
        }

        Set<String> keys = glossary.keySet();
        Integer cards_number = keys.size();
        String[] front_sides = keys.toArray(new String[cards_number]);

        progress_txt.setText("1/" + cards_number.toString());
        flashcard_txt.setText(front_sides[current_flashcard - 1]);
        HashMap<String, String> finalGlossary = glossary;

        final boolean[] front = {true}; // niezbyt rozumiem czemu tak dziwnie musi być, ale intellij zmienił ze zwykłego boola xd
        flashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            //tu bedzie flip jeśli zachce mi sie go zrobić
            public void onClick(View view) {
                if (front[0]) {
                    flashcard_txt.setText(finalGlossary.get(front_sides[current_flashcard - 1]));
                } else {
                    flashcard_txt.setText(front_sides[current_flashcard - 1]);
                }
                front[0] = !front[0];
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_flashcard != cards_number) {
                    current_flashcard +=1;

                    flashcard_txt.setText(front_sides[current_flashcard - 1]);
                    progress_txt.setText(current_flashcard + "/" + cards_number.toString());
                    front[0] = true;
                }
            }
        });

        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_flashcard != 1) {
                    current_flashcard -=1;

                    flashcard_txt.setText(front_sides[current_flashcard - 1]);
                    progress_txt.setText(current_flashcard + "/" + cards_number.toString());
                    front[0] = true;
                }
            }
        });
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
                Intent settings_intent = new Intent(Flashcards.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(Flashcards.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}