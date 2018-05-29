package com.snapxeats.ui.home.fragment.smartphotos.smart;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 27/5/18.
 */

public class SmartContract {
    public interface SmartView extends BaseView<SmartPresenter>, AppContract.SnapXResults {

    }

    public interface SmartPresenter extends BasePresenter<SmartView> {
    }
}
