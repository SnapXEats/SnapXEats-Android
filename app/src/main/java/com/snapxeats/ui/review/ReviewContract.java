package com.snapxeats.ui.review;

import android.net.Uri;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Prajakta Patil on 12/4/18.
 */
public class ReviewContract {
    interface ReviewView extends BaseView<ReviewPresenter>, AppContract.SnapXResults {
    }

    interface ReviewPresenter extends BasePresenter<ReviewView> {
        void presentScreen(Router.Screen screen);

        void sendReview(String token, String restId, Uri image, Uri audio, String txtReview, Integer rating);

        void getInstaInfo(String token);

        void getUserdata(SnapXUserRequest snapXUserRequest);
    }

    interface ReviewRouter {
        void presentScreen(Router.Screen screen);

        void setView(ReviewContract.ReviewView view);
    }
}
