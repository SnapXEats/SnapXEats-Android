package com.snapxeats.ui.shareReview;

import android.app.Activity;

import com.snapxeats.common.Router;

import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 23/4/18.
 */
public class ShareReviewRouterImpl implements ShareReviewContract.ShareReviewRouter {
    private Activity activity;
    private Router router;

    @Inject
    public ShareReviewRouterImpl(Router router) {
        this.router = router;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        this.router = router;
    }

    /**
     * Set view to Presenter
     *
     * @param view
     */
    @Override
    public void setView(ShareReviewContract.ShareReviewView view) {
        this.activity = view.getActivity();
    }
}