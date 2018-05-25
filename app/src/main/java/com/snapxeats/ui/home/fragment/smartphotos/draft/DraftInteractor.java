package com.snapxeats.ui.home.fragment.smartphotos.draft;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.snapxeats.R;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.model.SnapXUserResponse;
import com.snapxeats.common.model.login.RootInstagram;
import com.snapxeats.common.model.review.SnapNShareResponse;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.LoginUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.snapxeats.common.constants.UIConstants.FILE_MEDIATYPE;
import static com.snapxeats.common.constants.UIConstants.SX_BEARER;
import static com.snapxeats.common.constants.UIConstants.TEXT_TYPE;
import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Snehal Tembare on 19/5/18.
 */

public class DraftInteractor {
    private Context mContext;
    private DraftContract.DraftPresenter mpresenter;
    private RootInstagram rootInstagram;
    private String instaToken;
    private String serverToken;
    @Inject
    AppUtility utility;

    @Inject
    LoginUtility loginUtility;

    @Inject
    DraftInteractor() {
    }


    public void setDraftPresenter(DraftContract.DraftPresenter draftPresenter) {
        mpresenter = draftPresenter;
    }

    public void setContext(DraftContract.DraftView view) {
        mContext = view.getActivity();
        utility.setContext(view.getActivity());
        loginUtility.setContext(view.getActivity());
    }

    /**
     * Send review api call
     *
     * @param restId
     * @param image
     * @param audio
     * @param txtReview
     * @param rating
     */
    void sendReview(String token, String restId, Uri image, Uri audio, String txtReview, Integer rating) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            MultipartBody.Part audioUpload = null;
            if (null != audio) {
                String fileAudioPath = utility.getRealPathFromURIPath(audio, mContext);
                File fileAud = new File(fileAudioPath);
                RequestBody mFileAudio = RequestBody.create(MediaType.parse(FILE_MEDIATYPE), fileAud);
                audioUpload = MultipartBody.Part.createFormData
                        (mContext.getString(R.string.review_audioReview), fileAud.getName(), mFileAudio);

            }
            String fileImagePath = utility.getRealPathFromURIPath(image, mContext);
            File fileImg = new File(fileImagePath);
            RequestBody mFileImage = RequestBody.create(MediaType.parse(FILE_MEDIATYPE), fileImg);

            RequestBody review = RequestBody.create(MediaType.parse(TEXT_TYPE), txtReview);
            RequestBody id = RequestBody.create(MediaType.parse(TEXT_TYPE), restId);

            MultipartBody.Part imageUpload = MultipartBody.Part.createFormData
                    (mContext.getString(R.string.review_dishPicture), fileImg.getName(), mFileImage);

            if (utility.isLoggedIn()) {
                serverToken = utility.getAuthToken(mContext);
            } else {
                serverToken = SX_BEARER + token;
            }
            Call<SnapNShareResponse> snapXUserCall = apiHelper.sendUserReview(
                    serverToken, id, imageUpload, audioUpload, review, rating);
            snapXUserCall.enqueue(new Callback<SnapNShareResponse>() {
                @Override
                public void onResponse(Call<SnapNShareResponse> call,
                                       Response<SnapNShareResponse> response) {
                    if (response.isSuccessful() && null != response.body()) {
                        SnapNShareResponse nShareResponse = response.body();
                        mpresenter.response(SnapXResult.SUCCESS, nShareResponse);
                    }
                }

                @Override
                public void onFailure(Call<SnapNShareResponse> call, Throwable t) {
                    mpresenter.response(SnapXResult.FAILURE, null);
                }
            });
        } else {
            mpresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    /**
     * Instagram Info api call
     *
     * @param token
     */
    public void getInstaInfo(String token) {
        this.instaToken = token;
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
            }
        });
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
                        SharedPreferences settings = utility.getSharedPreferences();
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(mContext.getString(R.string.user_id), snapXUser.getUserInfo().getUser_id());
                        editor.putBoolean(mContext.getString(R.string.isFirstTimeUser), snapXUser.getUserInfo().isFirst_time_login());
                        editor.apply();

                        /** save instagram data **/
                        if (snapXUser.getUserInfo().getSocial_platform().
                                equalsIgnoreCase(mContext.getString(R.string.platform_instagram))) {
                            loginUtility.saveInstaDataInDb(snapXUser.getUserInfo(), instaToken, rootInstagram);
                            loginUtility.getUserPreferences(snapXUser.getUserInfo().getToken());
                        }

                        /** save facebook data **/
                        if (snapXUser.getUserInfo().getSocial_platform().
                                equalsIgnoreCase(mContext.getString(R.string.platform_facebook))) {
                            loginUtility.saveFbDataInDb(snapXUser.getUserInfo(), rootInstagram);
                            loginUtility.getUserPreferences(snapXUser.getUserInfo().getToken());
                        }
                        mpresenter.response(SnapXResult.SUCCESS, snapXUser);
                    }
                }

                @Override
                public void onFailure(Call<SnapXUserResponse> call, Throwable t) {
                    mpresenter.response(SnapXResult.FAILURE, null);
                }
            });
        } else {
            mpresenter.response(SnapXResult.NONETWORK, null);
        }
    }

}
