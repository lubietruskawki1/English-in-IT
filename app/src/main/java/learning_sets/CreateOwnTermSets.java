package learning_sets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.technetapps.english_in_it.ConnectionHandler;
import com.technetapps.english_in_it.ConnectionHandlerUtils;
import com.technetapps.english_in_it.R;
import com.technetapps.english_in_it.Settings;
import com.technetapps.english_in_it.Utils;

import java.util.ArrayList;

import activities_menu.StartListActivity;

/**
 * Lists user's sets and allows their modification.
 */
public class CreateOwnTermSets extends AppCompatActivity {
    private RecyclerView sets_view;
    private Button new_set_btn;
    private TextView learning_sets_number;
    private TextView new_set_txt;
    private EditText new_set_name;
    private boolean new_set_clicked = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_create_own_term_sets);

        new_set_btn = findViewById(R.id.new_set_btn);
        learning_sets_number = findViewById(R.id.sets_number_txt);
        new_set_name = findViewById(R.id.new_set_name_edit);
        new_set_txt = findViewById(R.id.new_set_txt);

        ConnectionHandler handler = new ConnectionHandler(this);
        ConnectionHandlerUtils connection_handler_utils = new ConnectionHandlerUtils(handler);

        ArrayList<Set> user_sets = connection_handler_utils.getAllLearningSets();
        ArrayList<String> empty_sets = connection_handler_utils.getEmptySets();

        for (String set_name : empty_sets) {
            user_sets.add(new Set(set_name, 0));
        }

        if (user_sets.size() == 1) {
            learning_sets_number.setText("You have " + user_sets.size() + " learning set.");
        } else {
            learning_sets_number.setText("You have " + user_sets.size() + " learning sets.");
        }

        new_set_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (!new_set_clicked) {
                    new_set_btn.setText("OK");
                    new_set_txt.setVisibility(View.GONE);
                    new_set_name.setVisibility(View.VISIBLE);
                    new_set_clicked = true;
                }
                else {
                    String create_set_name = new_set_name.getText().toString();
                    boolean already_exists = false;
                    ArrayList<String> sets = connection_handler_utils.getAllLearningSetNames();
                    for(int i = 0; i < sets.size(); i++) {
                        if(create_set_name.equals(sets.get(i))) {
                            already_exists = true;
                        }
                    }
                    if (create_set_name.equals(""))
                        Toast.makeText(CreateOwnTermSets.this, "Enter new set name.", Toast.LENGTH_SHORT).show();
                    else if(already_exists) {
                        Toast.makeText(CreateOwnTermSets.this, "Already exists.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        connection_handler_utils.newLearningSet(create_set_name);
                        new_set_name.setVisibility(View.GONE);
                        new_set_txt.setVisibility(View.VISIBLE);
                        new_set_btn.setText("+");
                        new_set_clicked = false;

                        finish();
                        startActivity(getIntent());
                    }
                }
            }
        });

        sets_view = findViewById(R.id.sets_recycler_view);
        SetListRecViewAdapter adapter = new SetListRecViewAdapter(this, connection_handler_utils);
        adapter.setItems(user_sets);
        sets_view.setAdapter(adapter);
        sets_view.setLayoutManager(new LinearLayoutManager(this));
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
                Intent settings_intent = new Intent(CreateOwnTermSets.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(CreateOwnTermSets.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}