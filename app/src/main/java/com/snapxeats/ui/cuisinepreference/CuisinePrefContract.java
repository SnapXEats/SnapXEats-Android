package com.snapxeats.ui.cuisinepreference;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

public class CuisinePrefContract {

    public interface CuisinePrefView extends BaseView<CuisinePrefPresenter>, AppContract.SnapXResults {

    }

    public interface CuisinePrefPresenter extends BasePresenter<CuisinePrefView> {
        void presentScreen(Router.Screen screen);

        void getCuisinePrefList();
    }

    interface CuisinePrefRouter {
        void presentScreen(Router.Screen screen);

        void setView(CuisinePrefContract.CuisinePrefView view);
    }

}
