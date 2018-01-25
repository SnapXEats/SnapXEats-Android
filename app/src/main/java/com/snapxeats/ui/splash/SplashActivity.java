package com.snapxeats.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.snapxeats.R;
import com.snapxeats.ui.login.LoginActivity;
import com.snapxeats.ui.preferences.PreferenceActivity;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

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
            //check if facebook user is logged in or not
            if (AccessToken.getCurrentAccessToken() == null) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                startActivity(new Intent(this, PreferenceActivity.class));
            }
        }, TIME_OUT);

        checkForUpdates();


    }


    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }

}
