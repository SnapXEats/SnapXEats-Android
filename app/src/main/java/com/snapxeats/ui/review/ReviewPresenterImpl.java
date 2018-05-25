package com.snapxeats.ui.review;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.snapxeats.common.Router;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Prajakta Patil on 12/4/18.
 */
public class ReviewPresenterImpl implements ReviewContract.ReviewPresenter {
    private ReviewInteractor mReviewInteractor;

    @Nullable
    private ReviewContract.ReviewView mReviewView;

    private ReviewRouterImpl mInfoRouter;

    ReviewPresenterImpl(ReviewInteractor mInteractor, ReviewRouterImpl router) {
        this.mReviewInteractor = mInteractor;
        this.mInfoRouter = router;
    }

    @Override
    public void addView(ReviewContract.ReviewView view) {
        mReviewView = view;
        mInfoRouter.setView(view);
        mReviewInteractor.setContext(view);
    }

    @Override
    public void dropView() {
        mReviewView = null;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        mInfoRouter.presentScreen(screen);
    }

    @Override
    public void sendReview(String token, String restId, Uri image, Uri audio, String txtReview, Integer rating) {
        mReviewInteractor.sendReview(token, restId, image, audio, txtReview, rating);
    }

    @Override
    public void getInstaInfo(String token) {
        mReviewInteractor.getInstaInfo(token);
    }

    @Override
    public void getUserdata(SnapXUserRequest snapXUserRequest) {
        mReviewInteractor.getUserData(snapXUserRequest);
    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (mReviewView != null) {
            switch (result) {
                case SUCCESS:
                    mReviewView.success(value);
                    break;
                case FAILURE:
                    mReviewView.error(value);
                    break;
                case NONETWORK:
                    mReviewView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    mReviewView.networkError(value);
                    break;
            }
        }
    }
}