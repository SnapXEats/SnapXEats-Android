package com.snapxeats.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.AccessToken;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.ui.login.LoginActivity;
import com.snapxeats.ui.preferences.PreferenceActivity;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by Snehal Tembare on 12/1/18.
 */

public class SplashActivity extends BaseActivity {
    private final int TIME_OUT = 1000;
    private LocationManager mLocationManager;
    private SharedPreferences preferences;

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snapXDialog.setContext(this);
        utility.setContext(this);

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        checkForUpdates();
        preferences = utility.getSharedPreferences();

        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName("City Pride Kothrud, Pune, Maharashtra, India",1);
            if (address!=null) {
                SnapXToast.debug("***Lat"+address.get(0).getLatitude());
                SnapXToast.debug("**Longitude"+address.get(0).getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


            new Handler().postDelayed(() -> {
                //check if facebook user is logged in or not
                if (AccessToken.getCurrentAccessToken() == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(this, PreferenceActivity.class));
                }
            }, TIME_OUT);

    }

    /**
     * Check provider is enable or not
     */
    private void checkGpsPermission() {
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            snapXDialog.showGpsPermissionDialog();
        }
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
