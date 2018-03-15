package com.snapxeats;

import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.snapxeats.common.Router;
import com.snapxeats.common.utilities.SnapXDialog;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import dagger.android.DaggerFragment;

/**
 * Created by Snehal Tembare on 16/2/18.
 */

public class BaseFragment extends DaggerFragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "BaseFragment";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    @Inject
    Router router;

    @Inject
    SnapXDialog mSnapXDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSnapXDialog.setContext(getActivity());
        router.setActivity(getActivity());
    }

    public void showProgressDialog() {
        mSnapXDialog.createProgressDialog();
    }

    public void dismissProgressDialog() {
        mSnapXDialog.dismissProgressSialog();
    }

    public void showResetDialog(DialogInterface.OnClickListener negativeClick,
                                DialogInterface.OnClickListener positiveClick) {
        mSnapXDialog.showResetDialog(negativeClick, positiveClick);
    }

    public void showNetworkErrorDialog(DialogInterface.OnClickListener click) {
        mSnapXDialog.showNetworkErrorDialog(click);
    }

    public void buildGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
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
//        Address locationAddress = getAddress(location.getLatitude(), location.getLongitude());

        if (locationAddress != null) {

            if (locationAddress.getSubLocality() != null) {
                placeName = locationAddress.getSubLocality();
            } else if (locationAddress.getThoroughfare() != null) {
                placeName = locationAddress.getThoroughfare();
            }
        }
        return placeName;

    }


    public Address getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
