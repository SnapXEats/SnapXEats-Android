package com.snapxeats.ui.home;

import android.app.Activity;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.SnapxDataDao;
import com.snapxeats.common.model.preference.DaoSession;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserCuisinePreferences;
import com.snapxeats.common.model.preference.UserCuisinePreferencesDao;
import com.snapxeats.common.model.preference.UserFoodPreferences;
import com.snapxeats.common.model.preference.UserFoodPreferencesDao;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.model.preference.UserPreferenceDao;
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
 * Created by Snehal Tembare on 28/2/18.
 */

public class HomeInteractor {
    private Activity mContext;
    @Inject
    AppUtility utility;

    @Inject
    RootUserPreference rootUserPreference;

    private UserPreferenceDao userPreferenceDao;
    private UserCuisinePreferencesDao cuisineDao;
    private UserFoodPreferencesDao foodDao;

    @Inject
    public HomeInteractor() {
    }

    private HomeContract.HomePresenter homePresenter;
    private SnapxDataDao snapxDataDao;
    private DaoSession daoSession;

    public void setContext(HomeContract.HomeView context) {
        this.mContext = context.getActivity();
        utility.setContext(context.getActivity());
        daoSession = ((SnapXApplication) mContext.getApplication()).getDaoSession();
        snapxDataDao = daoSession.getSnapxDataDao();
    }

    public void setHomePresenter(HomeContract.HomePresenter homePresenter) {
        this.homePresenter = homePresenter;
    }

    public List<SnapxData> getUserInfoFromDb() {
        return snapxDataDao.loadAll();
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
        UserPreferenceDao userPreferenceDao = daoSession.getUserPreferenceDao();
        userPreferenceDao.insertOrReplaceInTx(userPreference);
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
        userPreferenceDao = daoSession.getUserPreferenceDao();
        cuisineDao = daoSession.getUserCuisinePreferencesDao();
        foodDao = daoSession.getUserFoodPreferencesDao();

        if (userPreferenceDao != null && userPreferenceDao.loadAll().size() != 0) {

            UserPreference userPreference = userPreferenceDao.loadAll().get(0);

            rootUserPreference.setUser_Id(userPreference.getId());
            if (null != userPreference.getRestaurant_rating() &&
                    !userPreference.getRestaurant_rating().isEmpty()) {
                rootUserPreference.setRestaurant_rating(userPreference.getRestaurant_rating());
            } else {
                rootUserPreference.setRestaurant_rating(String.valueOf(0));
            }

            if (null != userPreference.getRestaurant_distance() &&
                    !userPreference.getRestaurant_distance().isEmpty()) {
                rootUserPreference.setRestaurant_distance(userPreference.getRestaurant_distance());
            } else {
                rootUserPreference.setRestaurant_rating(String.valueOf(1));
            }

            if (null != userPreference.getRestaurant_price() &&
                    !userPreference.getRestaurant_price().isEmpty()) {
                rootUserPreference.setRestaurant_price(userPreference.getRestaurant_price());
            } else {
                rootUserPreference.setRestaurant_price(String.valueOf(0));
            }

            if (userPreference.getSort_by_distance()) {
                rootUserPreference.setSort_by_distance(userPreference.getSort_by_distance());
            } else if (userPreference.getSort_by_rating()) {
                rootUserPreference.setSort_by_rating(userPreference.getSort_by_rating());
            } else {
                rootUserPreference.setSort_by_distance(true);
            }


            List<UserCuisinePreferences> selectedCuisineList = cuisineDao.queryBuilder().
                    whereOr(UserCuisinePreferencesDao.Properties.Is_cuisine_favourite.eq(1),
                            (UserCuisinePreferencesDao.Properties.Is_cuisine_like.eq(1))).list();

            rootUserPreference.setUserCuisinePreferences(selectedCuisineList);

            List<UserFoodPreferences> selectedFoodList = foodDao.queryBuilder().
                    whereOr(UserFoodPreferencesDao.Properties.Is_food_favourite.eq(1),
                            (UserFoodPreferencesDao.Properties.Is_food_like.eq(1))).list();

            rootUserPreference.setUserFoodPreferences(selectedFoodList);

        }
        return rootUserPreference != null ? rootUserPreference : null;
    }
}
