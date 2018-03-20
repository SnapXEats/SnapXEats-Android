package com.snapxeats.ui.restaurantInfo;

import android.content.Context;
import com.snapxeats.common.model.restaurantInfo.RootRestaurantInfo;
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
 * Created by Prajakta Patil on 26/2/18.
 */
public class RestaurantInfoInteractor {
    private RestaurantInfoContract.RestaurantInfoPresenter mRestaurantInfoPresenter;
    private Context mContext;

    @Inject
    public RestaurantInfoInteractor() {

    }

    public void setRestaurantInfoPresenter(RestaurantInfoContract.RestaurantInfoPresenter presenter) {
        this.mRestaurantInfoPresenter = presenter;
    }

    public void setContext(RestaurantInfoContract.RestaurantInfoView view) {
        this.mContext = view.getActivity();
    }

    /**
     * get restaurant info
     *
     * @param restaurantId
     */
    public void getRestInfo(String restaurantId) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext,BASE_URL).create(ApiHelper.class);
            Call<RootRestaurantInfo> snapXUserCall = apiHelper.getRestInfo(restaurantId);
            snapXUserCall.enqueue(new Callback<RootRestaurantInfo>() {
                @Override
                public void onResponse(Call<RootRestaurantInfo> call,
                                       Response<RootRestaurantInfo> response) {
                    if (response.isSuccessful() && null!=response.body()) {
                        RootRestaurantInfo rootRestaurantDetails = response.body();
                        mRestaurantInfoPresenter.response(SnapXResult.SUCCESS, rootRestaurantDetails);
                    }
                }
                @Override
                public void onFailure(Call<RootRestaurantInfo> call, Throwable t) {
                    mRestaurantInfoPresenter.response(SnapXResult.FAILURE, null);
                }
            });
        } else {
            mRestaurantInfoPresenter.response(SnapXResult.NONETWORK, null);
        }
    }
}