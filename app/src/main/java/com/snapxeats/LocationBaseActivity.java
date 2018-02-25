package com.snapxeats;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.network.LocationHelper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 7/2/18.
 */

public class LocationBaseActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LocationBaseActivity";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    @Inject
    SnapXDialog snapXDialog;
    Geocoder geocoder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snapXDialog.setContext(this);
        geocoder = new Geocoder(this, Locale.getDefault());

    }

    public void buildGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean checkPermissions() {

        //Check device level location permission
        if (LocationHelper.isGpsEnabled(this)) {
            if (LocationHelper.checkPermission(this)) {
                LocationHelper.requestPermission(this);
            } else if (NetworkUtility.isNetworkAvailable(this)) {
                return true;
            } else {
                showNetworkErrorDialog((dialog, which) -> {
                });
                return false;
            }
        } else {
            checkGpsPermission();
        }
        return false;
    }

    /**
     * Show Enable Gps Permission dialog
     */
    public void checkGpsPermission() {
        if (!LocationHelper.isGpsEnabled(this)) {
            snapXDialog.showGpsPermissionDialog();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Once connected with google api, get the location
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        SnapXToast.debug("Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    /**
     * Method to display the location on UI
     */

    public Location getLocation() {
        try {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return mLastLocation != null ? mLastLocation : null;
    }

    public String getPlaceName(Location location) {
        String placeName = "";
        Address locationAddress = getAddress(location.getLatitude(), location.getLongitude());

        if (locationAddress != null) {

            if (locationAddress.getSubLocality() != null) {
                placeName = locationAddress.getSubLocality();
            } else if (locationAddress.getThoroughfare() != null) {
                placeName = locationAddress.getThoroughfare();
            }

            Log.i(TAG, "Address" + placeName);
        }
        return placeName;

    }

    public Address getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        SnapXToast.debug("Geocoder available:" + Geocoder.isPresent());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

