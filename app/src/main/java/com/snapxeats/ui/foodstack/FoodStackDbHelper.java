package com.snapxeats.ui.foodstack;

import android.content.Context;

import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.SnapxDataDao;
import com.snapxeats.common.model.foodGestures.FoodDislikes;
import com.snapxeats.common.model.foodGestures.FoodDislikesDao;
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
    private SnapxDataDao snapxDataDao;
    private FoodDislikesDao foodDislikesDao;

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
    }

    public List<FoodWishlists> saveFoodWishlist(List<FoodWishlists> foodWishlists) {
        FoodWishlists item;
        for (FoodWishlists wishlists : foodWishlists) {
            item = new FoodWishlists(wishlists.getRestaurant_dish_id());
            foodWishlistsDao.insert(item);
        }
        return foodWishlists;
    }

    public String saveFoodWishlistCount(String count) {
        if (snapxDataDao.loadAll().size() > 0) {
            List<SnapxData> snapxDataList = snapxDataDao.loadAll();
            snapxDataList.get(0).setFoodWishlistCount(count);
            snapxDataDao.update(snapxDataList.get(0));
        }
        return count;
    }

    public List<FoodDislikes> saveFoodDislikes(List<FoodDislikes> foodDislikes) {
        FoodDislikes item;
        for (FoodDislikes foodGestureDislike : foodDislikes) {
            item = new FoodDislikes(foodGestureDislike.getRestaurant_dish_id());
            foodDislikesDao.insert(item);
        }
        return foodDislikes;
    }
}
