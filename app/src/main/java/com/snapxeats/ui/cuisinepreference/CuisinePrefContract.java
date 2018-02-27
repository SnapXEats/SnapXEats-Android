package com.snapxeats.ui.cuisinepreference;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.Cuisines;
import com.snapxeats.common.model.UserCuisinePreferences;
import com.snapxeats.dagger.AppContract;

import java.util.List;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

public class CuisinePrefContract {

    public interface CuisinePrefView extends BaseView<CuisinePrefPresenter>, AppContract.SnapXResults {

    }

    public interface CuisinePrefPresenter extends BasePresenter<CuisinePrefView> {
        void presentScreen(Router.Screen screen);

        void getCuisinePrefList();

        void saveCuisineList(List<Cuisines> rootCuisineList);

        List<UserCuisinePreferences> getCuisineListFromDb();

        void resetCuisineList();
    }

    interface CuisinePrefRouter {
        void presentScreen(Router.Screen screen);

        void setView(CuisinePrefContract.CuisinePrefView view);
    }

}