package com.snapxeats.ui.review;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.snapxeats.common.Router;
import com.snapxeats.common.utilities.SnapXResult;

import okhttp3.MultipartBody;

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
    public void sendReview(String restId, Uri image, Uri audio, String txtReview, Integer rating) {
        mReviewInteractor.sendReview(restId, image, audio, txtReview, rating);
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