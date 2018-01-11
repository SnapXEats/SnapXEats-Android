package com.example.synerzip.snapxeats.ui.location;

import android.app.Activity;
import android.location.Location;

import com.example.synerzip.snapxeats.ui.preferences.PreferenceContract;
import com.google.android.gms.location.FusedLocationProviderClient;

import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationInteractor {
    private LocationContract.LocationPresenter locationPresenter;

    private LocationContract.LocationView locationView;
    private Activity context;

    @Inject
    public LocationInteractor(){}

    public void setLocationView(LocationContract.LocationView locationView) {
        this.locationView = locationView;
    }

    public LocationContract.LocationView getPreferenceView() {
        return locationView;
    }


    public void setPresenter(LocationContract.LocationPresenter presenter) {
        this.locationPresenter = presenter;
    }

    public void setLocationPresenter(LocationContract.LocationPresenter presenter) {
        this.locationPresenter = presenter;
    }
}
