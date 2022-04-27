package com.example.english_in_it;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BrowseVocabulary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_vocabulary);

        ConnectionHandler connection_handler = new ConnectionHandler();
        connection_handler.HandleConnection();
        Connection c = connection_handler.getConnection();
    }
}