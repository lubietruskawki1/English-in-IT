package comet;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.technetapps.english_in_it.ConnectionHandler;
import com.technetapps.english_in_it.ConnectionHandlerUtils;
import com.technetapps.english_in_it.R;
import com.technetapps.english_in_it.Settings;
import activities_menu.StartListActivity;
import learning_sets.Set;

import com.technetapps.english_in_it.Utils;

import java.util.ArrayList;

/**
 * Timer from the Falling Comet game.
 */
public class CometTimerActivity extends AppCompatActivity {
    private int counter;
    private String selectedSet;
    private Spinner setSpinner;
    Button startPlayingBtn;
    TextView countdownView;
    TextView chooseSetTxt;

    ObjectAnimator fallingCometAnimation(View view) {
        view.setVisibility(View.VISIBLE);
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationY", 3200f);
        animation.setDuration(3000);
        return animation;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        this.recreate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_comet_timer);
        setSpinner = findViewById(R.id.cometSetSpinner);
        startPlayingBtn = findViewById(R.id.btnPlayComet);
        countdownView = findViewById(R.id.countdownTxtView);
        chooseSetTxt = findViewById(R.id.cometChooseSetTxt);

        ConnectionHandler c = new ConnectionHandler(this);
        ConnectionHandlerUtils connection_handler_utils = new ConnectionHandlerUtils(c);
        ArrayList<Set> setsAndTermNumbers = connection_handler_utils.getAllLearningSets();
        setsAndTermNumbers.add(new Set("All terms", 282));

        ArrayList<String> sets = new ArrayList<>();
        for (Set set : setsAndTermNumbers) {
            sets.add(set.getName() + " (" + set.getTerms_number() + " terms)");
        }

        ArrayAdapter<String> setsAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item_comet,
                sets
        );

        setSpinner.setAdapter(setsAdapter);
        setSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSet = setsAndTermNumbers.get(i).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        startPlayingBtn.setOnClickListener(v -> {
            startPlayingBtn.setVisibility(View.GONE);
            setSpinner.setVisibility(View.GONE);
            chooseSetTxt.setVisibility(View.GONE);
            counter = 3;
            new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
                    countdownView.setText(String.valueOf(counter));
                    counter--;
                }

                public void onFinish() {
                    countdownView.setText(R.string.start_txt);
                    new CountDownTimer(1000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {}

                        @Override
                        public void onFinish() {

                            Intent intent = new Intent(CometTimerActivity.this, FallingCometGame.class);
                            Bundle flashcards_bundle = new Bundle();
                            flashcards_bundle.putString("selectedSet", selectedSet);
                            intent.putExtras(flashcards_bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }.start();
                }
            }.start();
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
                Intent settings_intent = new Intent(CometTimerActivity.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(CometTimerActivity.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}