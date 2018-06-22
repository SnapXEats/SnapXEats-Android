package com.snapxeats.ui.home.fragment.home;

import android.app.Activity;
import android.content.Context;

import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.model.UserReward;
import com.snapxeats.common.model.preference.RootCuisine;
import com.snapxeats.common.utilities.AppUtility;
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
import static com.snapxeats.common.utilities.NoNetworkResults.CHECKIN;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

@Singleton
public class HomeFgmtInteractor {

    private HomeFgmtContract.HomeFgmtPresenter homeFgmtPresenter;

    private Context mContext;

    @Inject
    AppUtility utility;

    @Inject
    HomeFgmtInteractor() {
    }

    @Inject
    ApiClient apiClient;

    public void setHomeFgmtPresenter(HomeFgmtContract.HomeFgmtPresenter presenter) {
        this.homeFgmtPresenter = presenter;
    }

    public void setContext(HomeFgmtContract.HomeFgmtView view) {
        this.mContext = view.getActivity();
        utility.setContext(mContext);

    }

    /**
     * get cuisines list
     */
    public void getCuisineList(LocationCuisine locationCuisine) {

        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);

            double lat = locationCuisine.getLatitude();
            double lng = locationCuisine.getLongitude();

            Call<RootCuisine> listCuisineCall = apiHelper.getCuisineList(lat, lng);
            listCuisineCall.enqueue(new Callback<RootCuisine>() {
                @Override
                public void onResponse(Call<RootCuisine> call, Response<RootCuisine> response) {
                    if (response.isSuccessful() && null != response.body()) {
                        RootCuisine rootCuisine = response.body();
                        if (utility.isLoggedIn()) {
                            updateUserRewardPoint();
                        }
                        homeFgmtPresenter.response(SnapXResult.SUCCESS, rootCuisine);
                    }
                }

                @Override
                public void onFailure(Call<RootCuisine> call, Throwable t) {
                    homeFgmtPresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            homeFgmtPresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    private void updateUserRewardPoint() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<UserReward> rewardCall = apiHelper.getUsersRewardPoints(utility.getAuthToken(mContext));

            rewardCall.enqueue(new Callback<UserReward>() {
                @Override
                public void onResponse(Call<UserReward> call, Response<UserReward> response) {
                    if (response.isSuccessful() && null != response.body()) {
                        homeFgmtPresenter.response(SnapXResult.SUCCESS, response.body());
                    }
                }

                @Override
                public void onFailure(Call<UserReward> call, Throwable t) {
                    homeFgmtPresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            homeFgmtPresenter.response(SnapXResult.NONETWORK, CHECKIN);
        }
    }
}
