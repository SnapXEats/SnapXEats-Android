package com.snapxeats.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.SnapXData;
import com.snapxeats.common.model.SnapXDataDao;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.ui.home.HomeActivity;
import com.snapxeats.ui.login.LoginActivity;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.snapxeats.common.constants.UIConstants.ONE;
import static com.snapxeats.common.constants.UIConstants.TIME_SECONDS;
import static com.snapxeats.common.constants.UIConstants.ZERO;
import static com.snapxeats.common.model.SnapXDataDao.Properties.UserId;

/**
 * Created by Snehal Tembare on 12/1/18.
 */

public class SplashActivity extends BaseActivity {
    private SnapXData snapXData;

    @Inject
    AppUtility appUtility;

    @Inject
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        checkForUpdates();

        appUtility.setContext(this);
        dbHelper.setContext(this);

        SnapXDataDao snapxDataDao = dbHelper.getSnapxDataDao();

        SharedPreferences settings = appUtility.getSharedPreferences();
        if (null != snapxDataDao) {
            snapXData = snapxDataDao.queryBuilder().where(UserId.eq(settings.getString(getString(R.string.user_id), "")))
                    .limit(ONE).unique();
        }
    }

    public void checkLoginStatus() {
        new Handler().postDelayed(() -> {
            //check if facebook user is logged in or not
            List<SnapXData> snapXData = dbHelper.getSnapxDataDao().loadAll();
            if (ZERO < snapXData.size() && null != snapXData.get(ZERO).getUserId()) {
                startActivity(new Intent(this, HomeActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }, TIME_SECONDS);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
        checkLoginStatus();
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
