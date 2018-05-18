package com.snapxeats.common.model.restaurantInfo;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 14/2/18.
 */
@Getter
@Setter
public class RestaurantSpeciality implements Parcelable {
    private String dish_image_url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dish_image_url);
    }

    public RestaurantSpeciality() {
    }

    protected RestaurantSpeciality(Parcel in) {
        dish_image_url = in.readString();
    }

    public static final Parcelable.Creator<RestaurantSpeciality> CREATOR = new Parcelable.Creator<RestaurantSpeciality>() {
        @Override
        public RestaurantSpeciality createFromParcel(Parcel source) {
            return new RestaurantSpeciality(source);
        }

        @Override
        public RestaurantSpeciality[] newArray(int size) {
            return new RestaurantSpeciality[size];
        }
    };
}
