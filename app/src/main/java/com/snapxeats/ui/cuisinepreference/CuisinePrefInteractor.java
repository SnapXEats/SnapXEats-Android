package com.snapxeats.ui.cuisinepreference;

import android.app.Activity;
import android.content.SharedPreferences;

import com.snapxeats.R;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.model.DaoSession;
import com.snapxeats.common.model.preference.Cuisines;
import com.snapxeats.common.model.preference.RootCuisine;
import com.snapxeats.common.model.preference.UserCuisinePreferences;
import com.snapxeats.common.model.preference.UserCuisinePreferencesDao;
import com.snapxeats.common.utilities.AppUtility;
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
    private UserCuisinePreferencesDao cuisinePreferencesDao;

    @Inject
    AppUtility utility;

    @Inject
    public CuisinePrefInteractor() {
    }

    public void setPresenter(CuisinePrefContract.CuisinePrefPresenter presenter) {
        this.presenter = presenter;
    }

    public void setContext(CuisinePrefContract.CuisinePrefView view) {
        this.view = view;
        mContext = view.getActivity();
        utility.setContext(mContext);
        DaoSession daoSession = ((SnapXApplication) mContext.getApplication()).getDaoSession();
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
        UserCuisinePreferences cuisinePreferences;
        SharedPreferences preferences = utility.getSharedPreferences();
        String userId = preferences.getString(mContext.getString(R.string.user_id), "");

        for (Cuisines cuisines : rootCuisineList) {
            if (cuisines.is_cuisine_like() || cuisines.is_cuisine_favourite()) {
                cuisinePreferences = new UserCuisinePreferences(cuisines.getCuisine_info_id(),
                        cuisines.is_cuisine_like(), cuisines.is_cuisine_favourite(), userId);
                cuisinePreferencesDao.insert(cuisinePreferences);
            }
        }
    }
}
