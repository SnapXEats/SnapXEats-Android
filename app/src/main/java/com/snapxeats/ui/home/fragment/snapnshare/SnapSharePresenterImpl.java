package com.snapxeats.ui.home.fragment.snapnshare;

import com.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Snehal Tembare on 8/4/18.
 */

public class SnapSharePresenterImpl implements SnapShareContract.SnapSharePresenter {
    private SnapShareContract.SnapShareView snapShareView;
    private SnapShareInteractor interactor;
    private SnapShareRouterImpl router;

    public SnapSharePresenterImpl(SnapShareInteractor interactor, SnapShareRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void addView(SnapShareContract.SnapShareView view) {
        snapShareView = view;
        router.setView(view);
        interactor.setContext(view);
    }

    @Override
    public void dropView() {
        snapShareView = null;
    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (null != snapShareView) {
            switch (result) {
                case SUCCESS:
                    snapShareView.success(value);
                    break;
                case FAILURE:
                    snapShareView.error(value);
                    break;
                case NONETWORK:
                    snapShareView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    snapShareView.networkError(value);
                    break;
            }
        }
    }

    @Override
    public void getRestaurantInfo(String restaurantId) {
        interactor.getRestaurantInfo(restaurantId);
    }
}
