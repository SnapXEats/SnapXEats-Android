package com.snapxeats.ui.home;

import android.content.Context;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.Logout;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.checkin.CheckInRequest;
import com.snapxeats.common.model.checkin.CheckInResponse;
import com.snapxeats.common.model.checkin.CheckInRestaurants;
import com.snapxeats.common.model.foodGestures.RootDeleteWishlist;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.model.smartphotos.SmartPhotoResponse;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;
import com.snapxeats.ui.home.fragment.snapnshare.CameraActivity;
import com.snapxeats.ui.home.fragment.wishlist.WishlistDbHelper;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.snapxeats.common.constants.WebConstants.BASE_URL;
import static com.snapxeats.common.utilities.NoNetworkResults.CHECKIN;
import static com.snapxeats.common.utilities.NoNetworkResults.CHECKIN_RESTAURANTS;
import static com.snapxeats.common.utilities.NoNetworkResults.SMART_PHOTO;

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
    HomeInteractor() {
    }

    private HomeContract.HomePresenter homePresenter;

    public void setContext(HomeContract.HomeView context) {
        this.mContext = context.getActivity();
        utility.setContext(context.getActivity());
        homeDbHelper.setContext(mContext);
        dbHelper.setContext(mContext);
    }

    void setHomePresenter(HomeContract.HomePresenter homePresenter) {
        this.homePresenter = homePresenter;
    }

    List<SnapxData> getUserInfoFromDb() {
        return homeDbHelper.getUserInfoFromDb();
    }

    /**
     * PUT- Update user preferences
     */
    void updatePreferences(UserPreference userPreference) {
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

    void saveDataInLocalDb(UserPreference userPreference) {
        homeDbHelper.saveDataInLocalDb(userPreference);
    }

    /**
     * POST- Save user preferences
     */

    void applyPreferences(UserPreference userPreference) {
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

    RootUserPreference getUserPreferenceFromDb() {
        return homeDbHelper.getUserPreferenceFromDb();
    }

    void sendUserGestures(RootFoodGestures rootFoodGestures) {
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

    /**
     * Logout user from app
     */

    private void logout() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<Logout> logoutCall = apiHelper.logout(utility.getAuthToken(mContext));

            logoutCall.enqueue(new Callback<Logout>() {
                @Override
                public void onResponse(Call<Logout> call, Response<Logout> response) {
                    //Delete local db
                    if (response.isSuccessful()) {
                        clearLocalDb();
                        homePresenter.response(SnapXResult.SUCCESS, true);
                    }
                }

                @Override
                public void onFailure(Call<Logout> call, Throwable t) {
                    homePresenter.response(SnapXResult.ERROR, null);
                }
            });

        } else {
            homePresenter.response(SnapXResult.NONETWORK, null);

        }
    }

    private void clearLocalDb() {
        utility.getSharedPreferences().edit().clear().apply();

        dbHelper.getDaoSesion().getSnapxDataDao().deleteAll();
        dbHelper.getDaoSesion().getFoodWishlistsDao().deleteAll();
        dbHelper.getDaoSesion().getFoodDislikesDao().deleteAll();
        dbHelper.getDaoSesion().getFoodLikesDao().deleteAll();
        dbHelper.getDaoSesion().getUserFoodPreferencesDao().deleteAll();
        dbHelper.getDaoSesion().getUserCuisinePreferencesDao().deleteAll();
        dbHelper.getDaoSesion().getUserPreferenceDao().deleteAll();
        dbHelper.getDaoSesion().clear();

        SnapXApplication app = (SnapXApplication) mContext.getApplicationContext();
        app.setToken(null);
        rootUserPreference.resetRootUserPreference();
    }

    void getNearByRestaurantToCheckIn(double lat, double lng) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<CheckInRestaurants> checkInRestaurantsCall = apiHelper.getRestaurantsForCheckIn(lat, lng);
            checkInRestaurantsCall.enqueue(new Callback<CheckInRestaurants>() {
                @Override
                public void onResponse(Call<CheckInRestaurants> call, Response<CheckInRestaurants> response) {
                    if (response.isSuccessful() && null != response.body() && null != response.body().getRestaurants_info()) {
                        homePresenter.response(SnapXResult.SUCCESS, response.body());
                    }
                }

                @Override
                public void onFailure(Call<CheckInRestaurants> call, Throwable t) {
                    homePresenter.response(SnapXResult.FAILURE, null);
                }
            });

        } else {
            homePresenter.response(SnapXResult.NONETWORK, CHECKIN_RESTAURANTS);
        }
    }

    void checkIn(CheckInRequest checkInRequest) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<CheckInResponse> checkInPostCall = apiHelper.checkIn(utility.getAuthToken(mContext), checkInRequest);

            checkInPostCall.enqueue(new Callback<CheckInResponse>() {
                @Override
                public void onResponse(Call<CheckInResponse> call, Response<CheckInResponse> response) {
                    homePresenter.response(SnapXResult.SUCCESS, response.body());
                }

                @Override
                public void onFailure(Call<CheckInResponse> call, Throwable t) {
                    homePresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            homePresenter.response(SnapXResult.NONETWORK, CHECKIN);
        }
    }

    public void getSmartPhotoInfo(String dishId) {
        if (NetworkUtility.isNetworkAvailable(mContext)){
            ApiHelper apiHelper = ApiClient.getClient(mContext,BASE_URL).create(ApiHelper.class);
            Call<SmartPhotoResponse> smartPhotoResponseCall = apiHelper.getSmartPhotoDetails(dishId);

            smartPhotoResponseCall.enqueue(new Callback<SmartPhotoResponse>() {
                @Override
                public void onResponse(Call<SmartPhotoResponse> call, Response<SmartPhotoResponse> response) {
                   homePresenter.response(SnapXResult.SUCCESS,response.body());
                }

                @Override
                public void onFailure(Call<SmartPhotoResponse> call, Throwable t) {
                    homePresenter.response(SnapXResult.SUCCESS,null);
                }
            });

        }else {
            homePresenter.response(SnapXResult.SUCCESS,SMART_PHOTO);
        }

    }
}
