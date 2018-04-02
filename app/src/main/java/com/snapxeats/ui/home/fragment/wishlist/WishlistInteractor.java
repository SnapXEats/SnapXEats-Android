package com.snapxeats.ui.home.fragment.wishlist;

import android.content.Context;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.foodGestures.RootDeleteWishlist;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.common.model.foodGestures.RootWishlist;
import com.snapxeats.common.model.foodGestures.Wishlist;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Snehal Tembare on 22/3/18.
 */

public class WishlistInteractor {

    private WishlistContract.WishlistPresenter wishlistPresenter;
    private Context mContext;
    private List<Wishlist> wishlist;

    @Inject
    AppUtility utility;

    @Inject
    WishlistDbHelper wishlistDbHelper;

    @Inject
    DbHelper dbHelper;

    @Inject
    public WishlistInteractor() {
    }

    public void setWishlistPresenter(WishlistContract.WishlistPresenter wishlistPresenter) {
        this.wishlistPresenter = wishlistPresenter;
    }

    public void setContext(WishlistContract.WishlistView context) {
        this.mContext = context.getActivity();
        wishlistDbHelper.setContext(mContext);
        dbHelper.setContext(mContext);
    }

    /**
     * Post users gestures
     *
     * @param rootFoodGestures
     */
    void sendUsersGestures(RootFoodGestures rootFoodGestures) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootFoodGestures> call = apiHelper.foodstackGestures(utility.getAuthToken(mContext), rootFoodGestures);
            call.enqueue(new Callback<RootFoodGestures>() {
                @Override
                public void onResponse(Call<RootFoodGestures> call, Response<RootFoodGestures> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.isSuccessful()) {
                            if (0 != wishlistDbHelper.getDeletedWishlist().size()) {
                                sendDeletedWishlist(wishlistDbHelper.getDeletedWishlistObject());
                            } else {
                                getWishlist();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RootFoodGestures> call, Throwable t) {
                    wishlistPresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            wishlistPresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    /**
     * Delete users wishlist
     */
    public void sendDeletedWishlist(RootDeleteWishlist deletedWishlist) {

        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootDeleteWishlist> call = apiHelper.deleteUserWishlist(utility.getAuthToken(mContext),
                    deletedWishlist);
            call.enqueue(new Callback<RootDeleteWishlist>() {
                @Override
                public void onResponse(Call<RootDeleteWishlist> call, Response<RootDeleteWishlist> response) {
                    if (response.isSuccessful()) {
                        //Delete wishlist from local db
                        wishlistDbHelper.deleteLocalWishlist();
                        wishlistPresenter.response(SnapXResult.SUCCESS, null);
                        getWishlist();
                    }
                }

                @Override
                public void onFailure(Call<RootDeleteWishlist> call, Throwable t) {
                    wishlistPresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            wishlistPresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    /**
     * Get users wishlist
     */
    void getWishlist() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootWishlist> wishlistCall = apiHelper.getUserWishlist(utility.getAuthToken(mContext));
            wishlistCall.enqueue(new Callback<RootWishlist>() {
                @Override
                public void onResponse(Call<RootWishlist> call, Response<RootWishlist> response) {
                    if (null != response && response.isSuccessful() && null != response.body()) {
                        wishlistPresenter.response(SnapXResult.SUCCESS, response.body());
                    }
                }

                @Override
                public void onFailure(Call<RootWishlist> call, Throwable t) {
                    wishlistPresenter.response(SnapXResult.FAILURE, null);
                }
            });

        } else {
            wishlistPresenter.response(SnapXResult.NONETWORK, null);
        }
    }
}
