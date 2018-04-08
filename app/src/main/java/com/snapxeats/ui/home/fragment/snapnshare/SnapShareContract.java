package com.snapxeats.ui.home.fragment.snapnshare;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 8/4/18.
 */

public class SnapShareContract {
    public interface SnapShareView extends BaseView<SnapSharePresenter>, AppContract.SnapXResults {

    }

    public interface SnapSharePresenter extends BasePresenter<SnapShareView> {
    }

    public interface SnapShareRouter {

    }
}
