package com.snapxeats.ui.home.fragment.smartphotos.draft;

import android.net.Uri;

import com.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Snehal Tembare on 19/5/18.
 */

public class DraftPresenterImpl implements DraftContract.DraftPresenter {
    private DraftRouterImpl router;
    private DraftInteractor interactor;
    private DraftContract.DraftView draftView;

    public DraftPresenterImpl(DraftInteractor interactor, DraftRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void addView(DraftContract.DraftView view) {
        draftView = view;
        interactor.setContext(view);
    }

    @Override
    public void dropView() {
draftView = null;
    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (null != draftView) {
            switch (result) {
                case SUCCESS:
                    draftView.success(value);
                    break;
                case FAILURE:
                    draftView.error(value);
                    break;
                case NONETWORK:
                    draftView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    draftView.networkError(value);
                    break;
            }
        }
    }

    @Override
    public void sendReview(String restId, Uri fileImageUri, Uri audioFile, String textReview, int rating) {
        interactor.sendReview(restId,fileImageUri,audioFile,textReview,rating);
    }
}
