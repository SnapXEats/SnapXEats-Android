package com.snapxeats.ui.home.fragment.smartphotos;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 8/4/18.
 */

public class SmartPhotoContract {
    public interface SmartPhotoView extends BaseView<SmartPhotoPresenter>, AppContract.SnapXResults {

    }

    public interface SmartPhotoPresenter extends BasePresenter<SmartPhotoView> {
    }

    public interface SmartPhotoRouter {

    }
}
