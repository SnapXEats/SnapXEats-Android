package com.snapxeats.ui.review;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.snapxeats.R;
import com.snapxeats.common.model.review.SnapNShareResponse;
import com.snapxeats.common.utilities.AppUtility;
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

import static com.snapxeats.common.constants.UIConstants.MEDIATYPE_AUD;
import static com.snapxeats.common.constants.UIConstants.MEDIATYPE_IMG;
import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Prajakta Patil on 12/4/18.
 */
public class ReviewInteractor {
    private ReviewContract.ReviewPresenter mReviewPresenter;
    private Context mContext;
    @Inject
    AppUtility utility;

    @Inject
    public ReviewInteractor() {

    }

    public void setReviewPresenter(ReviewContract.ReviewPresenter presenter) {
        this.mReviewPresenter = presenter;

    }

    public void setContext(ReviewContract.ReviewView view) {
        this.mContext = view.getActivity();
        utility.setContext(view.getActivity());
    }

    public void sendReview(String restId, Uri image, Uri audio, String txtReview, Integer rating) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);

            String fileImagePath = getRealPathFromURIPath(image, mContext);
            String fileAudioPath = getRealPathFromURIPath(audio, mContext);
            File fileImg = new File(fileImagePath);
            File fileAud = new File(fileAudioPath);
            RequestBody mFileImage = RequestBody.create(MediaType.parse(MEDIATYPE_IMG), fileImg);
            RequestBody mFileAudio = RequestBody.create(MediaType.parse(MEDIATYPE_AUD), fileAud);
            MultipartBody.Part imageUpload = MultipartBody.Part.createFormData(mContext.getString(R.string.file), fileImg.getName(), mFileImage);
            MultipartBody.Part audioUpload = MultipartBody.Part.createFormData(mContext.getString(R.string.file), fileAud.getName(), mFileAudio);

            Call<SnapNShareResponse> snapXUserCall = apiHelper.sendUserReview(utility.getAuthToken(mContext)
                    , restId, imageUpload, audioUpload, txtReview, rating);
            snapXUserCall.enqueue(new Callback<SnapNShareResponse>() {
                @Override
                public void onResponse(Call<SnapNShareResponse> call,
                                       Response<SnapNShareResponse> response) {
                    if (response.isSuccessful() && null != response.body()) {
                        SnapNShareResponse nShareResponse = response.body();
                        mReviewPresenter.response(SnapXResult.SUCCESS, nShareResponse);
                    }
                }

                @Override
                public void onFailure(Call<SnapNShareResponse> call, Throwable t) {
                    mReviewPresenter.response(SnapXResult.FAILURE, null);
                }
            });
        } else {
            mReviewPresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    private String getRealPathFromURIPath(Uri contentURI, Context mContext) {
        Cursor cursor = mContext.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}
