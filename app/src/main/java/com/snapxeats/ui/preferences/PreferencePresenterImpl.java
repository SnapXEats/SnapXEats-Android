package com.snapxeats.ui.preferences;

import android.location.Location;
import android.support.annotation.Nullable;

import com.snapxeats.common.Router;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.utilities.SnapXResult;

import javax.inject.Singleton;


/**
 * Created by Snehal Tembare on 3/1/18.
 */

@Singleton
public class PreferencePresenterImpl implements PreferenceContract.PreferencePresenter {

    private PreferenceInteractor mPreferenceInteractor;

    @Nullable
    private PreferenceContract.PreferenceView mPreferenceView;

    private PreferenceContract.PreferenceRouter mPreferenceRouter;

    PreferencePresenterImpl(PreferenceInteractor mPreferenceInteractor, PreferenceRouterImpl router) {
        this.mPreferenceInteractor = mPreferenceInteractor;
        this.mPreferenceRouter = router;
    }

    @Override
    public void getLocation(PreferenceContract.PreferenceView preferenceView) {
        mPreferenceInteractor.getLocation(preferenceView);
    }

    @Override
    public void response(SnapXResult result) {
        switch(result) {
            case SUCCESS:
                mPreferenceView.success();
                break;
            case FAILURE:
                mPreferenceView.error();
                break;
            case NONETWORK:
                mPreferenceView.noNetwork();
                break;
            case NETWORKERROR:
                mPreferenceView.networkError();
                break;
        }
    }

    /**
     * Update user location
     *
     * @param placename
     */

    public void updatePlace(String placename, Location location) {
        if (mPreferenceView != null) {
            mPreferenceView.updatePlaceName(placename, location);
        }
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        mPreferenceRouter.presentScreen(screen);
    }

    @Override
    public void getCuisineList() {
        mPreferenceInteractor.getCuisineList();
    }

    @Override
    public void setCuisineList(RootCuisine rootCuisine) {
        mPreferenceView.getCuisineInfo(rootCuisine);
    }

    /**
     * Set view to Presenter
     * @param view
     */

    @Override
    public void addView(PreferenceContract.PreferenceView view) {
        mPreferenceView = view;
        mPreferenceRouter.setView(view);
    }

    @Override
    public void dropView() {
        mPreferenceView = null;
    }

}
