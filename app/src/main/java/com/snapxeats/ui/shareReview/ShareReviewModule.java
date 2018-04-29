package com.snapxeats.ui.shareReview;

import com.snapxeats.common.Router;
import com.snapxeats.ui.review.ReviewContract;
import com.snapxeats.ui.review.ReviewInteractor;
import com.snapxeats.ui.review.ReviewPresenterImpl;
import com.snapxeats.ui.review.ReviewRouterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Prajakta Patil on 23/4/18.
 */

@Module
public abstract class ShareReviewModule {
    @Provides
    static ShareReviewContract.ShareReviewPresenter presenter(ShareReviewInteractor interactor,
                                                         ShareReviewRouterImpl reviewRouter) {
        ShareReviewContract.ShareReviewPresenter reviewPresenter =
                new ShareReviewPresenterImpl(interactor, reviewRouter);
        interactor.setShareReviewPresenter(reviewPresenter);
        return reviewPresenter;
    }

    @Provides
    static ShareReviewRouterImpl provideReviewRouter(Router router) {
        return new ShareReviewRouterImpl(router);
    }
}