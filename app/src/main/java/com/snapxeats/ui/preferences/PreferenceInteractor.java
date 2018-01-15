package com.snapxeats.ui.preferences;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.NetworkHelper;
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
    private Location mLastKnownLocation;

    @Inject
    public PreferenceInteractor() {

    }

    public void setPreferencePresenter(PreferenceContract.PreferencePresenter presenter) {
        this.preferencePresenter = presenter;
    }


    private void locationHelper(PreferenceContract.PreferenceView preferenceView) {
        preferenceView.showProgressDialog();
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        if (mFusedLocationProviderClient != null && !NetworkHelper.checkPermission(context)) {

            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

            if (locationResult != null) {
                locationResult.addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task != null) {
                        mLastKnownLocation = task.getResult();
                        if (mLastKnownLocation != null) {
                            //preferencePresenter.setLocation(mLastKnownLocation);
                            //preferenceView.dismissProgressDialog();
                            Geocoder geocoder = new Geocoder(context);
                            try {

                                List<Address> addresses = geocoder.getFromLocation(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude(), 1);
                                String place = "";
                                if (addresses.get(0).getSubLocality() != null) {
                                    place = addresses.get(0).getSubLocality();

                                } else if (addresses.get(0).getThoroughfare() != null) {
                                    place = addresses.get(0).getSubLocality();

                                }
                                preferencePresenter.updatePlace(place);
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
            preferencePresenter.response(SnapXResult.NONETWORK);
//            preferenceView.showNetworkErrorDialog(preferenceView.setListener(()->context.finish()));
        }
    }

}
