package com.snapxeats.ui.home.fragment.home;

import com.snapxeats.common.Router;
import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.utilities.SnapXResult;
import javax.inject.Singleton;


/**
 * Created by Snehal Tembare on 3/1/18.
 */

@Singleton
public class HomeFgmtPresenterImpl implements HomeFgmtContract.HomeFgmtPresenter {

    private HomeFgmtInteractor mHomeFgmtInteractor;

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

    public HomeFgmtContract.HomeFgmtView getView(){
        return  mHomeFgmtView;
    }

    public void setView(HomeFgmtContract.HomeFgmtView homeFgmtView){
        mHomeFgmtView = homeFgmtView;
    }

    @Override
    public void dropView() {
        mHomeFgmtView = null;
    }
}
