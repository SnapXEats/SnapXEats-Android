package com.snapxeats.ui.navpreference;

/**
 * Created by Snehal Tembare on 9/2/18.
 */

public class NavPrefInteractor {

    private NavPrefContract.NavPrefPresenter navPrefPresenter;

    public void setPreferencePresenter(NavPrefContract.NavPrefPresenter presenter) {
        this.navPrefPresenter = presenter;
    }
}
