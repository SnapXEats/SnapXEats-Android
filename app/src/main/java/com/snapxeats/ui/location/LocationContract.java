package com.snapxeats.ui.location;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationContract {

    interface LocationView extends BaseView<LocationPresenter>, AppContract.SnapXResults {

    }

    interface LocationPresenter extends BasePresenter<LocationView> {

        void getPlaceDetails(String placeId);

        void presentScreen(Router.Screen screen);

    }

    interface LocationRouter {
        void presentScreen(Router.Screen screen);

        void setView(LocationContract.LocationView view);

    }
}
