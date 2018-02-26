package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 14/2/18.
 */
@Getter
@Setter
public class RestaurantPics implements Parcelable {
    private String dish_image_url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dish_image_url);
    }

    public RestaurantPics() {
    }

    protected RestaurantPics(Parcel in) {
        dish_image_url = in.readString();
    }

    public static final Parcelable.Creator<RestaurantPics> CREATOR = new Parcelable.Creator<RestaurantPics>() {
        @Override
        public RestaurantPics createFromParcel(Parcel source) {
            return new RestaurantPics(source);
        }

        @Override
        public RestaurantPics[] newArray(int size) {
            return new RestaurantPics[size];
        }
    };
}
