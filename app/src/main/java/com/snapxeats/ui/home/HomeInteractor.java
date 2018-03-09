package com.snapxeats.ui.home;

import android.app.Activity;

import com.snapxeats.SnapXApplication;
import com.snapxeats.common.model.DaoSession;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.SnapxDataDao;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 28/2/18.
 */

public class HomeInteractor {
    private Activity mContext;

    @Inject
    public HomeInteractor() {
    }

    private HomeContract.HomePresenter homePresenter;
    private SnapxDataDao snapxDataDao;
    private DaoSession daoSession;

    public void setContext(HomeContract.HomeView context) {
        this.mContext = context.getActivity();

        daoSession = ((SnapXApplication) mContext.getApplication()).getDaoSession();
        snapxDataDao = daoSession.getSnapxDataDao();

    }

    public void setHomePresenter(HomeContract.HomePresenter homePresenter) {
        this.homePresenter = homePresenter;
    }

    public List<SnapxData> getUserInfoFromDb() {
        return snapxDataDao.loadAll();
    }
}
