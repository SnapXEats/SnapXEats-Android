package com.snapxeats.common.model.restaurantDetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.snapxeats.common.model.RestaurantDishLabels;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 30/1/18.
 */
@Getter
@Setter
public class RestaurantDishes {
    private List<RestaurantDishLabels> restaurantDishLabels;

    private String dish_image_url;

    private String restaurant_dish_id;
}
