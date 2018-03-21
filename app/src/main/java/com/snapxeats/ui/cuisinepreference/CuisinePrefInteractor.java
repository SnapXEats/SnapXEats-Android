package com.snapxeats.ui.cuisinepreference;

import android.content.Context;
import com.snapxeats.common.model.preference.Cuisines;
import com.snapxeats.common.model.preference.RootCuisine;
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
    private Context mContext;
    private CuisinePrefContract.CuisinePrefView view;
    private UserCuisinePreferencesDao cuisinePreferencesDao;

    @Inject
    AppUtility utility;

    @Inject
    CuisinePrefInteractor() {
    }

    @Inject
    CuisinePrefDbHelper helper;

    public void setPresenter(CuisinePrefContract.CuisinePrefPresenter presenter) {
        this.presenter = presenter;
    }

    public void setContext(CuisinePrefContract.CuisinePrefView view) {
        this.view = view;
        mContext = view.getActivity();
        utility.setContext(mContext);
        helper.setContext(mContext);
        cuisinePreferencesDao = helper.getCuisinePreferencesDao();
    }

    public void getCuisinePref() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);

            Call<RootCuisine> cuisinesCall = apiHelper.getCuisinePreferences();
            cuisinesCall.enqueue(new Callback<RootCuisine>() {
                @Override
                public void onResponse(Call<RootCuisine> call, Response<RootCuisine> response) {
                    if (response.isSuccessful() && null != response.body().getCuisineList()) {
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

    void saveCuisineList(List<Cuisines> rootCuisineList) {
        helper.saveCuisineList(rootCuisineList);
    }
}
