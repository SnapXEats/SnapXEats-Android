package com.snapxeats.common;

import android.content.Context;

import com.snapxeats.SnapXApplication;
import com.snapxeats.common.model.SnapXDataDao;
import com.snapxeats.common.model.draft.RestaurantAminitiesDao;
import com.snapxeats.common.model.draft.SnapXDraftPhotoDao;
import com.snapxeats.common.model.foodGestures.FoodDislikesDao;
import com.snapxeats.common.model.foodGestures.FoodLikesDao;
import com.snapxeats.common.model.foodGestures.FoodWishlistsDao;
import com.snapxeats.common.model.preference.DaoSession;
import com.snapxeats.common.model.preference.UserCuisinePreferencesDao;
import com.snapxeats.common.model.preference.UserFoodPreferencesDao;
import com.snapxeats.common.model.preference.UserPreferenceDao;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 19/3/18.
 */
@Singleton
public class DbHelper {
    private Context mContext;
    private DaoSession daoSession;

    @Inject
    public DbHelper() {
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public DaoSession getDaoSesion() {
        daoSession = ((SnapXApplication) mContext.getApplicationContext()).getDaoSession();
        return daoSession;
    }

    public UserCuisinePreferencesDao getUserCuisinePreferencesDao() {
        daoSession = getDaoSesion();
        return daoSession.getUserCuisinePreferencesDao();
    }

    public UserFoodPreferencesDao getUserFoodPreferencesDao() {
        daoSession = getDaoSesion();
        return daoSession.getUserFoodPreferencesDao();
    }

    public SnapXDataDao getSnapxDataDao() {
        daoSession = getDaoSesion();
        return daoSession.getSnapXDataDao();
    }

    public UserPreferenceDao getUserPreferenceDao() {
        daoSession = getDaoSesion();
        return daoSession.getUserPreferenceDao();
    }

    public FoodDislikesDao getFoodDislikesDao() {
        daoSession = getDaoSesion();
        return daoSession.getFoodDislikesDao();
    }

    public FoodWishlistsDao getFoodWishlistsDao() {
        daoSession = getDaoSesion();
        return daoSession.getFoodWishlistsDao();
    }

    public FoodLikesDao getFoodLikesDao() {
        daoSession = getDaoSesion();
        return daoSession.getFoodLikesDao();
    }

    public SnapXDraftPhotoDao getDraftPhotoDao() {
        daoSession = getDaoSesion();
        return daoSession.getSnapXDraftPhotoDao();
    }

    public RestaurantAminitiesDao getRestaurantAminitiesDao() {
        daoSession = getDaoSesion();
        return daoSession.getRestaurantAminitiesDao();
    }
}
