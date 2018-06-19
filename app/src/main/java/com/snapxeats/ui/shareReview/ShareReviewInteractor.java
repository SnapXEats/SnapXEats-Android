package com.snapxeats.ui.shareReview;

import android.content.Context;
import android.content.SharedPreferences;

import com.snapxeats.R;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.model.SnapXUserResponse;
import com.snapxeats.common.model.login.RootInstagram;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.LoginUtility;
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
 * Created by Prajakta Patil on 23/4/18.
 */
public class ShareReviewInteractor {
    private ShareReviewContract.ShareReviewPresenter mReviewPresenter;
    private Context mContext;
    private RootInstagram rootInstagram;
    private String insttaToken;

    @Inject
    public AppUtility appUtility;

    @Inject
    LoginUtility loginUtility;

    @Inject
    public ShareReviewInteractor() {

    }

    public void setShareReviewPresenter(ShareReviewContract.ShareReviewPresenter presenter) {
        this.mReviewPresenter = presenter;

    }

    public void setContext(ShareReviewContract.ShareReviewView view) {
        this.mContext = view.getActivity();
        appUtility.setContext(view.getActivity());
        loginUtility.setContext(view.getActivity());
    }

    /**
     * Instagram Info api call
     *
     * @param token
     */
    public void getInstaInfo(String token) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            this.insttaToken = token;
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootInstagram> snapXUserCall = apiHelper.getInstagramInfo(token);
            snapXUserCall.enqueue(new Callback<RootInstagram>() {
                @Override
                public void onResponse(Call<RootInstagram> call, Response<RootInstagram> response) {
                    if (response.isSuccessful() && null != response.body()) {
                        rootInstagram = response.body();
                        SnapXUserRequest snapXUserRequest = new SnapXUserRequest(rootInstagram.getInstagramToken(),
                                mContext.getString(R.string.platform_instagram), rootInstagram.getData().getId());
                        getUserData(snapXUserRequest);
                    }
                }

                @Override
                public void onFailure(Call<RootInstagram> call, Throwable t) {
                    mReviewPresenter.response(SnapXResult.FAILURE, null);
                }
            });
        } else {
            mReviewPresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    /**
     * get server user info api call
     *
     * @param snapXUserRequest
     */
    public void getUserData(SnapXUserRequest snapXUserRequest) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<SnapXUserResponse> snapXUserCall = apiHelper.getServerToken(snapXUserRequest);

            snapXUserCall.enqueue(new Callback<SnapXUserResponse>() {
                @Override
                public void onResponse(Call<SnapXUserResponse> call, Response<SnapXUserResponse> response) {
                    if (response.isSuccessful() && null != response.body()) {
                        SnapXUserResponse snapXUser = response.body();

                        /** save userId to shared preferences **/
                        SharedPreferences settings = appUtility.getSharedPreferences();
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(mContext.getString(R.string.user_id), snapXUser.getUserInfo().getUser_id());
                        editor.putBoolean(mContext.getString(R.string.isFirstTimeUser), snapXUser.getUserInfo().isFirst_time_login());
                        editor.apply();

                        /** save instagram data **/
                        if (snapXUser.getUserInfo().getSocial_platform().
                                equalsIgnoreCase(mContext.getString(R.string.platform_instagram))) {
                            loginUtility.saveInstaDataInDb(snapXUser.getUserInfo(), insttaToken, rootInstagram);
                            loginUtility.getUserPreferences(snapXUser.getUserInfo().getToken());
                        } else if (snapXUser.getUserInfo().getSocial_platform().
                                equalsIgnoreCase(mContext.getString(R.string.platform_facebook))) {
                            loginUtility.saveFbDataInDb(snapXUser.getUserInfo(), rootInstagram);
                            loginUtility.getUserPreferences(snapXUser.getUserInfo().getToken());
                        }
                        mReviewPresenter.response(SnapXResult.SUCCESS, snapXUser);
                    }
                }

                @Override
                public void onFailure(Call<SnapXUserResponse> call, Throwable t) {
                    mReviewPresenter.response(SnapXResult.FAILURE, null);
                }
            });
        } else {
            mReviewPresenter.response(SnapXResult.NONETWORK, null);
        }
    }
}