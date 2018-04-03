package com.snapxeats.common.model.foodGestures;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 30/3/18.
 */

@Getter
@Setter

public class UserWishlist {
    private String restaurant_dish_id;

    public UserWishlist(String restaurant_dish_id) {
        this.restaurant_dish_id = restaurant_dish_id;
    }
}
