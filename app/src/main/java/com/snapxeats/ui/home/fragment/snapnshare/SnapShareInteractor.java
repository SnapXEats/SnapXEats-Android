package com.snapxeats.ui.home.fragment.snapnshare;

import android.content.Context;

import com.snapxeats.common.model.restaurantDetails.RootRestaurantDetails;
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
 * Created by Snehal Tembare on 8/4/18.
 */

public class SnapShareInteractor {

    private SnapShareContract.SnapSharePresenter snapSharePresenter;
    private Context mContext;

    @Inject
    public SnapShareInteractor() {
    }

    public void setContext(SnapShareContract.SnapShareView view) {
        this.mContext = view.getActivity();
    }

    public void setSnapSharePresenter(SnapShareContract.SnapSharePresenter snapSharePresenter) {
        this.snapSharePresenter = snapSharePresenter;
    }

    void getRestaurantInfo(String restaurantId) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootRestaurantDetails> snapXUserCall = apiHelper.getRestDetails(restaurantId);
            snapXUserCall.enqueue(new Callback<RootRestaurantDetails>() {
                @Override
                public void onResponse(Call<RootRestaurantDetails> call, Response<RootRestaurantDetails> response) {
                    if (response.isSuccessful() && null != response.body()) {
                        RootRestaurantDetails rootRestaurantDetails = response.body();
                        snapSharePresenter.response(SnapXResult.SUCCESS, rootRestaurantDetails);
                    }
                }

                @Override
                public void onFailure(Call<RootRestaurantDetails> call, Throwable t) {
                    snapSharePresenter.response(SnapXResult.FAILURE, null);
                }
            });

        } else {
            snapSharePresenter.response(SnapXResult.NONETWORK, null);
        }
    }
}
