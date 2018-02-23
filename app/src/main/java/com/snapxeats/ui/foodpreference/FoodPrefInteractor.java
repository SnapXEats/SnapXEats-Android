package com.snapxeats.ui.foodpreference;

import android.app.Activity;

import com.google.android.gms.common.api.Api;
import com.snapxeats.common.model.RootFoodPref;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

public class FoodPrefInteractor {
    private FoodPreferenceContract.FoodPreferencePresenter presenter;
    private FoodPreferenceContract.FoodPreferenceView view;
    private Activity mContext;

    @Inject
    public FoodPrefInteractor() {
    }

    public void setPresenter(FoodPreferenceContract.FoodPreferencePresenter presenter) {
        this.presenter = presenter;
    }

    public void setContext(FoodPreferenceContract.FoodPreferenceView view) {
        this.view = view;
        this.mContext = view.getActivity();
    }

    public void getFoodPrefList() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootFoodPref> foodPrefCall = apiHelper.getFoodPreferences();
            foodPrefCall.enqueue(new Callback<RootFoodPref>() {
                @Override
                public void onResponse(Call<RootFoodPref> call, Response<RootFoodPref> response) {
                    if (response.isSuccessful() && response.body().getFoodTypeList() != null) {
                        presenter.response(SnapXResult.SUCCESS, response.body());
                    }
                }

                @Override
                public void onFailure(Call<RootFoodPref> call, Throwable t) {
                    presenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            presenter.response(SnapXResult.NONETWORK, null);
        }
    }
}
