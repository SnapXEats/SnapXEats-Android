package com.snapxeats.ui.home.fragment.home;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class HomeFgmtContract {

   public interface HomeFgmtView extends BaseView<HomeFgmtPresenter>, AppContract.SnapXResults {

    }

    public interface HomeFgmtPresenter extends BasePresenter<HomeFgmtView> {

        void presentScreen(Router.Screen screen);

        void getCuisineList(HomeFgmtView homeFgmtView,
                            LocationCuisine locationCuisine);

        void getUserData(HomeFgmtView homeFgmtView, SnapXUserRequest snapXUserRequest);

    }

    interface HomeFgmtRouter {
        void presentScreen(Router.Screen screen);

        void setView(HomeFgmtView view);
    }
}