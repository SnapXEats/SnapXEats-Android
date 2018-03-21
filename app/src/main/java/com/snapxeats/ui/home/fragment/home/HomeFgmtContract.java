package com.snapxeats.ui.home.fragment.home;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.common.model.preference.UserCuisinePreferences;
import com.snapxeats.common.model.preference.UserFoodPreferences;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.dagger.AppContract;

import java.util.List;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class HomeFgmtContract {

   public interface HomeFgmtView extends BaseView<HomeFgmtPresenter>, AppContract.SnapXResults {

    }

    public interface HomeFgmtPresenter extends BasePresenter<HomeFgmtView> {

        void presentScreen(Router.Screen screen);

        void getCuisineList(LocationCuisine locationCuisine);
    }

    interface HomeFgmtRouter {
        void presentScreen(Router.Screen screen);

        void setView(HomeFgmtView view);
    }
}