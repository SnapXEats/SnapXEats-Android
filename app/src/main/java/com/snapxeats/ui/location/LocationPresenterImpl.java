package com.snapxeats.ui.location;

import com.snapxeats.common.Router;
import com.snapxeats.common.utilities.SnapXResult;

import java.util.List;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationPresenterImpl implements LocationContract.LocationPresenter {
    private LocationInteractor interactor;
    private LocationContract.LocationView locationView;
    public static List<String> arryalist;
    private LocationRouterImpl router;

    public LocationPresenterImpl(LocationInteractor interactor,
                                 LocationRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
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

        switch (result) {
            case SUCCESS:
                locationView.success();
                break;
            case FAILURE:
                locationView.error();
                break;
            case NONETWORK:
                locationView.noNetwork();
                break;
            case NETWORKERROR:
                locationView.networkError();
                break;
        }
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
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

}
