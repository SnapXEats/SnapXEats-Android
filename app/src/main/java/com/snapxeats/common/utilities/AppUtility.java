package com.snapxeats.common.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.snapxeats.R;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.SnapxDataDao;
import com.snapxeats.common.model.foodGestures.DaoSession;
import com.snapxeats.common.model.location.Location;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 31/1/18.
 */

@Singleton
public class AppUtility implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context mContext;
    private DaoSession daoSession;
    private SnapxDataDao snapxDataDao;
    private SnapxData snapxData;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mLastLocation;

    @Inject
    public AppUtility() {
    }

    public void setContext(Context context) {
        this.mContext = context;
        daoSession = ((SnapXApplication) context.getApplicationContext()).getDaoSession();
        snapxDataDao = daoSession.getSnapxDataDao();
        if (snapxDataDao.loadAll() != null && snapxDataDao.loadAll().size() > 0) {
            snapxData = snapxDataDao.loadAll().get(0);
        }
    }

    public SharedPreferences getSharedPreferences() {
        preferences = mContext.getSharedPreferences(mContext.getString(R.string.preference_name),
                mContext.MODE_PRIVATE);
        return preferences;
    }

    public void saveObjectInPref(Location location, String key) {
        editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(location);
        editor.putString(key, json);
        editor.apply();
    }

    public String getAuthToken(final Context context) {
        String token = null;
        SnapXApplication app = (SnapXApplication) context.getApplicationContext();
        if (null != app) {
            token = app.getToken();
            if (null != token && !token.isEmpty()) {
                return String.format("Bearer %s", token);
            } else {
                //TODO: fetch it from DB, assign it to app.token & return that token
                if (snapxData != null) {
                    token = snapxData.getToken(); // fetch it from DB}
                    app.setToken(token);
                }
                return String.format("Bearer %s", token);
            }
        }
        return token;
    }

    public void setAuthToken(final Context context) {
        String token = null;
        SnapXApplication app = (SnapXApplication) context.getApplicationContext();
        if (null != app) {
            token = app.getToken();
            if (null != token && !token.isEmpty()) {
                //TODO: fetch it from DB, assign it to app.token & return that token
                if (snapxData != null && !snapxData.getToken().isEmpty()) {
                    token = snapxData.getToken(); // fetch it from DB
                }
                app.setToken(token);
            }
        }
    }

    public void hideKeyboard() {
              View view = ((Activity) mContext).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void buildGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(locationSettingsResult -> {

            final Status status = locationSettingsResult.getStatus();

            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can initialize location requests here
                    getLocation();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    break;
            }
        });

    }

    public android.location.Location getLocation() {
        try {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return mLastLocation != null ? mLastLocation : null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        SnapXToast.error("Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }
}
