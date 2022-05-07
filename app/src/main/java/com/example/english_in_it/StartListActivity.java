package com.example.english_in_it;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StartListActivity extends AppCompatActivity {
    private RecyclerView main_list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //ListView menuList = findViewById(R.id.menuElementsList);
        main_list_view = findViewById(R.id.mainListRecView);

        final ArrayList<String> menu_elements = new ArrayList<>();
        menu_elements.add("Browse vocabulary");
        menu_elements.add("Choose words to learn");
        menu_elements.add("Learn with flashcards");
        menu_elements.add("Play memory");
        menu_elements.add("Play falling comet");
        menu_elements.add("Set course preferences");


        ListRecViewAdapter adapter = new ListRecViewAdapter(this);
        adapter.setItems(menu_elements);

        main_list_view.setAdapter(adapter);
        main_list_view.setLayoutManager(new GridLayoutManager(this, 2));

        /* ArrayAdapter<String> menu_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, menu_elements);

        menuList.setAdapter(menu_adapter);


        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(StartListActivity.this, "selected " + menu_elements.get(position), Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        Intent browse_intent = new Intent(StartListActivity.this, BrowseVocabulary.class);
                        startActivity(browse_intent);
                        break;
                    case 1:
                        break;
                    case 2:
                        Intent memory_intent = new Intent(StartListActivity.this, Memory.class);
                        startActivity(memory_intent);
                        break;
                    case 3:
                    case 4:
                }
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_menu, menu);
        return true;
    }
}