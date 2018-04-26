package com.snapxeats.ui.review;

import android.app.Activity;

import com.snapxeats.common.Router;

import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 12/4/18.
 */
public class ReviewRouterImpl implements ReviewContract.ReviewRouter {
    private Activity activity;
    private Router router;

    @Inject
    public ReviewRouterImpl(Router router) {
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
    public void setView(ReviewContract.ReviewView view) {
        this.activity = view.getActivity();
    }
}