package learning_sets;

import activities_menu.StartListActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.english_in_it.*;

import java.text.ParseException;
import java.util.ArrayList;

public class EditSet extends AppCompatActivity {
    private TextView set_name_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_edit_set);

        Bundle editSetBundle = getIntent().getExtras();
        String set_name = editSetBundle.getString("set_name");

        set_name_view = findViewById(R.id.set_name_edit_set);
        set_name_view.setText(set_name);

        Button addWordsButton = findViewById(R.id.add_words_btn);
        addWordsButton.setOnClickListener(view -> {
            Intent intent = new Intent(EditSet.this, ChooseWordsToLearn.class);
            intent.putExtra("set_name", set_name);
            startActivity(intent);
        });

        Button finishButton = findViewById(R.id.finish_btn); // TODO: nie jestem pewna, czy on powinien prowadzić do setów czy menu ?
        finishButton.setOnClickListener(view -> {
            Intent intent = new Intent(EditSet.this, CreateOwnTermSets.class);
            startActivity(intent);
        });

        ConnectionHandler handler = new ConnectionHandler(this);
        ArrayList<Word> words;
        try {
            words = handler.getLearningSetList(set_name);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> strings = new ArrayList<>();
        for (Word word : words) {
            strings.add(word.getWord() + ": " + word.getMeaning());
        }

        ListView editSetList = findViewById(R.id.words_list);
        ArrayAdapter<String> editSetAdapter = new ArrayAdapter<>(
                EditSet.this, android.R.layout.simple_list_item_1, strings);
        editSetList.setAdapter(editSetAdapter);

        editSetList.setOnItemClickListener((parent, view, position, id) -> {
            String chosen = (String) parent.getItemAtPosition(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String term = chosen.split(":")[0];
            builder.setMessage("Are you sure you want to delete word " + term + " from set?")
                    .setPositiveButton("YES", (dialogInterface, i) -> {
                        Toast.makeText(this, "Deleted word " + term, Toast.LENGTH_SHORT).show();
                        handler.deleteWordFromLearningSet(term, set_name);
                        startActivity(getIntent());
                    })
                    .setNegativeButton("NO", (dialogInterface, i) -> {
                        Toast.makeText(this, "Cancelled.", Toast.LENGTH_SHORT).show();
                    });
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu:
                Intent settings_intent = new Intent(EditSet.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(EditSet.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}