package com.snapxeats.ui.foodpreference;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.preference.FoodPref;
import com.snapxeats.dagger.AppContract;
import java.util.List;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

public class FoodPreferenceContract {

    interface FoodPreferenceView extends BaseView<FoodPreferencePresenter>, AppContract.SnapXResults {

    }

    interface FoodPreferencePresenter extends BasePresenter<FoodPreferenceView> {
        void presentScreen(Router.Screen screen);

        void getFoodPrefList();

        void saveFoodPrefList(List<FoodPref> foodPrefList);
    }

    interface FoodPrefRouter {
        void presentScreen(Router.Screen screen);

        void setView(FoodPreferenceView view);
    }
}
