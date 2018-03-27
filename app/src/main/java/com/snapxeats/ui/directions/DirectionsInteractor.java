package com.snapxeats.ui.directions;

import android.app.Activity;

import com.snapxeats.common.utilities.AppUtility;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 22/3/18.
 */
@Singleton
public class DirectionsInteractor {
    private Activity mContext;
    private DirectionsContract.DirectionsPresenter mPresenter;
    private DirectionsContract.DirectionsView mView;

    @Inject
    public DirectionsInteractor() {
    }

    @Inject
    AppUtility utility;

    public void setDirectionsPresenter(DirectionsContract.DirectionsPresenter presenter) {
        this.mPresenter = presenter;
    }

    public void setContext(DirectionsContract.DirectionsView view) {
        this.mView = view;
        this.mContext = view.getActivity();
    }
}
