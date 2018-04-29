package com.snapxeats.ui.shareReview;

import android.content.Context;

import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.ui.review.ReviewContract;

import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 23/4/18.
 */
public class ShareReviewInteractor {
    private ShareReviewContract.ShareReviewPresenter mReviewPresenter;
    private Context mContext;

    @Inject
    AppUtility utility;

    @Inject
    public ShareReviewInteractor() {

    }

    public void setShareReviewPresenter(ShareReviewContract.ShareReviewPresenter presenter) {
        this.mReviewPresenter = presenter;

    }

    public void setContext(ShareReviewContract.ShareReviewView view) {
        this.mContext = view.getActivity();
        utility.setContext(view.getActivity());
    }
}