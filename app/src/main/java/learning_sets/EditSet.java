package learning_sets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.english_in_it.ConnectionHandler;
import com.example.english_in_it.R;
import com.example.english_in_it.Utils;
import com.example.english_in_it.Word;

import java.text.ParseException;
import java.util.ArrayList;

public class EditSet extends AppCompatActivity {
    private TextView set_name_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_edit_set);

        Bundle editSetBundle = getIntent().getExtras();
        String set_name = editSetBundle.getString("set_name");

        set_name_view = findViewById(R.id.set_name_edit_set);
        set_name_view.setText(set_name);

        ConnectionHandler handler = new ConnectionHandler(this);
        try {
            ArrayList<Word> words = handler.getLearningSetList(set_name);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}