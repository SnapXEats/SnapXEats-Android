package com.snapxeats.ui.maps;

import android.app.Activity;

import com.snapxeats.common.utilities.AppUtility;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 27/3/18.
 */
@Singleton
public class MapsInteractor {
    private Activity mContext;
    private MapsContract.MapsPresenter mPresenter;
    private MapsContract.MapsView mView;

    @Inject
    public MapsInteractor() {
    }

    @Inject
    AppUtility utility;

    public void setMapsPresenter(MapsContract.MapsPresenter presenter) {
        this.mPresenter = presenter;
    }

    public void setContext(MapsContract.MapsView view) {
        this.mView = view;
        this.mContext = view.getActivity();
    }
}

