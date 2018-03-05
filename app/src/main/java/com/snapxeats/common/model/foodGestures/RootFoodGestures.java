package com.snapxeats.common.model.foodGestures;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 27/2/18.
 */
@Getter
@Setter
public class RootFoodGestures {
    List<FoodGestureWishlist> wishlist_dish_array;
    List<FoodGestureLike> like_dish_array;
    List<FoodGestureDislike> dislike_dish_array;
}
