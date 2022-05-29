package com.example.english_in_it;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MemoryWin extends AppCompatActivity {
    private String score;
    private TextView txtScore;
    private Button buttonReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_win); // todo: lepszy obrazek xd
        txtScore = findViewById(R.id.txtScore);

        Bundle extras = getIntent().getExtras();
        score = extras.getString("playerScore");

        txtScore.setText(score);

        // todo: play again
        buttonReturn = findViewById(R.id.btnReturn);
        buttonReturn.setOnClickListener(view -> {
            Intent intent = new Intent(MemoryWin.this, StartListActivity.class);
            startActivity(intent);
        });
    }
}
