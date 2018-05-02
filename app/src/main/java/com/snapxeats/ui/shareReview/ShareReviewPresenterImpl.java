package com.snapxeats.ui.shareReview;

import android.support.annotation.Nullable;

import com.snapxeats.common.Router;
import com.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Prajakta Patil on 23/4/18.
 */
public class ShareReviewPresenterImpl implements ShareReviewContract.ShareReviewPresenter {
    private ShareReviewInteractor mReviewInteractor;

    @Nullable
    private ShareReviewContract.ShareReviewView mReviewView;

    private ShareReviewRouterImpl mRouter;

    ShareReviewPresenterImpl(ShareReviewInteractor mInteractor, ShareReviewRouterImpl router) {
        this.mReviewInteractor = mInteractor;
        this.mRouter = router;
    }

    @Override
    public void addView(ShareReviewContract.ShareReviewView view) {
        mReviewView = view;
        mRouter.setView(view);
        mReviewInteractor.setContext(view);
    }

    @Override
    public void dropView() {
        mReviewView = null;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        mRouter.presentScreen(screen);
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