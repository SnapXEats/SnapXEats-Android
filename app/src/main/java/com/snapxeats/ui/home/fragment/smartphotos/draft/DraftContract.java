package com.snapxeats.ui.home.fragment.smartphotos.draft;

import android.net.Uri;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 19/5/18.
 */

public class DraftContract {
    interface DraftView extends BaseView<DraftPresenter>, AppContract.SnapXResults {

    }

    public interface DraftPresenter extends BasePresenter<DraftView> {
        void sendReview(String token, String restId, Uri fileImageUri, Uri audioFile, String textReview, int rating);

        void getInstaInfo(String token);

        void getUserdata(SnapXUserRequest snapXUserRequest);
    }
}
