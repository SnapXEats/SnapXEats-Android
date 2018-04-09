package com.snapxeats.ui.home.fragment.rewards;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 8/4/18.
 */

public class RewardsContract {
    public interface RewardsView extends BaseView<RewardsPresenter>, AppContract.SnapXResults {

    }

    public interface RewardsPresenter extends BasePresenter<RewardsView> {
    }

    public interface RewardsRouter {

    }
}
