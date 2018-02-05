package com.snapxeats.ui.location;

import com.snapxeats.common.utilities.SnapXResult;

import java.util.List;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationPresenterImpl implements LocationContract.LocationPresenter {
    private LocationInteractor interactor;
    private LocationContract.LocationView locationView;
    public static List<String> arryalist;

    public LocationPresenterImpl(LocationInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void addView(LocationContract.LocationView view) {
        this.locationView = view;
    }

    @Override
    public void dropView() {

    }

    @Override
    public void response(SnapXResult result) {

    }

    @Override
    public List<String> getPredictionList(LocationContract.LocationView locationView, String input) {
        arryalist = interactor.getPredictionList(locationView, input);
        return arryalist;
    }

    @Override
    public void getPlaceDetails(String placeId) {
        interactor.getPlaceDetails(locationView, placeId);
    }

    @Override
    public void setLatLng(double lat, double lng) {
        locationView.setLatLng(lat,lng);
    }

}
