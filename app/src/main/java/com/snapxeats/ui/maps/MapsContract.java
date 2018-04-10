package com.snapxeats.ui.maps;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefContract;

/**
 * Created by Prajakta Patil on 27/3/18.
 */
public class MapsContract {
    interface MapsView extends BaseView<MapsPresenter>, AppContract.SnapXResults {
    }

    interface MapsPresenter extends BasePresenter<MapsView> {
        void presentScreen(Router.Screen screen);
        void getUserPreferences();
    }

    public interface MapsRouter {
        void setView(MapsContract.MapsView view);
        void presentScreen(Router.Screen screen);
    }
}
