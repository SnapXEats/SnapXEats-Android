package com.snapxeats.ui.cuisinepreference;

import com.snapxeats.common.Router;
import com.snapxeats.common.model.preference.Cuisines;
import com.snapxeats.common.utilities.SnapXResult;
import java.util.List;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

@Singleton
public class CuisinePrefPresenterImpl implements CuisinePrefContract.CuisinePrefPresenter {

    private CuisinePrefInteractor interactor;
    private CuisinePrefRouterImpl router;
    private CuisinePrefContract.CuisinePrefView cuisinePrefView;

    public CuisinePrefPresenterImpl(CuisinePrefInteractor interactor, CuisinePrefRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void addView(CuisinePrefContract.CuisinePrefView view) {
        this.cuisinePrefView = view;
        router.setView(view);
        interactor.setContext(view);
    }

    @Override
    public void dropView() {
        cuisinePrefView = null;
    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (cuisinePrefView != null) {
            switch (result) {
                case SUCCESS:
                    cuisinePrefView.success(value);
                    break;
                case FAILURE:
                    cuisinePrefView.error(value);
                    break;
                case NONETWORK:
                    cuisinePrefView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    cuisinePrefView.networkError(value);
                    break;
            }
        }

    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    @Override
    public void getCuisinePrefList() {
        interactor.getCuisinePref();
    }

    @Override
    public void saveCuisineList(List<Cuisines> rootCuisineList) {
        interactor.saveCuisineList(rootCuisineList);
    }
}
