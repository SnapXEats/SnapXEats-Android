package com.example.synerzip.snapxeats.ui.preferences;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.synerzip.snapxeats.R;
import com.example.synerzip.snapxeats.common.constants.SnapXToast;
import com.example.synerzip.snapxeats.common.utilities.NetworkUtility;
import com.example.synerzip.snapxeats.dagger.NetworkModule;
import com.example.synerzip.snapxeats.network.NetworkHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class PreferenceInteractor {

    private PreferenceContract.PreferencePresenter preferencePresenter;

    private PreferenceContract.PreferenceView preferenceView;
    private Activity context;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;


    @Inject
    public PreferenceInteractor() {

    }

    public void setPreferenceView(PreferenceContract.PreferenceView preferenceView) {
        this.preferenceView = preferenceView;
    }

    public PreferenceContract.PreferenceView getPreferenceView() {
        return preferenceView;
    }


    public void setPresenter(PreferenceContract.PreferencePresenter presenter) {
        this.preferencePresenter = presenter;
    }

    public void setPreferencePresenter(PreferenceContract.PreferencePresenter presenter) {
        this.preferencePresenter = presenter;
    }

    /**
     * Get user current location
     */

    public void getUserLocation() {


        context = preferencePresenter.getActivityInstance();

        if (NetworkHelper.checkPermission(context)) {
            NetworkHelper.requestPermission(context);
        } else {
            preferencePresenter.showProgressDialog();
            if (NetworkUtility.isNetworkAvailable(context)) {
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            preferencePresenter.setLocation(mLastKnownLocation);
                            preferencePresenter.dismissProgressDialog();
                            Geocoder geocoder = new Geocoder(context);
                            try {

                                Log.v("Lat", "" + mLastKnownLocation.getLatitude());
                                Log.v("Long", "" + mLastKnownLocation.getLongitude());

                                List<Address> addresses = geocoder.getFromLocation(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude(), 1);

                                if (addresses.get(0).getSubLocality() != null) {
                                    preferencePresenter.updatePlaceName(addresses.get(0).getSubLocality());
                                    Log.v("Place name", "" + addresses.get(0).getSubLocality());
                                } else {
                                    if (addresses.get(0).getThoroughfare() != null) {
                                        preferencePresenter.updatePlaceName(addresses.get(0).getThoroughfare());
                                        Log.v("Place name", "" + addresses.get(0).getThoroughfare());
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }else {
                preferencePresenter.dismissProgressDialog();
                preferencePresenter.showNetworkErrorDialog();
            }
        }
    }

}
