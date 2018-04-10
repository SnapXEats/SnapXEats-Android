package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.snapxeats.common.model.restaurantDetails.RestaurantDishes;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 30/1/18.
 */
@Getter
@Setter
public class DishesInfo implements Parcelable {

    private List<RestaurantDishes> restaurantDishes;

    private String restaurant_info_id;

    private String restaurant_name;

    private String location_lat;
    private String location_long;
    private String restaurant_price;
    private String restaurant_rating;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(restaurantDishes);
        dest.writeString(restaurant_info_id);
        dest.writeString(restaurant_name);
        dest.writeString(location_lat);
        dest.writeString(location_long);
        dest.writeString(restaurant_price);
        dest.writeString(restaurant_rating);
    }

    public DishesInfo() {
    }

    protected DishesInfo(Parcel in) {
        restaurantDishes = in.createTypedArrayList(RestaurantDishes.CREATOR);
        restaurant_info_id = in.readString();
        restaurant_name = in.readString();
        location_lat = in.readString();
        location_long = in.readString();
        restaurant_price = in.readString();
        restaurant_rating = in.readString();
    }

    public static final Parcelable.Creator<DishesInfo> CREATOR = new Parcelable.Creator<DishesInfo>() {
        @Override
        public DishesInfo createFromParcel(Parcel source) {
            return new DishesInfo(source);
        }

        @Override
        public DishesInfo[] newArray(int size) {
            return new DishesInfo[size];
        }
    };
}
