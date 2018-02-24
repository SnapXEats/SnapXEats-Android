package com.snapxeats.ui.home.fragment.navpreference;

import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 9/2/18.
 */

public class NavPrefInteractor {

    private NavPrefContract.NavPrefPresenter navPrefPresenter;

    @Inject
    public NavPrefInteractor() {
    }

    public void setPreferencePresenter(NavPrefContract.NavPrefPresenter presenter) {
        this.navPrefPresenter = presenter;
    }
}
