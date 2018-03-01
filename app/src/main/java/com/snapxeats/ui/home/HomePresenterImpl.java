package com.snapxeats.ui.home;

import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.utilities.SnapXResult;

import java.util.List;

/**
 * Created by Snehal Tembare on 28/2/18.
 */

public class HomePresenterImpl implements HomeContract.HomePresenter {

    private HomeInteractor interactor;
    private HomeContract.HomeView homeView;
    private HomeRouterImpl homeRouter;

    public HomePresenterImpl(HomeInteractor interactor,
                             HomeRouterImpl homeRouter) {
        this.interactor = interactor;
        this.homeRouter = homeRouter;
    }

    @Override
    public void addView(HomeContract.HomeView view) {
        this.homeView = view;
        interactor.setContext(view);
    }

    @Override
    public void dropView() {

    }

    @Override
    public List<SnapxData> getUserDataFromDb() {
        return interactor.getUserInfoFromDb();
    }

    @Override
    public void response(SnapXResult result, Object value) {

    }
}
