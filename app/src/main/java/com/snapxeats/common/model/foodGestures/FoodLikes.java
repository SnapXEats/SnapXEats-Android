package com.snapxeats.common.model.foodGestures;

import org.greenrobot.greendao.annotation.Entity;

import lombok.Getter;
import lombok.Setter;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Prajakta Patil on 27/2/18.
 */
@Getter
@Setter
@Entity
public class FoodLikes {
    private String restaurant_dish_id;

    @Generated(hash = 189771852)
    public FoodLikes(String restaurant_dish_id) {
        this.restaurant_dish_id = restaurant_dish_id;
    }

    @Generated(hash = 668037214)
    public FoodLikes() {
    }

    public String getRestaurant_dish_id() {
        return this.restaurant_dish_id;
    }

    public void setRestaurant_dish_id(String restaurant_dish_id) {
        this.restaurant_dish_id = restaurant_dish_id;
    }
}
