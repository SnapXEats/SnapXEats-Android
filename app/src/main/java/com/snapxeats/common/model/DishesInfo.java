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
public class DishesInfo implements Parcelable {
    private List<RestaurantDishes> restaurantDishes;

    private String restaurant_info_id;

    private String restaurant_name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(restaurantDishes);
        dest.writeString(restaurant_info_id);
        dest.writeString(restaurant_name);
    }

    public DishesInfo() {
    }

    protected DishesInfo(Parcel in) {
        restaurantDishes = new ArrayList<RestaurantDishes>();
        in.readList(restaurantDishes, RestaurantDishes.class.getClassLoader());
        restaurant_info_id = in.readString();
        restaurant_name = in.readString();
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
