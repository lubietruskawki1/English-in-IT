package learning_sets;

import activities_menu.StartListActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.technetapps.english_in_it.ChooseWordsToLearn;
import com.technetapps.english_in_it.ConnectionHandler;
import com.technetapps.english_in_it.ConnectionHandlerUtils;
import com.technetapps.english_in_it.R;
import com.technetapps.english_in_it.Settings;
import com.technetapps.english_in_it.TermAndDef;
import com.technetapps.english_in_it.Utils;
import com.technetapps.english_in_it.VocabularyRecViewAdapter;
import com.technetapps.english_in_it.Word;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Shows content of user-chosen set and allows its modification.
 */
public class EditSet extends AppCompatActivity {
    private TextView set_name_view;
    private RecyclerView vocabulary_view;
    private VocabularyRecViewAdapter adapter;

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
        ConnectionHandlerUtils connection_handler_utils = new ConnectionHandlerUtils(handler);
        ArrayList<Word> words;
        try {
            words = connection_handler_utils.getLearningSetList(set_name);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        ArrayList<TermAndDef> vocabulary = new ArrayList<>();
        for (Word word : words) {
            vocabulary.add(new TermAndDef(word.getWord(), word.getMeaning()));
        }

        vocabulary_view = findViewById(R.id.words_list);
        adapter = new VocabularyRecViewAdapter(this);
        adapter.allowDelete(connection_handler_utils, set_name);
        adapter.setVocabulary(vocabulary);
        vocabulary_view.setAdapter(adapter);
        vocabulary_view.setLayoutManager(new LinearLayoutManager(this));
        vocabulary_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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