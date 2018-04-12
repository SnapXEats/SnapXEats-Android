package com.snapxeats.ui.home;

import com.snapxeats.common.Router;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.checkin.CheckInRequest;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.utilities.SnapXResult;
import java.util.List;

/**
 * Created by Snehal Tembare on 28/2/18.
 */

public class HomePresenterImpl implements HomeContract.HomePresenter {

    private HomeInteractor interactor;
    private HomeContract.HomeView homeView;
    private HomeRouterImpl router;

    public HomePresenterImpl(HomeInteractor interactor,
                             HomeRouterImpl homeRouter) {
        this.interactor = interactor;
        this.router = homeRouter;
    }

    @Override
    public void addView(HomeContract.HomeView view) {
        this.homeView = view;
        interactor.setContext(view);
    }

    @Override
    public void dropView() {
        homeView = null;
    }

    @Override
    public List<SnapxData> getUserDataFromDb() {
        return interactor.getUserInfoFromDb();
    }

    @Override
    public void updatePreferences(UserPreference mUserPreference) {
        interactor.updatePreferences(mUserPreference);
    }

    @Override
    public void saveLocalData(UserPreference mUserPreference) {
        interactor.saveDataInLocalDb(mUserPreference);
    }

    @Override
    public void savePreferences(UserPreference mUserPreference) {
        interactor.applyPreferences(mUserPreference);
    }

    @Override
    public RootUserPreference getUserPreferenceFromDb() {
        return interactor.getUserPreferenceFromDb();
    }

    @Override
    public void sendUserGestures(RootFoodGestures foodGestures) {
        interactor.sendUserGestures(foodGestures);
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    @Override
    public void getNearByRestaurantToCheckIn(double lattitude, double longitude) {
        interactor.getNearByRestaurantToCheckIn(lattitude,longitude);
    }

    @Override
    public void checkIn(CheckInRequest checkInRequest) {
        interactor.checkIn(checkInRequest);
    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (null != homeView) {
            switch (result) {
                case SUCCESS:
                    homeView.success(value);
                    break;
                case FAILURE:
                    homeView.error(value);
                    break;
                case NONETWORK:
                    homeView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    homeView.networkError(value);
                    break;
            }
        }
    }
}
