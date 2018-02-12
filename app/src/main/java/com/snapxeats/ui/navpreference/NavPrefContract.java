package com.snapxeats.ui.navpreference;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 9/2/18.
 */

public class NavPrefContract {

    interface NavPrefView extends BaseView<NavPrefPresenter>,AppContract.SnapXResults{

    }

    interface NavPrefPresenter extends BasePresenter<NavPrefView>{

    }

    interface NavPrefRouter{

    }
}
