package com.technetapps.english_in_it;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.technetapps.english_in_it.R;

import java.util.ArrayList;

import activities_menu.StartListActivity;

/**
 * Settings (course preferences) screen.
 * @param[in] theme : user-selected app theme (as of now: light [default] / dark)
 */
public class Settings extends AppCompatActivity {
    private RecyclerView settings_list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_settings);

        settings_list_view = findViewById(R.id.settingsListRecView);

        final ArrayList<String> menu_elements = new ArrayList<>();
        menu_elements.add("Light theme");
        menu_elements.add("Dark theme");

        SettingsListRecViewAdapter adapter = new SettingsListRecViewAdapter(this);
        adapter.setItems(menu_elements);

        settings_list_view.setAdapter(adapter);
        settings_list_view.setLayoutManager(new GridLayoutManager(this, 2));
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
                Intent settings_intent = new Intent(Settings.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(Settings.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
