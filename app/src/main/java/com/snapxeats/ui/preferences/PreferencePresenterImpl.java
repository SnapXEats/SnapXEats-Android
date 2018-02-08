package com.snapxeats.ui.preferences;

import android.support.annotation.Nullable;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.LocationCuisine;
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
    public void response(SnapXResult result,Object value) {
        switch (result) {
            case SUCCESS:
                mPreferenceView.success(value);
                break;
            case FAILURE:
                mPreferenceView.error();
                break;
            case NONETWORK:
                mPreferenceView.noNetwork(value);
                break;
            case NETWORKERROR:
                mPreferenceView.networkError();
                break;
        }
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        mPreferenceRouter.presentScreen(screen);
    }

    @Override
    public void getCuisineList(PreferenceContract.PreferenceView mPreferenceView,
                               LocationCuisine locationCuisine) {

        mPreferenceInteractor.getCuisineList(mPreferenceView,locationCuisine);

    }

    /**
     * Set view to Presenter
     *
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
