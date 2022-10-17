package com.technetapps.english_in_it;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.technetapps.english_in_it.R;

import activities_menu.StartListActivity;

/**
 * Class implements starting screen of application.*/

public class MainActivity extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        String theme = pref.getString("theme", null);
        if (theme == null) {
            editor.putString("theme", "LightTheme");
            setTheme(R.style.LightTheme);
            editor.commit();
        } else {
            setTheme(Utils.getTheme(theme));
        }
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.btnStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StartListActivity.class);
                startActivity(intent);
            }
        });
    }
}