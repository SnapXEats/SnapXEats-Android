package com.snapxeats.ui.location;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.preferences.PreferenceContract;

import java.util.List;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationContract {

    interface LocationView extends BaseView<LocationPresenter>, AppContract.SnapXResults {

    }

    interface LocationPresenter extends BasePresenter<LocationView> {
        List<String> getPredictionList(LocationContract.LocationView locationView, String input);

        void getPlaceDetails(String placeId);

        void presentScreen(Router.Screen screen);

    }

    interface LocationRouter {
        void presentScreen(Router.Screen screen);

        void setView(LocationContract.LocationView view);

    }
}
