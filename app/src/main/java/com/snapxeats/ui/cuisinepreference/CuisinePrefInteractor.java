package com.snapxeats.ui.cuisinepreference;

import android.app.Activity;

import com.snapxeats.SnapXApplication;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.Cuisines;
import com.snapxeats.common.model.DaoSession;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.model.UserCuisinePreferences;
import com.snapxeats.common.model.UserCuisinePreferencesDao;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

public class CuisinePrefInteractor {

    private CuisinePrefContract.CuisinePrefPresenter presenter;
    private Activity mContext;
    private CuisinePrefContract.CuisinePrefView view;
    private DaoSession daoSession;
    private UserCuisinePreferencesDao cuisinePreferencesDao;
    private List<UserCuisinePreferences> cuisinedPrefList;

    @Inject
    public CuisinePrefInteractor() {
    }

    public void setPresenter(CuisinePrefContract.CuisinePrefPresenter presenter) {
        this.presenter = presenter;
    }

    public void setContext(CuisinePrefContract.CuisinePrefView view) {
        this.view = view;
        mContext = view.getActivity();
        daoSession = ((SnapXApplication) mContext.getApplication()).getDaoSession();
        cuisinePreferencesDao = daoSession.getUserCuisinePreferencesDao();
    }

    public void getCuisinePref() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootCuisine> cuisinesCall = apiHelper.getCuisinePreferences();
            cuisinesCall.enqueue(new Callback<RootCuisine>() {
                @Override
                public void onResponse(Call<RootCuisine> call, Response<RootCuisine> response) {
                    if (response.isSuccessful() && response.body().getCuisineList() != null) {
                        presenter.response(SnapXResult.SUCCESS, response.body());
                    }
                }

                @Override
                public void onFailure(Call<RootCuisine> call, Throwable t) {
                    presenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            presenter.response(SnapXResult.NONETWORK, null);
        }
    }

    public void saveCuisineList(List<Cuisines> rootCuisineList) {
        cuisinePreferencesDao.deleteAll();
        UserCuisinePreferences cuisinePreferences = null;
        for (Cuisines cuisines : rootCuisineList) {
            cuisinePreferences = new UserCuisinePreferences(cuisines.getCuisine_info_id(),
                    cuisines.is_cuisine_like(), cuisines.is_cuisine_favourite(), "");
            if (null != cuisinePreferences) {
                cuisinePreferencesDao.insert(cuisinePreferences);
            }
        }
    }

    public List<UserCuisinePreferences> getCuisinePrefListFromDb() {

        return cuisinePreferencesDao.loadAll() != null ? cuisinePreferencesDao.loadAll() : null;
    }

    public void resetCuisineList() {
        cuisinePreferencesDao.deleteAll();
    }
}
