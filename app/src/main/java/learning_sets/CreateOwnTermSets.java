package learning_sets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.english_in_it.R;
import com.example.english_in_it.Settings;
import com.example.english_in_it.Utils;

import java.util.ArrayList;

import activities_menu.ListRecViewAdapter;
import activities_menu.StartListActivity;

public class CreateOwnTermSets extends AppCompatActivity {
    private RecyclerView sets_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_create_own_term_sets);

        final ArrayList<Set> example_sets = new ArrayList<>();
        example_sets.add(new Set("Pierwszy set", 5));
        example_sets.add(new Set("To jest set Agaty", 3));
        example_sets.add(new Set("POtezny secik", 69));

        sets_view = findViewById(R.id.sets_recycler_view);
        SetListRecViewAdapter adapter = new SetListRecViewAdapter(this);
        adapter.setItems(example_sets);
        sets_view.setAdapter(adapter);
        sets_view.setLayoutManager(new LinearLayoutManager(this));
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