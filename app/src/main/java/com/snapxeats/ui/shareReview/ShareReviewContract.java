package com.snapxeats.ui.shareReview;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Prajakta Patil on 23/4/18.
 */
public class ShareReviewContract {
    interface ShareReviewView extends BaseView<ShareReviewPresenter>, AppContract.SnapXResults {
    }

    interface ShareReviewPresenter extends BasePresenter<ShareReviewView> {
        void presentScreen(Router.Screen screen);

        void getInstaInfo(String token);
    }

    interface ShareReviewRouter {
        void presentScreen(Router.Screen screen);

        void setView(ShareReviewContract.ShareReviewView view);
    }
}
