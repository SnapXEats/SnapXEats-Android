package com.snapxeats.ui.restaurant;

import android.content.Context;

import com.snapxeats.common.model.RootRestaurantDetails;
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
 * Created by Prajakta Patil on 5/2/18.
 */
public class RestaurantDetailsInteractor {

    private RestaurantDetailsContract.RestaurantDetailsPresenter mRestaurantDetailsPresenter;
    private Context mContext;

    @Inject
    public RestaurantDetailsInteractor() {
    }

    public void setRestaurantDetailsPresenter(RestaurantDetailsContract.RestaurantDetailsPresenter presenter) {
        this.mRestaurantDetailsPresenter = presenter;
    }

    /**
     * get restaurant details
     *
     * @param restaurantId
     */

    public void getRestDetails(String restaurantId) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootRestaurantDetails> snapXUserCall = apiHelper.getRestDetails(restaurantId);
            snapXUserCall.enqueue(new Callback<RootRestaurantDetails>() {
                @Override
                public void onResponse(Call<RootRestaurantDetails> call, Response<RootRestaurantDetails> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        RootRestaurantDetails rootRestaurantDetails = response.body();
                        mRestaurantDetailsPresenter.response(SnapXResult.SUCCESS, rootRestaurantDetails);
                    }
                }

                @Override
                public void onFailure(Call<RootRestaurantDetails> call, Throwable t) {
                    mRestaurantDetailsPresenter.response(SnapXResult.FAILURE, null);
                }
            });
        } else {
            mRestaurantDetailsPresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    public void setContext(RestaurantDetailsContract.RestaurantDetailsView view) {
        this.mContext = view.getActivity();
    }
}
