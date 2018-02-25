package com.snapxeats.ui.home.fragment.home;

import android.app.Activity;
import android.util.Log;

import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.model.SnapXUserInfo;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.model.SnapXUserResponse;
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
public class HomeFgmtInteractor {

    private HomeFgmtContract.HomeFgmtPresenter homeFgmtPresenter;

    private Activity mContext;

    @Inject
    HomeFgmtInteractor() {
    }

    @Inject
    ApiClient apiClient;

    public void setHomeFgmtPresenter(HomeFgmtContract.HomeFgmtPresenter presenter) {
        this.homeFgmtPresenter = presenter;
    }

    /**
     * get cuisines list
     */
    public void getCuisineList(HomeFgmtContract.HomeFgmtView homeFgmtView, LocationCuisine locationCuisine) {
        mContext = homeFgmtView.getActivity();
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            homeFgmtView.showProgressDialog();
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
                    homeFgmtView.dismissProgressDialog();
                    if (response.isSuccessful() && response.body() != null) {
                        RootCuisine rootCuisine = response.body();
                        homeFgmtPresenter.response(SnapXResult.SUCCESS,rootCuisine);
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

    /**
     * get user info
     * @param homeFgmtView
     * @param snapXUserRequest
     */
    void getUserData(HomeFgmtContract.HomeFgmtView homeFgmtView, SnapXUserRequest snapXUserRequest){
        mContext= homeFgmtView.getActivity();
        if(NetworkUtility.isNetworkAvailable(mContext)){
            ApiHelper apiHelper=ApiClient.getClient(mContext,BASE_URL).create(ApiHelper.class);
            Call<SnapXUserResponse> snapXUserCall=apiHelper.getUserToken(snapXUserRequest);
            snapXUserCall.enqueue(new Callback<SnapXUserResponse>() {
                @Override
                public void onResponse(Call<SnapXUserResponse> call, Response<SnapXUserResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        SnapXUserInfo snapXUserInfo = response.body().getUserInfo();
                        Log.d("---**ServerToken",snapXUserInfo.getToken());
                        Log.d("---**ServerId",snapXUserInfo.getUser_id());

                    }
                }

                @Override
                public void onFailure(Call<SnapXUserResponse> call, Throwable t) {
                    homeFgmtPresenter.response(SnapXResult.FAILURE,null);
                }
            });
        }else {
            homeFgmtPresenter.response(SnapXResult.NONETWORK,null);
        }
    }
}
