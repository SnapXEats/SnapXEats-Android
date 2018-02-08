package com.snapxeats.ui.preferences;

import android.app.Activity;
import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;
import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

@Singleton
public class PreferenceInteractor {

    private PreferenceContract.PreferencePresenter preferencePresenter;

    private Activity mContext;

    @Inject
    PreferenceInteractor() {
    }

    @Inject
    ApiClient apiClient;

    public void setPreferencePresenter(PreferenceContract.PreferencePresenter presenter) {
        this.preferencePresenter = presenter;
    }

    /**
     * get cuisines list
     */
    void getCuisineList(PreferenceContract.PreferenceView preferenceView, LocationCuisine locationCuisine) {
        mContext = preferenceView.getActivity();
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            preferenceView.showProgressDialog();
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);

        /*    TODO latlng are hardcoded for now
        double lat = locationCuisine.getLatitude();
        double lng =locationCuisine.getLongitude();*/
            double lat = 40.4862157;
            double lng = -74.4518188;
            Call<RootCuisine> listCuisineCall = apiHelper.getCuisineList(lat, lng);
            listCuisineCall.enqueue(new Callback<RootCuisine>() {
                @Override
                public void onResponse(Call<RootCuisine> call, Response<RootCuisine> response) {
                    preferenceView.dismissProgressDialog();
                    if (response.isSuccessful() && response.body() != null) {
                        RootCuisine rootCuisine = response.body();
                        preferencePresenter.response(SnapXResult.SUCCESS, rootCuisine);
                    }
                }

                @Override
                public void onFailure(Call<RootCuisine> call, Throwable t) {
                    preferencePresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            preferencePresenter.response(SnapXResult.NONETWORK, null);
        }
    }
}
