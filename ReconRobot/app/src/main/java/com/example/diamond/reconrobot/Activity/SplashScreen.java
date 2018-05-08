package com.example.diamond.reconrobot.Activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.diamond.reconrobot.Model.Contextor;
import com.example.diamond.reconrobot.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Contextor.getInstance().init(getApplicationContext());
        new CountDownTimer(2000, 1000) { //5 seconds
            public void onTick(long millisUntilFinished) {
                Log.d("DSS","Time = "+millisUntilFinished / 1000);
            }
            public void onFinish() {
                startActivity(new Intent(SplashScreen.this, MenuActivity.class));
                finish();
            }

        }.start();
    }
}
