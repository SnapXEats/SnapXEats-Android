package com.snapxeats.ui.home;

import android.content.Context;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.foodGestures.RootDeleteWishlist;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;
import com.snapxeats.ui.home.fragment.wishlist.WishlistDbHelper;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Snehal Tembare on 28/2/18.
 */

public class HomeInteractor {
    private Context mContext;

    @Inject
    AppUtility utility;

    @Inject
    HomeDbHelper homeDbHelper;

    @Inject
    DbHelper dbHelper;

    @Inject
    WishlistDbHelper wishlistDbHelper;

    @Inject
    RootUserPreference rootUserPreference;

    @Inject
    public HomeInteractor() {
    }

    private HomeContract.HomePresenter homePresenter;

    public void setContext(HomeContract.HomeView context) {
        this.mContext = context.getActivity();
        utility.setContext(context.getActivity());
        homeDbHelper.setContext(mContext);
        dbHelper.setContext(mContext);
    }

    public void setHomePresenter(HomeContract.HomePresenter homePresenter) {
        this.homePresenter = homePresenter;
    }

    public List<SnapxData> getUserInfoFromDb() {
        return homeDbHelper.getUserInfoFromDb();
    }

    /**
     * PUT- Update user preferences
     */
    public void updatePreferences(UserPreference userPreference) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<UserPreference> userPreferenceCall = apiHelper.updateUserPreferences(utility.getAuthToken(mContext), userPreference);
            userPreferenceCall.enqueue(new Callback<UserPreference>() {
                @Override
                public void onResponse(Call<UserPreference> call, Response<UserPreference> response) {
                    if (response.isSuccessful()) {
                        homePresenter.response(SnapXResult.SUCCESS, response.body());
                    }
                }

                @Override
                public void onFailure(Call<UserPreference> call, Throwable t) {
                    homePresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            homePresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    public void saveDataInLocalDb(UserPreference userPreference) {
        homeDbHelper.saveDataInLocalDb(userPreference);
    }

    /**
     * POST- Save user preferences
     */

    public void applyPreferences(UserPreference userPreference) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<UserPreference> userPreferenceCall = apiHelper.setUserPreferences(utility.getAuthToken(mContext), userPreference);
            userPreferenceCall.enqueue(new Callback<UserPreference>() {
                @Override
                public void onResponse(Call<UserPreference> call, Response<UserPreference> response) {
                    if (response.isSuccessful()) {
                        homePresenter.response(SnapXResult.SUCCESS, response.body());
                    }
                }

                @Override
                public void onFailure(Call<UserPreference> call, Throwable t) {
                    homePresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            homePresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    public RootUserPreference getUserPreferenceFromDb() {
        return homeDbHelper.getUserPreferenceFromDb();
    }

    public void sendUserGestures(RootFoodGestures rootFoodGestures) {
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
                                logout();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<RootFoodGestures> call, Throwable t) {
                    homePresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            homePresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    /**
     * Delete users wishlist
     */
    private void sendDeletedWishlist(RootDeleteWishlist deletedWishlist) {

        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootDeleteWishlist> call = apiHelper.deleteUserWishlist(utility.getAuthToken(mContext),
                    deletedWishlist);
            call.enqueue(new Callback<RootDeleteWishlist>() {
                @Override
                public void onResponse(Call<RootDeleteWishlist> call, Response<RootDeleteWishlist> response) {
                    if (response.isSuccessful()) {
                        logout();
                    }
                }

                @Override
                public void onFailure(Call<RootDeleteWishlist> call, Throwable t) {
                    homePresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            homePresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    private void logout() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<String> logoutCall = apiHelper.logout(utility.getAuthToken(mContext));

            logoutCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    //Delete local db
                    clearLocalDb();

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });

        } else {
            homePresenter.response(SnapXResult.NONETWORK, null);

        }
    }

    private void clearLocalDb() {
        dbHelper.getDaoSesion().getSnapxDataDao().deleteAll();
        dbHelper.getDaoSesion().getFoodWishlistsDao().deleteAll();
        dbHelper.getDaoSesion().getFoodDislikesDao().deleteAll();
        dbHelper.getDaoSesion().getFoodLikesDao().deleteAll();
    }
}
