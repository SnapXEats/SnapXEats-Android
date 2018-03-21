package com.snapxeats.common.model.foodGestures;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Prajakta Patil on 19/3/18.
 */
@Entity
public class FoodDislikes {
    private String restaurant_dish_id;

    @Generated(hash = 269812200)
    public FoodDislikes(String restaurant_dish_id) {
        this.restaurant_dish_id = restaurant_dish_id;
    }

    @Generated(hash = 14702048)
    public FoodDislikes() {
    }

    public String getRestaurant_dish_id() {
        return this.restaurant_dish_id;
    }

    public void setRestaurant_dish_id(String restaurant_dish_id) {
        this.restaurant_dish_id = restaurant_dish_id;
    }
}
