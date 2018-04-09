package com.snapxeats.ui.home.fragment.checkin;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 8/4/18.
 */

public class CheckInContract {
    public interface CheckInView extends BaseView<CheckInPresenter>, AppContract.SnapXResults {

    }

    public interface CheckInPresenter extends BasePresenter<CheckInView> {
    }

    public interface CheckInRouter {

    }
}
