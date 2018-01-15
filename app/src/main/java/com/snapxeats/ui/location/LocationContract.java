package com.snapxeats.ui.location;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationContract {

interface LocationView extends BaseView<LocationPresenter>{

}

interface LocationPresenter extends BasePresenter<LocationView> {

}
}
