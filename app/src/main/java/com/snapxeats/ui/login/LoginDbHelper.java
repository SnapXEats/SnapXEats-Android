package com.snapxeats.ui.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserCuisinePreferences;
import com.snapxeats.common.model.preference.UserCuisinePreferencesDao;
import com.snapxeats.common.model.preference.UserFoodPreferences;
import com.snapxeats.common.model.preference.UserFoodPreferencesDao;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.model.preference.UserPreferenceDao;
import com.snapxeats.common.model.preference.UserPreferences;
import com.snapxeats.common.utilities.AppUtility;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 4/4/18.
 */

@Singleton

public class LoginDbHelper {
    private Context mContext;

    @Inject
    public LoginDbHelper() {
    }

    @Inject
    AppUtility utility;

    @Inject
    DbHelper dbHelper;

    @Inject
    RootUserPreference mRootUserPreference;

    public void setContext(Context context) {
        this.mContext = context;
        dbHelper.setContext(context);
        utility.setContext(context);
    }

    public void saveUserPrefDataInDb(UserPreferences userPreferences) {
        SharedPreferences preferences = utility.getSharedPreferences();
        String userId = preferences.getString(mContext.getString(R.string.user_id), "");
        UserPreference userPreference = new UserPreference(userId,
                String.valueOf(userPreferences.getRestaurant_rating()),
                String.valueOf(userPreferences.getRestaurant_price()),
                String.valueOf(userPreferences.getRestaurant_distance()),
                userPreferences.isSort_by_distance(),
                userPreferences.isSort_by_rating(),
                userPreferences.getUserCuisinePreferences(),
                userPreferences.getUserFoodPreferences());

        UserPreferenceDao userPreferenceDao = dbHelper.getUserPreferenceDao();
        userPreferenceDao.insertOrReplace(userPreference);

        mRootUserPreference = getUserPreferenceFromDb(userPreferences);


    }

    RootUserPreference getUserPreferenceFromDb(UserPreferences userPreference) {

        SharedPreferences preferences = utility.getSharedPreferences();
        String userId = preferences.getString(mContext.getString(R.string.user_id), "");
        mRootUserPreference.setUser_Id(userId);

        if (null != userPreference) {
            if (null != userPreference.getRestaurant_rating() &&
                    !userPreference.getRestaurant_rating().isEmpty()) {
                mRootUserPreference.setRestaurant_rating(userPreference.getRestaurant_rating());
            } else {
                mRootUserPreference.setRestaurant_rating(String.valueOf(0));
            }

            if (null != userPreference.getRestaurant_distance() &&
                    !userPreference.getRestaurant_distance().isEmpty()) {
                mRootUserPreference.setRestaurant_distance(userPreference.getRestaurant_distance());
            } else {
                mRootUserPreference.setRestaurant_distance(String.valueOf(1));
            }

            if (null != userPreference.getRestaurant_price() &&
                    !userPreference.getRestaurant_price().isEmpty()) {
                mRootUserPreference.setRestaurant_price(userPreference.getRestaurant_price());
            } else {
                mRootUserPreference.setRestaurant_price(String.valueOf(0));
            }

            mRootUserPreference.setSort_by_distance(userPreference.isSort_by_distance());
            mRootUserPreference.setSort_by_rating(userPreference.isSort_by_rating());

            saveCuisineList(userPreference.getUserCuisinePreferences());
            mRootUserPreference.setUserCuisinePreferences(userPreference.getUserCuisinePreferences());

            saveFoodPrefList(userPreference.getUserFoodPreferences());
            mRootUserPreference.setUserFoodPreferences(userPreference.getUserFoodPreferences());

        }
        return mRootUserPreference;
    }

    private void saveCuisineList(List<UserCuisinePreferences> rootCuisineList) {

        UserCuisinePreferencesDao cuisineDao = dbHelper.getUserCuisinePreferencesDao();

        cuisineDao.deleteAll();
        UserCuisinePreferences cuisinePreferences = null;
        SharedPreferences preferences = utility.getSharedPreferences();
        String userId = preferences.getString(mContext.getString(R.string.user_id), "");

        for (UserCuisinePreferences cuisines : rootCuisineList) {
            if (cuisines.getIs_cuisine_like() || cuisines.getIs_cuisine_favourite()) {

                cuisinePreferences = new UserCuisinePreferences(cuisines.getCuisine_info_id(),
                        cuisines.getIs_cuisine_like(), cuisines.getIs_cuisine_favourite(), userId);
                cuisineDao.insertOrReplace(cuisinePreferences);
            }
        }
    }

    void saveFoodPrefList(List<UserFoodPreferences> foodPrefList) {
        UserFoodPreferencesDao foodPrefDao = dbHelper.getUserFoodPreferencesDao();
        foodPrefDao.deleteAll();
        UserFoodPreferences foodPreferences = null;
        SharedPreferences preferences = utility.getSharedPreferences();
        String userId = preferences.getString(mContext.getString(R.string.user_id), "");

        for (UserFoodPreferences foodPref : foodPrefList) {
            if (foodPref.getIs_food_like() || foodPref.getIs_food_favourite()) {
                foodPreferences = new UserFoodPreferences(foodPref.getFood_type_info_id(),
                        foodPref.getIs_food_like(), foodPref.getIs_food_favourite(), userId);
                foodPrefDao.insertOrReplace(foodPreferences);
            }
        }
    }

}
