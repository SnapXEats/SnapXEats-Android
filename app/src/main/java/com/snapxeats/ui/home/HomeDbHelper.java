package com.snapxeats.ui.home;

import android.content.Context;
import android.content.SharedPreferences;

import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.SnapXData;
import com.snapxeats.common.model.SnapXDataDao;
import com.snapxeats.common.model.foodGestures.FoodWishlists;
import com.snapxeats.common.model.foodGestures.FoodWishlistsDao;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserCuisinePreferences;
import com.snapxeats.common.model.preference.UserCuisinePreferencesDao;
import com.snapxeats.common.model.preference.UserFoodPreferences;
import com.snapxeats.common.model.preference.UserFoodPreferencesDao;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.model.preference.UserPreferenceDao;
import com.snapxeats.common.utilities.AppUtility;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 21/3/18.
 */

@Singleton
public class HomeDbHelper {
    private Context mContext;

    private UserPreferenceDao userPreferenceDao;
    private UserCuisinePreferencesDao cuisineDao;
    private UserFoodPreferencesDao foodDao;

    @Inject
    public HomeDbHelper() {
    }

    @Inject
    DbHelper dbHelper;

    @Inject
    AppUtility utility;

    @Inject
    RootUserPreference rootUserPreference;

    public void setContext(Context context) {
        this.mContext = context;
        dbHelper.setContext(mContext);
        utility.setContext(mContext);
    }

    UserPreference mapLocalObject(RootUserPreference mRootUserPreference) {
        UserPreference userPreference = new UserPreference(mRootUserPreference.getUser_Id(),
                String.valueOf(mRootUserPreference.getRestaurant_rating()),
                String.valueOf(mRootUserPreference.getRestaurant_price()),
                String.valueOf(mRootUserPreference.getRestaurant_distance()),
                mRootUserPreference.isSort_by_distance(),
                mRootUserPreference.isSort_by_rating(),
                mRootUserPreference.getUserCuisinePreferences(),
                mRootUserPreference.getUserFoodPreferences());
        return userPreference;
    }

    RootUserPreference getUserPreferenceFromDb() {
        userPreferenceDao = dbHelper.getUserPreferenceDao();
        cuisineDao = dbHelper.getUserCuisinePreferencesDao();
        foodDao = dbHelper.getUserFoodPreferencesDao();

        SharedPreferences preferences = utility.getSharedPreferences();
        String userId = preferences.getString(mContext.getString(R.string.user_id), "");
        rootUserPreference.setUser_Id(userId);

        if (userPreferenceDao != null && userPreferenceDao.loadAll().size() != 0) {

            UserPreference userPreference = userPreferenceDao.loadAll().get(0);

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
                rootUserPreference.setRestaurant_distance(String.valueOf(1));
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
        return rootUserPreference;
    }

    void saveDataInLocalDb(UserPreference userPreference) {
        UserPreferenceDao userPreferenceDao = dbHelper.getUserPreferenceDao();
        userPreferenceDao.insertOrReplaceInTx(userPreference);
    }

    List<SnapXData> getUserInfoFromDb() {
        SnapXDataDao snapxDataDao = dbHelper.getSnapxDataDao();
        return snapxDataDao.loadAll();
    }

    public int getWishlistCount() {
        List<FoodWishlists> usersWishlist = dbHelper.getFoodWishlistsDao().queryBuilder()
                .where(FoodWishlistsDao.Properties.IsDeleted.eq(0)).list();
        return usersWishlist.size();
    }
}
