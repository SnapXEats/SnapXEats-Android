package com.snapxeats.ui.home.fragment.home;

import android.app.Activity;
import android.net.Uri;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.model.DaoSession;
import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.model.SnapXUser;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.model.SnapXUserResponse;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.SnapxDataDao;
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
    private DaoSession daoSession;
    private SnapxDataDao snapxDataDao;

    private Activity mContext;

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
        daoSession = ((SnapXApplication) mContext.getApplication()).getDaoSession();
    }

    /**
     * get cuisines list
     */
    public void getCuisineList(LocationCuisine locationCuisine) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
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
                    if (response.isSuccessful() && response.body() != null) {
                        RootCuisine rootCuisine = response.body();
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

    /**
     * get user info
     *
     * @param snapXUserRequest
     *//*
    void getUserData(SnapXUserRequest snapXUserRequest) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<SnapXUserResponse> snapXUserCall = apiHelper.getServerToken(snapXUserRequest);

            snapXUserCall.enqueue(new Callback<SnapXUserResponse>() {
                @Override
                public void onResponse(Call<SnapXUserResponse> call, Response<SnapXUserResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        SnapXUser snapXUser = response.body().getUserInfo();
                        homeFgmtPresenter.response(SnapXResult.SUCCESS, snapXUser);
                    }
                }

                @Override
                public void onFailure(Call<SnapXUserResponse> call, Throwable t) {
                    homeFgmtPresenter.response(SnapXResult.FAILURE, null);
                }
            });
        } else {
            homeFgmtPresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    public void saveDataInDb(SnapXUser snapXUser) {
        snapxDataDao = daoSession.getSnapxDataDao();

        SnapxData snapxData = new SnapxData();
        //Data from server
        snapxData.setUserId(snapXUser.getUser_id());
        snapxData.setToken(snapXUser.getToken());
        snapxData.setSocialToken(snapXUser.getSocial_platform());
        snapxData.setIsFirstTimeUser(snapXUser.getIsFirstTimeUser());

        String userName = Profile.getCurrentProfile().getFirstName() + " "
                + Profile.getCurrentProfile().getLastName();

        Uri profileUri = Profile.getCurrentProfile().getProfilePictureUri(50, 50);

        snapxData.setSocialToken(AccessToken.getCurrentAccessToken().getToken());
        snapxData.setSocialUserId(AccessToken.getCurrentAccessToken().getUserId());
        snapxData.setUserName(userName);
        snapxData.setUserImage(profileUri.toString());
        snapxDataDao.insert(snapxData);
    }*/
}
