package com.example.synerzip.snapxeats.ui.preferences;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.example.synerzip.snapxeats.common.utilities.NetworkUtility;
import com.example.synerzip.snapxeats.network.NetworkHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class PreferenceInteractor {

    private PreferenceContract.PreferencePresenter preferencePresenter;

    private Activity context;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;

    @Inject
    public PreferenceInteractor() {

    }

    public void setPreferencePresenter(PreferenceContract.PreferencePresenter presenter) {
        this.preferencePresenter = presenter;
    }


    private void locationHelper(PreferenceContract.PreferenceView preferenceView) {
        preferenceView.showProgressDialog();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        if (mFusedLocationProviderClient != null && !NetworkHelper.checkPermission(context)) {
            //Network check is a duplicate call but it is required for LocationServices
            // and we need to move it out  in separate method that why in second call it will return true always
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

            if (locationResult != null) {
                locationResult.addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task != null) {
                        mLastKnownLocation = task.getResult();
                        if (mLastKnownLocation != null) {
                            preferencePresenter.setLocation(mLastKnownLocation);
                            preferenceView.dismissProgressDialog();
                            Geocoder geocoder = new Geocoder(context);
                            try {

                                List<Address> addresses = geocoder.getFromLocation(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude(), 1);

                                if (addresses.get(0).getSubLocality() != null) {
                                    preferencePresenter.updatePlace(addresses.get(0).getSubLocality());
                                } else if (addresses.get(0).getThoroughfare() != null) {
                                    preferencePresenter.updatePlace(addresses.get(0).getThoroughfare());

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }

    }

    /**
     * Get user current location
     */

    public void getLocation(PreferenceContract.PreferenceView preferenceView) {
        context = preferenceView.getActivity();
        if (context != null &&
                NetworkUtility.isNetworkAvailable(context)) {
            if (NetworkHelper.checkPermission(context)) {
                NetworkHelper.requestPermission(context);
            } else {
                locationHelper(preferenceView);
            }
        }else {
            preferenceView.showNetworkErrorDialog();
        }
    }

}
