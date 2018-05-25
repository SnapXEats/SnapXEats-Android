package com.snapxeats.ui.foodstack;

import android.content.Context;

import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.SnapXDataDao;
import com.snapxeats.common.model.foodGestures.FoodDislikes;
import com.snapxeats.common.model.foodGestures.FoodDislikesDao;
import com.snapxeats.common.model.foodGestures.FoodLikes;
import com.snapxeats.common.model.foodGestures.FoodLikesDao;
import com.snapxeats.common.model.foodGestures.FoodWishlists;
import com.snapxeats.common.model.foodGestures.FoodWishlistsDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 19/3/18.
 */

@Singleton
public class FoodStackDbHelper {
    private FoodWishlistsDao foodWishlistsDao;
    private SnapXDataDao snapxDataDao;
    private FoodDislikesDao foodDislikesDao;
    private FoodLikesDao foodLikesDao;

    @Inject
    public FoodStackDbHelper() {
    }

    @Inject
    DbHelper dbHelper;

    public void setContext(Context context) {
        dbHelper.setContext(context);
        foodWishlistsDao = dbHelper.getFoodWishlistsDao();
        snapxDataDao = dbHelper.getSnapxDataDao();
        foodDislikesDao = dbHelper.getFoodDislikesDao();
        foodLikesDao = dbHelper.getFoodLikesDao();
    }

    void saveFoodWishlist(List<FoodWishlists> foodWishlists) {
        FoodWishlists item;
        foodWishlistsDao.deleteAll();
        for (FoodWishlists wishlists : foodWishlists) {
            item = new FoodWishlists(wishlists.getRestaurant_dish_id(), false);
            foodWishlistsDao.insertOrReplace(item);
        }
    }

    public void saveFoodLikes(List<FoodLikes> foodLikes) {
        FoodLikes item;
        foodLikesDao.deleteAll();
        for (FoodLikes likes : foodLikes) {
            item = new FoodLikes(likes.getRestaurant_dish_id());
            foodLikesDao.insert(item);
        }
    }

    public List<FoodWishlists> getFoodWishList() {

        return foodWishlistsDao.loadAll();
    }

    public List<FoodDislikes> getFoodDislikes() {
        return foodDislikesDao.loadAll();
    }


    void saveFoodDislikes(List<FoodDislikes> foodDislikes) {
        FoodDislikes item;
        for (FoodDislikes foodGestureDislike : foodDislikes) {
            item = new FoodDislikes(foodGestureDislike.getRestaurant_dish_id());
            foodDislikesDao.insert(item);
        }
    }

    public List<FoodLikes> getFoodLikes() {
        return foodLikesDao.loadAll();
    }
}
