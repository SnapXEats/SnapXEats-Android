package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 30/1/18.
 */
@Getter
@Setter
public class RestaurantDishLabels implements Parcelable {
    private String dish_label;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dish_label);
    }

    public RestaurantDishLabels() {
    }

    protected RestaurantDishLabels(Parcel in) {
        this.dish_label = in.readString();
    }

    public static final Parcelable.Creator<RestaurantDishLabels> CREATOR = new Parcelable.Creator<RestaurantDishLabels>() {
        @Override
        public RestaurantDishLabels createFromParcel(Parcel source) {
            return new RestaurantDishLabels(source);
        }

        @Override
        public RestaurantDishLabels[] newArray(int size) {
            return new RestaurantDishLabels[size];
        }
    };
}
