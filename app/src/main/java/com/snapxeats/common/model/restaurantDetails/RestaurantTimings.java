package com.snapxeats.common.model.restaurantDetails;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 26/2/18.
 */
@Getter
@Setter
public class RestaurantTimings implements Parcelable {
    private String day_of_week;

    private String restaurant_open_close_time;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(day_of_week);
        dest.writeString(restaurant_open_close_time);
    }

    public RestaurantTimings() {
    }

    protected RestaurantTimings(Parcel in) {
        day_of_week = in.readString();
        restaurant_open_close_time = in.readString();
    }

    public static final Parcelable.Creator<RestaurantTimings> CREATOR = new Parcelable.Creator<RestaurantTimings>() {
        @Override
        public RestaurantTimings createFromParcel(Parcel source) {
            return new RestaurantTimings(source);
        }

        @Override
        public RestaurantTimings[] newArray(int size) {
            return new RestaurantTimings[size];
        }
    };
}
