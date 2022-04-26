package com.example.english_in_it;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class StartListActivity extends AppCompatActivity {

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


        ArrayAdapter<String> menu_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, menu_elements);

        menuList.setAdapter(menu_adapter);


    }
}