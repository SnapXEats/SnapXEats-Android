package com.snapxeats.ui.home;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.SnapXData;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.model.checkin.CheckInRequest;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.dagger.AppContract;

import java.util.List;

/**
 * Created by Snehal Tembare on 16/2/18.
 */

public class HomeContract {
    public interface HomeView extends BaseView<HomePresenter>, AppContract.SnapXResults {

    }

    public interface HomePresenter extends BasePresenter<HomeView> {
        List<SnapXData> getUserDataFromDb();

        void updatePreferences(UserPreference mUserPreference);

        void saveLocalData(UserPreference mUserPreference);

        void savePreferences(UserPreference mUserPreference);

        RootUserPreference getUserPreferenceFromDb();

        void sendUserGestures(RootFoodGestures foodGestures);

        void presentScreen(Router.Screen screen);

        void getNearByRestaurantToCheckIn(double lattitude, double longitude);

        void checkIn(CheckInRequest checkInRequest);

        void getSmartPhotoInfo(String dishId);

        void getInstaInfo(String token);

        void getUserdata(SnapXUserRequest snapXUserRequest);
    }

    public interface HomeRouter {
        void presentScreen(Router.Screen screen);

        void setView(HomeContract.HomeView view);
    }
}
