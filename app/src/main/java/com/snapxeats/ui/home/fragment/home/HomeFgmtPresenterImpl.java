package com.snapxeats.ui.home.fragment.home;

import android.support.annotation.Nullable;

import com.snapxeats.common.Router;
import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.model.SnapXUser;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.utilities.SnapXResult;

import javax.inject.Singleton;


/**
 * Created by Snehal Tembare on 3/1/18.
 */

@Singleton
public class HomeFgmtPresenterImpl implements HomeFgmtContract.HomeFgmtPresenter {

    private HomeFgmtInteractor mHomeFgmtInteractor;

    @Nullable
    private HomeFgmtContract.HomeFgmtView mHomeFgmtView;

    private HomeFgmtContract.HomeFgmtRouter mHomeFgmtRouter;

    public HomeFgmtPresenterImpl(HomeFgmtInteractor mHomeFgmtInteractor, HomeFgmtRouterImpl router) {
        this.mHomeFgmtInteractor = mHomeFgmtInteractor;
        this.mHomeFgmtRouter = router;
    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (null != mHomeFgmtView) {
            switch (result) {
                case SUCCESS:
                    mHomeFgmtView.success(value);
                    break;
                case FAILURE:
                    mHomeFgmtView.error(value);
                    break;
                case NONETWORK:
                    mHomeFgmtView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    mHomeFgmtView.networkError(value);
                    break;
            }
        }
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        mHomeFgmtRouter.presentScreen(screen);
    }

    @Override
    public void getCuisineList(LocationCuisine locationCuisine) {

        mHomeFgmtInteractor.getCuisineList(locationCuisine);
    }

    @Override
    public void getUserData(SnapXUserRequest snapXUserRequest) {
        mHomeFgmtInteractor.getUserData(snapXUserRequest);
    }

    @Override
    public void saveUserDataInDb(SnapXUser snapXUser) {
        mHomeFgmtInteractor.saveDataInDb(snapXUser);
    }

    /**
     * Set view to Presenter
     *
     * @param view
     */

    @Override
    public void addView(HomeFgmtContract.HomeFgmtView view) {
        mHomeFgmtView = view;
        mHomeFgmtRouter.setView(view);
        mHomeFgmtInteractor.setContext(view);
    }

    @Override
    public void dropView() {
        mHomeFgmtView = null;
    }
}
