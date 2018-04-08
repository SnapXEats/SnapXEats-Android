package com.snapxeats.ui.home.fragment.foodjourney;

import com.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Snehal Tembare on 8/4/18.
 */

public class FoodJourneyPresenterImpl implements FoodJourneyContract.FoodJourneyPresenter {

    private FoodJourneyInteractor interactor;
    private FoodJourneyRouterImpl router;

    public FoodJourneyPresenterImpl(FoodJourneyInteractor interactor, FoodJourneyRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void addView(FoodJourneyContract.FoodJourneyView view) {

    }

    @Override
    public void dropView() {

    }

    @Override
    public void response(SnapXResult result, Object value) {

    }
}
