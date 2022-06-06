package learning_sets;

import activities_menu.StartListActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.english_in_it.*;
import memory.MemoryGame;
import memory.MemoryWin;

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

        Button finishButton = findViewById(R.id.finish_btn); // TODO: nie jestem pewna, czy on powiniem prowadzić do setów czy menu ?
        finishButton.setOnClickListener(view -> {
            Intent intent = new Intent(EditSet.this, CreateOwnTermSets.class);
            startActivity(intent);
        });

        ConnectionHandler handler = new ConnectionHandler(this);
        ArrayList<Word> words = new ArrayList<>();
        try {
            words = handler.getLearningSetList(set_name);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> strings = new ArrayList<>();
        for (Word word : words) {
            strings.add(word.getWord() + ": " + word.getMeaning());
        }

        ListView browseVocabularyList = findViewById(R.id.words_list);
        ArrayAdapter<String> browseVocabularyAdapter = new ArrayAdapter<>(
                EditSet.this, android.R.layout.simple_list_item_1, strings);

        browseVocabularyList.setAdapter(browseVocabularyAdapter);
    }
}