package com.snapxeats.common.model.foodGestures;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Prajakta Patil on 19/3/18.
 */
@Entity
public class FoodWishlists {
    @Id
    private String restaurant_dish_id;
    private boolean isDeleted;

    @Generated(hash = 1677792644)
    public FoodWishlists(String restaurant_dish_id, boolean isDeleted) {
        this.restaurant_dish_id = restaurant_dish_id;
        this.isDeleted = isDeleted;
    }

    @Generated(hash = 1736300858)
    public FoodWishlists() {
    }

    public String getRestaurant_dish_id() {
        return this.restaurant_dish_id;
    }

    public void setRestaurant_dish_id(String restaurant_dish_id) {
        this.restaurant_dish_id = restaurant_dish_id;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
