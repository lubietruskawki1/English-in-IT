package flashcards;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.technetapps.english_in_it.R;
import com.technetapps.english_in_it.ConnectionHandler;
import com.technetapps.english_in_it.ConnectionHandlerUtils;
import com.technetapps.english_in_it.Settings;
import com.technetapps.english_in_it.Utils;

import activities_menu.StartListActivity;
import learning_sets.Set;

import java.util.ArrayList;

/**
 *  A class handling flashcards options:
 *  - choosing a learning style: term to definition/definition to term
 *  - choosing a learning set
 */
public class FlashcardsOptions extends AppCompatActivity {
    private boolean defToTerm = true;
    private String selectedSet;
    private RadioGroup howToLearn;
    private Spinner setSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_flashcards_options);

        setSpinner = findViewById(R.id.setSpinner);
        howToLearn = findViewById(R.id.termDefinitionChoice);
        howToLearn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.defToTerm:
                        defToTerm = true;
                        break;
                    case R.id.termToDef:
                        defToTerm = false;
                        break;
                }
            }
        });

        ConnectionHandler c = new ConnectionHandler(this);
        ConnectionHandlerUtils connection_handler_utils = new ConnectionHandlerUtils(c);
        ArrayList<Set> setsAndTermNumbers = connection_handler_utils.getAllLearningSets();
        setsAndTermNumbers.add(new Set("All terms", 282));
        ArrayList<String> sets = new ArrayList<>();
        for (Set set : setsAndTermNumbers) {
            sets.add(set.getName() + " (" + set.getTerms_number() + " terms)");
        }

        ArrayAdapter<String> setsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                sets
        );

        setSpinner.setAdapter(setsAdapter);
        setSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSet = setsAndTermNumbers.get(i).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        Button goToFlashcardsButton = findViewById(R.id.btnFlashcards);
        goToFlashcardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FlashcardsOptions.this, Flashcards.class);

                Bundle flashcards_bundle = new Bundle();
                flashcards_bundle.putBoolean("defToTerm", defToTerm);
                flashcards_bundle.putString("selectedSet", selectedSet);
                intent.putExtras(flashcards_bundle);

                startActivity(intent);
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
                Intent settings_intent = new Intent(FlashcardsOptions.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(FlashcardsOptions.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}