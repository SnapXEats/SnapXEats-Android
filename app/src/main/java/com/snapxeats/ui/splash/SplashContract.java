package com.snapxeats.ui.splash;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;

/**
 * Created by Snehal Tembare on 30/1/18.
 */

public class SplashContract {

    interface SplashView extends BaseView<SplashPresenter>{

    }
    interface SplashPresenter extends BasePresenter<SplashView> {

    }
}
