package activities_menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.technetapps.english_in_it.R;
import com.technetapps.english_in_it.Settings;
import com.technetapps.english_in_it.Utils;

import java.util.ArrayList;

/**
 * Main menu screen.
 */
public class StartListActivity extends AppCompatActivity {
    private RecyclerView main_list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_main_menu);

        main_list_view = findViewById(R.id.mainListRecView);

        final ArrayList<String> menu_elements = new ArrayList<>();
        menu_elements.add("Browse vocabulary");
        menu_elements.add("Manage learning sets");
        menu_elements.add("Daily repetitions");
        menu_elements.add("Learn with flashcards");
        menu_elements.add("Play memory");
        menu_elements.add("Play falling comet");
        menu_elements.add("Do typing exercise");
        menu_elements.add("Set course preferences");

        ListRecViewAdapter adapter = new ListRecViewAdapter(this);
        adapter.setItems(menu_elements);
        main_list_view.setAdapter(adapter);

        main_list_view.setLayoutManager(new GridLayoutManager(this, 2));
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
                Intent settings_intent = new Intent(StartListActivity.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(StartListActivity.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}