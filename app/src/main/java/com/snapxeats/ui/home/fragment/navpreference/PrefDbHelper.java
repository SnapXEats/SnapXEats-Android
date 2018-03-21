package com.snapxeats.ui.home.fragment.navpreference;

import android.content.Context;

import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.model.preference.UserPreferenceDao;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 4/3/18.
 */


@Singleton
public class PrefDbHelper {

    @Inject
    PrefDbHelper() {
    }

    @Inject
    DbHelper dbHelper;

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

    public void userPreference(UserPreference userPreference) {
        UserPreferenceDao userPreferenceDao = dbHelper.getUserPreferenceDao();
        userPreferenceDao.insertOrReplaceInTx(userPreference);
    }
}

