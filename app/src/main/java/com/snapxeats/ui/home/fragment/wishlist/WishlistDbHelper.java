package com.snapxeats.ui.home.fragment.wishlist;

import android.content.Context;

import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.foodGestures.FoodDislikes;
import com.snapxeats.common.model.foodGestures.FoodLikes;
import com.snapxeats.common.model.foodGestures.FoodWishlists;
import com.snapxeats.common.model.foodGestures.FoodWishlistsDao;
import com.snapxeats.common.model.foodGestures.RootDeleteWishlist;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.common.model.foodGestures.UserWishlist;
import com.snapxeats.ui.foodstack.FoodStackDbHelper;

import net.hockeyapp.android.metrics.model.User;

import org.greenrobot.greendao.query.DeleteQuery;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 22/3/18.
 */

@Singleton
public class WishlistDbHelper {

    private Context mContext;
    private RootFoodGestures mRootFoodGestures;

    @Inject
    public WishlistDbHelper() {
    }

    @Inject
    DbHelper dbHelper;

    @Inject
    FoodStackDbHelper foodStackDbHelper;

    private List<FoodWishlists> foodWishlist;
    private List<FoodDislikes> foodDislikeList;
    private List<FoodLikes> foodLikesList;

    public void setContext(Context context) {
        this.mContext = context;
        dbHelper.setContext(mContext);
        foodStackDbHelper.setContext(context);
    }

    public RootFoodGestures getFoodGestures() {
        foodWishlist = foodStackDbHelper.getFoodWishList();
        foodDislikeList = foodStackDbHelper.getFoodDislikes();
        foodLikesList = foodStackDbHelper.getFoodLikes();

        mRootFoodGestures = new RootFoodGestures();
        mRootFoodGestures.setDislike_dish_array(foodDislikeList);
        mRootFoodGestures.setWishlist_dish_array(foodWishlist);
        mRootFoodGestures.setLike_dish_array(foodLikesList);

        return mRootFoodGestures;
    }

    public List<FoodWishlists> getDeletedWishlist() {

        List<FoodWishlists> deletedWishlist = dbHelper.getFoodWishlistsDao().queryBuilder()
                .where(FoodWishlistsDao.Properties.IsDeleted.eq(1)).list();
        return deletedWishlist;
    }

    public RootDeleteWishlist getDeletedWishlistObject() {

        List<FoodWishlists> deletedWishlist = getDeletedWishlist();

        RootDeleteWishlist rootDeleteWishlist = new RootDeleteWishlist();
        List<UserWishlist> user_wishlist = new ArrayList<>();

        for (FoodWishlists foodWishlists : deletedWishlist) {
            UserWishlist userWishlist = new UserWishlist(foodWishlists.getRestaurant_dish_id());
            user_wishlist.add(userWishlist);
        }
        rootDeleteWishlist.setUser_wishlist(user_wishlist);
        return rootDeleteWishlist;
    }

    void setWishlistItemStatus(String dishid) {
        FoodWishlists foodWishlists = dbHelper.getFoodWishlistsDao().queryBuilder()
                .where(FoodWishlistsDao.Properties.Restaurant_dish_id.eq(dishid)).unique();
        foodWishlists.setIsDeleted(true);
        dbHelper.getFoodWishlistsDao().update(foodWishlists);
    }

    void deleteLocalWishlist() {
        final DeleteQuery<FoodWishlists> tableDeleteQuery = dbHelper.getFoodWishlistsDao().queryBuilder()
                .where(FoodWishlistsDao.Properties.IsDeleted.eq(1)).buildDelete();
        tableDeleteQuery.executeDeleteWithoutDetachingEntities();
    }

    public void saveWishlistDataInDb(List<UserWishlist> userWishList) {
        FoodWishlists wishlistItem = new FoodWishlists();

        for (int index = 0; index < userWishList.size(); index++) {
            wishlistItem.setRestaurant_dish_id(userWishList.get(index).getRestaurant_dish_id());
            wishlistItem.setIsDeleted(false);
            dbHelper.getFoodWishlistsDao().insert(wishlistItem);
        }
    }
}
