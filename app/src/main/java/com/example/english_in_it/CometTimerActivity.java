package com.example.english_in_it;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CometTimerActivity extends AppCompatActivity {
    public int counter;
    Button startPlayingBtn;
    TextView countdownView;

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
        setContentView(R.layout.activity_comet_timer);
        startPlayingBtn = findViewById(R.id.btnPlayComet);
        countdownView = findViewById(R.id.countdownTxtView);
        startPlayingBtn.setOnClickListener(v -> {
            startPlayingBtn.setVisibility(View.GONE);
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
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }.start();
                }
            }.start();
        });
    }
}