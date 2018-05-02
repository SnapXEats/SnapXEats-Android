package com.snapxeats.ui.shareReview;

import com.snapxeats.dagger.ActivityScoped;
import com.snapxeats.ui.review.ReviewActivity;
import com.snapxeats.ui.review.ReviewModule;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Prajakta Patil on 23/4/18.
 */
@ActivityScoped
@Subcomponent(modules = ShareReviewModule.class)
public interface ShareReviewComponent extends AndroidInjector<ShareReviewActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<ShareReviewActivity> {
    }
}