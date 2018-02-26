package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 30/1/18.
 */
@Getter
@Setter
public class RestaurantDishes implements Parcelable {
    private List<RestaurantDishLabels> restaurantDishLabels;

    private String dish_image_url;

    private String restaurant_dish_id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(restaurantDishLabels);
        dest.writeString(dish_image_url);
        dest.writeString(restaurant_dish_id);
    }

    public RestaurantDishes() {
    }

    protected RestaurantDishes(Parcel in) {
        restaurantDishLabels = new ArrayList<RestaurantDishLabels>();
        in.readList(restaurantDishLabels, RestaurantDishLabels.class.getClassLoader());
        dish_image_url = in.readString();
        restaurant_dish_id = in.readString();
    }

    public static final Parcelable.Creator<RestaurantDishes> CREATOR = new Parcelable.Creator<RestaurantDishes>() {
        @Override
        public RestaurantDishes createFromParcel(Parcel source) {
            return new RestaurantDishes(source);
        }

        @Override
        public RestaurantDishes[] newArray(int size) {
            return new RestaurantDishes[size];
        }
    };
}
