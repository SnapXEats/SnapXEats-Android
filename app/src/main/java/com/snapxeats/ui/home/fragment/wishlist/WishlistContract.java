package com.snapxeats.ui.home.fragment.wishlist;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.model.foodGestures.FoodWishlists;
import com.snapxeats.common.model.foodGestures.RootDeleteWishlist;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.dagger.AppContract;

import java.util.List;

/**
 * Created by Snehal Tembare on 22/3/18.
 */

public class WishlistContract {

    public interface WishlistView extends BaseView<WishlistPresenter>,AppContract.SnapXResults{

    }

    public interface WishlistPresenter extends BasePresenter<WishlistView> {
        void sendUsersGestures(RootFoodGestures rootFoodGestures);

        void sendDeletedWishlist(RootDeleteWishlist deletedWishlistObject);
    }

    public interface WishlistRouter{

    }
}
