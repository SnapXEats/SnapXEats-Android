package com.snapxeats.ui.home.fragment.wishlist;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 22/3/18.
 */

public class WishlistContract {

    public interface WishlistView extends BaseView<WishlistPresenter>,AppContract.SnapXResults{

    }

    public interface WishlistPresenter extends BasePresenter<WishlistView> {
        void sendUsersGestures(RootFoodGestures rootFoodGestures);
    }

    public interface WishlistRouter{

    }
}
