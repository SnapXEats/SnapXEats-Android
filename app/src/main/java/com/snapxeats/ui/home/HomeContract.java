package com.snapxeats.ui.home;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.model.SnapxData;

import java.util.List;

/**
 * Created by Snehal Tembare on 16/2/18.
 */

public class HomeContract {
    public interface HomeView extends BaseView<HomePresenter>{

    }

    public interface HomePresenter extends BasePresenter<HomeView>{
        List<SnapxData> getUserDataFromDb();
    }

    public interface HomeRouter {

    }
}
