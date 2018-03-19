package com.snapxeats.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.snapxeats.R;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.model.DaoSession;
import com.snapxeats.common.model.SnapXUser;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.model.SnapXUserResponse;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.SnapxDataDao;
import com.snapxeats.common.model.login.RootInstagram;
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

/**
 * Created by Prajakta Patil on 4/1/18.
 */

@Singleton
public class LoginInteractor {

    private LoginContract.LoginView mLoginLoginView;
    private LoginContract.LoginPresenter mLoginPresenter;
    private SnapxDataDao snapxDataDao;
    private DaoSession daoSession;
    private SnapxData snapxData;
    private Context mContext;
    private RootInstagram rootInstagram;
    private String token;

    @Inject
    public AppUtility appUtility;

    @Inject
    public LoginInteractor() {

    }

    public void setLoginView(LoginContract.LoginView loginLoginView) {
        mLoginLoginView = loginLoginView;
    }

    public void setPresenter(LoginContract.LoginPresenter loginPresenter) {
        mLoginPresenter = loginPresenter;
    }

    public void setContext(LoginContract.LoginView view) {
        this.mContext = view.getActivity();
        this.appUtility.setContext(view.getActivity());
        daoSession = ((SnapXApplication) mContext.getApplicationContext()).getDaoSession();
        snapxDataDao = daoSession.getSnapxDataDao();
        snapxData = new SnapxData();
    }

    //get instagram info
    public void getInstaInfo(String token) {
        this.token = token;
        ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
        Call<RootInstagram> snapXUserCall = apiHelper.getInstagramInfo(token);
        snapXUserCall.enqueue(new Callback<RootInstagram>() {
            @Override
            public void onResponse(Call<RootInstagram> call, Response<RootInstagram> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rootInstagram = response.body();
                    SnapXUserRequest snapXUserRequest = new SnapXUserRequest(rootInstagram.getInstagramToken(),
                            mContext.getString(R.string.platform_instagram), rootInstagram.getData().getId());
                    getUserData(snapXUserRequest);
                }
            }

            @Override
            public void onFailure(Call<RootInstagram> call, Throwable t) {
            }
        });
    }

    /**
     * get user info
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
                    if (response.isSuccessful() && response.body() != null) {
                        SnapXUserResponse snapXUser = response.body();
                        /** save userId to shared preferences **/
                        SharedPreferences settings = appUtility.getSharedPreferences();
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(mContext.getString(R.string.user_id), snapXUser.getUserInfo().getUser_id());
                        editor.putBoolean(mContext.getString(R.string.isFirstTimeUser), snapXUser.getUserInfo().isFirst_time_login());
                        editor.commit();
                        /** save instagram data **/
                        if (snapXUser.getUserInfo().getSocial_platform().
                                equalsIgnoreCase(mContext.getString(R.string.platform_instagram))) {
                            saveInstaDataInDb(snapXUser.getUserInfo(), token, rootInstagram);
                        }
                        /** save facebook data **/
                        if (snapXUser.getUserInfo().getSocial_platform().
                                equalsIgnoreCase(mContext.getString(R.string.platform_facebook))) {
                            saveFbDataInDb(snapXUser.getUserInfo());
                        }
                        mLoginPresenter.response(SnapXResult.SUCCESS, snapXUser);
                    }
                }

                @Override
                public void onFailure(Call<SnapXUserResponse> call, Throwable t) {
                    mLoginPresenter.response(SnapXResult.FAILURE, null);
                }
            });
        } else {
            mLoginPresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    //save data to db
    private void saveInstaDataInDb(SnapXUser snapXUser, String token, RootInstagram rootInstagram) {
        //User data from server
        saveServerDataInDb(snapXUser);
        snapxData.setSocialToken(token);
        snapxData.setSocialUserId(rootInstagram.getData().getId());
        snapxData.setUserName(rootInstagram.getData().getFull_name());
        snapxData.setImageUrl(rootInstagram.getData().getProfile_picture());
        if (snapxDataDao.loadAll().size() == 0) {
            snapxDataDao.insert(snapxData);
        } else {
            snapxDataDao.update(snapxData);
        }
    }

    public void saveServerDataInDb(SnapXUser snapXUser) {
        snapxData.setUserId(snapXUser.getUser_id());
        snapxData.setToken(snapXUser.getToken());
        snapxData.setSocialPlatform(snapXUser.getSocial_platform());
        snapxData.setIsFirstTimeUser(snapXUser.isFirst_time_login());
        if (snapXUser.getSocial_platform().equalsIgnoreCase(mContext.getString(R.string.platform_facebook))) {
            String userName = Profile.getCurrentProfile().getFirstName() + " "
                    + Profile.getCurrentProfile().getLastName();
            Uri profileUri = Profile.getCurrentProfile().getProfilePictureUri(50, 50);
            snapxData.setUserName(userName);
            snapxData.setImageUrl(profileUri.toString());
        } else if (snapXUser.getSocial_platform().equalsIgnoreCase(mContext.getString(R.string.platform_instagram))) {
            snapxData.setImageUrl(rootInstagram.getData().getProfile_picture());
            snapxData.setUserName(rootInstagram.getData().getFull_name());
        }
        if (snapxDataDao.loadAll().size() == 0) {
            snapxDataDao.insert(snapxData);
        } else {
            snapxDataDao.update(snapxData);
        }
    }

    public void saveFbDataInDb(SnapXUser snapXUser) {
        saveServerDataInDb(snapXUser);
        snapxData.setUserId(AccessToken.getCurrentAccessToken().getUserId());
        snapxData.setSocialToken(AccessToken.getCurrentAccessToken().getToken());
        if (snapxDataDao.loadAll().size() == 0) {
            snapxDataDao.insert(snapxData);
        } else {
            snapxDataDao.update(snapxData);
        }
    }
}



