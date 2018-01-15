package com.example.synerzip.snapxeats.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.synerzip.snapxeats.R;
import com.example.synerzip.snapxeats.ui.location.LocationActivity;
import com.example.synerzip.snapxeats.ui.login.LoginActivity;

/**
 * Created by Snehal Tembare on 12/1/18.
 */

public class SplashActivity extends AppCompatActivity {
    private final int TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, TIME_OUT);
    }
}
