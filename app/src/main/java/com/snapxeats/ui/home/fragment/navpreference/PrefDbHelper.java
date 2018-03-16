package com.snapxeats.ui.home.fragment.navpreference;

import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserCuisinePreferences;
import com.snapxeats.common.model.preference.UserCuisinePreferencesDao;
import com.snapxeats.common.model.preference.UserFoodPreferences;
import com.snapxeats.common.model.preference.UserFoodPreferencesDao;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.model.preference.UserPreferenceDao;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 4/3/18.
 */


@Singleton
public class PrefDbHelper {

    private RootUserPreference userPreferenceFromDb;

    @Inject
    public PrefDbHelper() {
    }


    public List<UserCuisinePreferences> getSelectedCuisineList(UserCuisinePreferencesDao cuisinePreferencesDao) {
        List<UserCuisinePreferences> selectedCuisineList = cuisinePreferencesDao.queryBuilder().
                whereOr(UserCuisinePreferencesDao.Properties.Is_cuisine_favourite.eq(1),
                        (UserCuisinePreferencesDao.Properties.Is_cuisine_like.eq(1))).list();
        return selectedCuisineList != null ? selectedCuisineList : null;
    }

    public List<UserFoodPreferences> getSelectedFoodList(UserFoodPreferencesDao foodPreferencesDao) {
        List<UserFoodPreferences> selectedFoodList = foodPreferencesDao.queryBuilder().
                whereOr(UserFoodPreferencesDao.Properties.Is_food_favourite.eq(1),
                        (UserFoodPreferencesDao.Properties.Is_food_like.eq(1))).list();
        return selectedFoodList != null ? selectedFoodList : null;
    }


    public List<UserPreference> getUserPreferenceList(UserPreferenceDao userPreferenceDao) {
        return userPreferenceDao.loadAll() != null ? userPreferenceDao.loadAll() : null;
    }


    public RootUserPreference getUserPreferenceFromDb() {
        return userPreferenceFromDb;
    }

    public UserPreference mapLocalObject(RootUserPreference mRootUserPreference) {
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
}

