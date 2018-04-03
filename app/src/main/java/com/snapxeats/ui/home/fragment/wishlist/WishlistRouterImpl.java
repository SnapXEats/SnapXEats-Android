package com.snapxeats.ui.home.fragment.wishlist;

import com.snapxeats.common.Router;

/**
 * Created by Snehal Tembare on 22/3/18.
 */

public class WishlistRouterImpl {

    private WishlistContract.WishlistView view;

    public WishlistRouterImpl(Router router) {

    }

    public void setView(WishlistContract.WishlistView view) {
        this.view = view;
    }
}
