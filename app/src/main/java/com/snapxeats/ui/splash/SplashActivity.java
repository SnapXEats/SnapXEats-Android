package com.snapxeats.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.snapxeats.R;
import com.snapxeats.ui.login.LoginActivity;

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
