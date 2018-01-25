package com.snapxeats.ui.preferences;

import android.location.Location;
import android.support.annotation.Nullable;

import com.snapxeats.common.Router;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.utilities.SnapXResult;

import javax.inject.Singleton;


/**
 * Created by Snehal Tembare on 3/1/18.
 */

@Singleton
public class PreferencePresenterImpl implements PreferenceContract.PreferencePresenter {

    private PreferenceInteractor interactor;
    private Location location;

    @Nullable
    private PreferenceContract.PreferenceView preferenceView;

    private PreferenceContract.PreferenceRouter router;


    public PreferencePresenterImpl(PreferenceInteractor interactor, PreferenceRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void getLocation(PreferenceContract.PreferenceView preferenceView) {
        interactor.getLocation(preferenceView);
    }

    @Override
    public void response(SnapXResult result) {
        switch(result) {
            case SUCCESS:
                preferenceView.success();
                break;
            case FAILURE:
                preferenceView.error();
                break;
            case NONETWORK:
                preferenceView.noNetwork();
                break;
            case NETWORKERROR:
                preferenceView.networkError();
                break;
        }
    }


    /**
     * Update user location
     *
     * @param placename
     */

    public void updatePlace(String placename, Location location) {
        preferenceView.updatePlaceName(placename, location);
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    @Override
    public void getCuisineList() {
        interactor.getCuisineList();
    }


    @Override
    public void setCuisineList(RootCuisine rootCuisine) {
        preferenceView.getCuisineInfo(rootCuisine);
    }

    /**
     * Set view to Presenter
     * @param view
     */

    @Override
    public void addView(PreferenceContract.PreferenceView view) {
        preferenceView = view;
        router.setView(view);
    }

    @Override
    public void dropView() {
        preferenceView = null;
    }

}
