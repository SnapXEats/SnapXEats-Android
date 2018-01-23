package com.snapxeats.ui.location;

import android.content.IntentSender;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;

import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.snapxeats.network.NetworkHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationActivity extends BaseActivity {

    private static final String TAG = "LocationActivity";
    private static final int REQUEST_CHECK_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);

        SharedPreferences preferences = getSharedPreferences("SnapXEats", MODE_PRIVATE);
        SharedPreferences.Editor  editor = preferences.edit();
        editor.putBoolean("isLocationPermissionDenied", true);
        editor.apply();

    }

    @OnClick(R.id.edt_detect_my_location)
    public void detectLocation() {
    }

    @OnClick(R.id.img_close)
    public void close() {
        finish();
    }
}
