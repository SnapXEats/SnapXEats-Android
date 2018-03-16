package com.snapxeats.ui.foodpreference;

import com.snapxeats.common.Router;
import com.snapxeats.common.model.preference.FoodPref;
import com.snapxeats.common.utilities.SnapXResult;
import java.util.List;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

@Singleton
public class FoodPrefPresenterImpl implements FoodPreferenceContract.FoodPreferencePresenter {

    private FoodPrefInteractor interactor;
    private FoodPrefRouterImpl router;
    private FoodPreferenceContract.FoodPreferenceView foodPrefView;

    public FoodPrefPresenterImpl(FoodPrefInteractor interactor,
                                 FoodPrefRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void addView(FoodPreferenceContract.FoodPreferenceView view) {
        this.foodPrefView = view;
        router.setView(view);
        interactor.setContext(view);
    }

    @Override
    public void dropView() {
        foodPrefView = null;
    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (foodPrefView != null) {
            switch (result) {
                case SUCCESS:
                    foodPrefView.success(value);
                    break;
                case FAILURE:
                    foodPrefView.error(value);
                    break;
                case NONETWORK:
                    foodPrefView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    foodPrefView.networkError(value);
                    break;
            }
        }
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    @Override
    public void getFoodPrefList() {
        interactor.getFoodPrefList();
    }

    @Override
    public void saveFoodPrefList(List<FoodPref> foodPrefList) {
        interactor.saveFoodPrefList(foodPrefList);
    }
}
