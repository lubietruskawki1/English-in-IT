package com.example.english_in_it;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class StartMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ListView menuList = findViewById(R.id.menuElementsList);

        final ArrayList<String> menu_elements = new ArrayList<>();
        menu_elements.add("Browse vocabulary");
        menu_elements.add("Learn with flashcards");
        menu_elements.add("Play memory");
        menu_elements.add("Play falling comet");
        menu_elements.add("Set course preferences");

        /*ArrayAdapter<String> menu_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, menu_elements) {

            @Override
            public View getView(int position, View convert_view, ViewGroup parent) {
                View view = super.getView(position, convert_view, parent);

                TextView text = findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);

                return view;
            }

        };*/

        ArrayAdapter<String> menu_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, menu_elements);

        menuList.setAdapter(menu_adapter);






    }
}