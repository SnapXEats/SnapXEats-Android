package com.snapxeats.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.AccessToken;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.model.RootInstagram;
import com.snapxeats.common.model.SnapXUser;
import com.snapxeats.ui.home.HomeActivity;
import com.snapxeats.ui.home.fragment.home.HomeFragment;
import com.snapxeats.ui.login.LoginActivity;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import butterknife.ButterKnife;

/**
 * Created by Snehal Tembare on 12/1/18.
 */

public class SplashActivity extends BaseActivity {

    private final int TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        checkForUpdates();
    }


    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();

        new Handler().postDelayed(() -> {
            //check if facebook user is logged in or not
            if (AccessToken.getCurrentAccessToken() == null /*&& rootInstagram.getData().getId()==null*/) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(this, HomeActivity.class));
            }
        }, TIME_OUT);

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
