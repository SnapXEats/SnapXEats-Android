package com.snapxeats.ui.home.fragment.smartphotos.draft;

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

import static com.snapxeats.common.constants.UIConstants.FILE_MEDIATYPE;
import static com.snapxeats.common.constants.UIConstants.TEXT_TYPE;
import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Snehal Tembare on 19/5/18.
 */

public class DraftInteractor {
    private Context mContext;
    private DraftContract.DraftPresenter mpresenter;


    @Inject
    AppUtility utility;

    @Inject
    public DraftInteractor() {
    }


    public void setDraftPresenter(DraftContract.DraftPresenter draftPresenter) {
        mpresenter = draftPresenter;    }

    public void setContext(DraftContract.DraftView view) {
        mContext = view.getActivity();
        utility.setContext(view.getActivity());
    }

    void sendReview(String restId, Uri image, Uri audio, String txtReview, Integer rating) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            MultipartBody.Part audioUpload = null;
            if (null != audio) {
                String fileAudioPath = getRealPathFromURIPath(audio, mContext);
                File fileAud = new File(fileAudioPath);
                RequestBody mFileAudio = RequestBody.create(MediaType.parse(FILE_MEDIATYPE), fileAud);
                audioUpload = MultipartBody.Part.createFormData
                        (mContext.getString(R.string.review_audioReview), fileAud.getName(), mFileAudio);

            }
            String fileImagePath = getRealPathFromURIPath(image, mContext);
            File fileImg = new File(fileImagePath);
            RequestBody mFileImage = RequestBody.create(MediaType.parse(FILE_MEDIATYPE), fileImg);

            RequestBody review = RequestBody.create(MediaType.parse(TEXT_TYPE), txtReview);
            RequestBody id = RequestBody.create(MediaType.parse(TEXT_TYPE), restId);

            MultipartBody.Part imageUpload = MultipartBody.Part.createFormData
                    (mContext.getString(R.string.review_dishPicture), fileImg.getName(), mFileImage);


            Call<SnapNShareResponse> snapXUserCall = apiHelper.sendUserReview(
                    utility.getAuthToken(mContext)
                    , id, imageUpload, audioUpload, review, rating);
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

    private String getRealPathFromURIPath(Uri contentURI, Context mContext) {
        Cursor cursor = mContext.getContentResolver().query(contentURI, null, null, null, null);
        if (null == cursor) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}
