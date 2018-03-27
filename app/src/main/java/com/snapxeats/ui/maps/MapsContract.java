package com.snapxeats.ui.maps;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Prajakta Patil on 27/3/18.
 */
public class MapsContract {
    interface MapsView extends BaseView<MapsPresenter>, AppContract.SnapXResults {
    }

    interface MapsPresenter extends BasePresenter<MapsView> {
        void presentScreen(Router.Screen screen);
    }

    public interface MapsRouter {
        void presentScreen(Router.Screen screen);
    }
}
