package com.snapxeats.ui.home.fragment.wishlist;

import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Snehal Tembare on 22/3/18.
 */

public class WishlistPresenterImpl implements WishlistContract.WishlistPresenter {

    private WishlistRouterImpl router;
    private WishlistInteractor interactor;
    private WishlistContract.WishlistView wishlistView;

    public WishlistPresenterImpl(WishlistInteractor interactor, WishlistRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void addView(WishlistContract.WishlistView view) {
        wishlistView = view;
        router.setView(view);
        interactor.setContext(view);
    }

    @Override
    public void dropView() {
        wishlistView = null;
    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (null != wishlistView) {
            switch (result) {
                case SUCCESS:
                    wishlistView.success(value);
                    break;
                case FAILURE:
                    wishlistView.error(value);
                    break;
                case NONETWORK:
                    wishlistView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    wishlistView.networkError(value);
                    break;
            }
        }
    }

    @Override
    public void sendUsersGestures(RootFoodGestures rootFoodGestures) {
        interactor.sendUsersGestures(rootFoodGestures);
    }
}
