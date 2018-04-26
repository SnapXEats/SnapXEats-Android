package com.snapxeats.ui.review;

import android.net.Uri;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.dagger.AppContract;

import okhttp3.MultipartBody;

/**
 * Created by Prajakta Patil on 12/4/18.
 */
public class ReviewContract {
    interface ReviewView extends BaseView<ReviewPresenter>, AppContract.SnapXResults {
    }

    interface ReviewPresenter extends BasePresenter<ReviewView> {
        void presentScreen(Router.Screen screen);

        void sendReview(String restId, Uri image, Uri audio, String txtReview, Integer rating);
    }

    interface ReviewRouter {
        void presentScreen(Router.Screen screen);

        void setView(ReviewContract.ReviewView view);
    }
}
