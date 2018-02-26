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
public class RootRestaurantDetails implements Parcelable {
    private RestaurantDetails restaurantDetails;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(restaurantDetails, flags);
    }

    public RootRestaurantDetails() {
    }

    protected RootRestaurantDetails(Parcel in) {
        restaurantDetails = in.readParcelable(RestaurantDetails.class.getClassLoader());
    }

    public static final Parcelable.Creator<RootRestaurantDetails> CREATOR = new Parcelable.Creator<RootRestaurantDetails>() {
        @Override
        public RootRestaurantDetails createFromParcel(Parcel source) {
            return new RootRestaurantDetails(source);
        }

        @Override
        public RootRestaurantDetails[] newArray(int size) {
            return new RootRestaurantDetails[size];
        }
    };
}
