package memory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.technetapps.english_in_it.R;
import com.technetapps.english_in_it.Settings;
import activities_menu.StartListActivity;
import com.technetapps.english_in_it.Utils;

/**
 * Winning screen of Memory game.
 */
public class MemoryWin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_memory_win);

        TextView txtScore = findViewById(R.id.txtScore);
        Bundle extras = getIntent().getExtras();
        String score = extras.getString("score") + " / 100";
        txtScore.setText(score);

        Button buttonPlayAgain = findViewById(R.id.btnPlayAgain);
        buttonPlayAgain.setOnClickListener(view -> {
            Intent intent = new Intent(MemoryWin.this, Memory.class);
            startActivity(intent);
        });

        Button buttonReturn = findViewById(R.id.btnReturn);
        buttonReturn.setOnClickListener(view -> {
            Intent intent = new Intent(MemoryWin.this, StartListActivity.class);
            startActivity(intent);
        });
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
                Intent settings_intent = new Intent(MemoryWin.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(MemoryWin.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
