package com.snapxeats.ui.maps;

import android.support.annotation.Nullable;

import com.snapxeats.common.Router;
import com.snapxeats.common.utilities.SnapXResult;

import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 27/3/18.
 */
@Singleton
public class MapsPresenterImpl implements MapsContract.MapsPresenter {
    private MapsRouterImpl mMapsRouter;

    private MapsInteractor mMapsInteractor;

    @Nullable
    private MapsContract.MapsView mMapsView;

    public MapsPresenterImpl(MapsInteractor mapsInteractor, MapsRouterImpl mapsRouter) {
        this.mMapsInteractor = mapsInteractor;
        this.mMapsRouter = mapsRouter;
    }

    @Override
    public void addView(MapsContract.MapsView view) {
        this.mMapsView = view;
        mMapsRouter.setView(view);
        mMapsInteractor.setContext(view);
    }

    @Override
    public void dropView() {
        mMapsView = null;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        mMapsRouter.presentScreen(screen);
    }

    @Override
    public void getUserPreferences() {
        mMapsInteractor.getUserPreferences();
    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (null != mMapsView) {
            switch (result) {
                case SUCCESS:
                    mMapsView.success(value);
                    break;
                case FAILURE:
                    mMapsView.error(value);
                    break;
                case NONETWORK:
                    mMapsView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    mMapsView.networkError(value);
                    break;
            }
        }
    }
}
