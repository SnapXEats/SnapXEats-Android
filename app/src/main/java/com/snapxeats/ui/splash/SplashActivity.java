package com.snapxeats.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.model.DaoSession;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.SnapxDataDao;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.ui.home.HomeActivity;
import com.snapxeats.ui.login.LoginActivity;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.snapxeats.common.model.SnapxDataDao.Properties.UserId;

/**
 * Created by Snehal Tembare on 12/1/18.
 */

public class SplashActivity extends BaseActivity {
    private SnapxData snapxData;

    @Inject
    AppUtility appUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        checkForUpdates();

        DaoSession daoSession = ((SnapXApplication) getApplication()).getDaoSession();
        SnapxDataDao snapxDataDao = daoSession.getSnapxDataDao();
        appUtility.setContext(this);

        SharedPreferences settings = appUtility.getSharedPreferences();
        snapxData = snapxDataDao.queryBuilder()
                .where(UserId.eq(settings.getString(getString(R.string.pref_server_id), ""))).limit(1).unique();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
        int TIME_OUT = 1000;
        new Handler().postDelayed(() -> {
            //check if facebook user is logged in or not
            if (snapxData != null && !snapxData.getUserId().isEmpty()) {
                startActivity(new Intent(this, HomeActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
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
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }
}
