package com.snapxeats.ui.directions;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Prajakta Patil on 22/3/18.
 */
public class DirectionsContract {
    interface DirectionsView extends BaseView<DirectionsPresenter>, AppContract.SnapXResults {
    }

    interface DirectionsPresenter extends BasePresenter<DirectionsView> {
        void presentScreen(Router.Screen screen);
    }

    public interface DirectionsRouter {
        void presentScreen(Router.Screen screen);
    }
}