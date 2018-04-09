package com.snapxeats.ui.home.fragment.rewards;

import com.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Snehal Tembare on 8/4/18.
 */

public class RewardsPresenterImpl implements RewardsContract.RewardsPresenter {

    private RewardsInteractor interactor;
    private RewardsRouterImpl router;

    public RewardsPresenterImpl(RewardsInteractor interactor, RewardsRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }


    @Override
    public void addView(RewardsContract.RewardsView view) {

    }

    @Override
    public void dropView() {

    }

    @Override
    public void response(SnapXResult result, Object value) {

    }
}
