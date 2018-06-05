package com.snapxeats.ui.directions;

import android.app.Activity;

import com.snapxeats.common.model.checkin.CheckInRequest;
import com.snapxeats.common.model.checkin.CheckInResponse;
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
 * Created by Prajakta Patil on 22/3/18.
 */
@Singleton
public class DirectionsInteractor {
    private Activity mContext;
    private DirectionsContract.DirectionsPresenter mPresenter;
    private DirectionsContract.DirectionsView mView;

    @Inject
    public DirectionsInteractor() {
    }

    @Inject
    AppUtility utility;

    public void setDirectionsPresenter(DirectionsContract.DirectionsPresenter presenter) {
        this.mPresenter = presenter;
    }

    public void setContext(DirectionsContract.DirectionsView view) {
        this.mView = view;
        this.mContext = view.getActivity();
    }

    void checkIn(CheckInRequest checkInRequest) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<CheckInResponse> checkInPostCall = apiHelper.checkIn(utility.getAuthToken(mContext), checkInRequest);

            checkInPostCall.enqueue(new Callback<CheckInResponse>() {
                @Override
                public void onResponse(Call<CheckInResponse> call, Response<CheckInResponse> response) {
                    mPresenter.response(SnapXResult.SUCCESS, response.body());
                }

                @Override
                public void onFailure(Call<CheckInResponse> call, Throwable t) {
                    mPresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            mPresenter.response(SnapXResult.NONETWORK, CHECKIN);
        }
    }

}
