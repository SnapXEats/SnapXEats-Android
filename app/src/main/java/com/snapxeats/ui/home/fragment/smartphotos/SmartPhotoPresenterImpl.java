package com.snapxeats.ui.home.fragment.smartphotos;

import com.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Snehal Tembare on 8/4/18.
 */

public class SmartPhotoPresenterImpl implements SmartPhotoContract.SmartPhotoPresenter {
    private SmartPhotoInteractor interactor;
    private SmartPhotoRouterImpl router;

    public SmartPhotoPresenterImpl(SmartPhotoInteractor interactor, SmartPhotoRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void addView(SmartPhotoContract.SmartPhotoView view) {

    }

    @Override
    public void dropView() {

    }

    @Override
    public void response(SnapXResult result, Object value) {

    }
}
