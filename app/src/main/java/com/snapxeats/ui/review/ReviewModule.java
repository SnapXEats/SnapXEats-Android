package com.snapxeats.ui.review;

import com.snapxeats.common.Router;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Prajakta Patil on 12/4/18.
 */
@Module
public abstract class ReviewModule {
    @Provides
    static ReviewContract.ReviewPresenter presenter(ReviewInteractor interactor,
                                                    ReviewRouterImpl reviewRouter) {
        ReviewContract.ReviewPresenter reviewPresenter =
                new ReviewPresenterImpl(interactor, reviewRouter);
        interactor.setReviewPresenter(reviewPresenter);
        return reviewPresenter;
    }

    @Provides
    static ReviewRouterImpl provideReviewRouter(Router router) {
        return new ReviewRouterImpl(router);
    }
}