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
        dest.writeList(this.restaurantDishLabels);
        dest.writeString(this.dish_image_url);
        dest.writeString(this.restaurant_dish_id);
    }

    public RestaurantDishes() {
    }

    protected RestaurantDishes(Parcel in) {
        this.restaurantDishLabels = new ArrayList<RestaurantDishLabels>();
        in.readList(this.restaurantDishLabels, RestaurantDishLabels.class.getClassLoader());
        this.dish_image_url = in.readString();
        this.restaurant_dish_id = in.readString();
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
