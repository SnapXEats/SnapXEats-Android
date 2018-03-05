package com.snapxeats.common.model.restaurantInfo;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 26/2/18.
 */
@Getter
@Setter
public class RootRestaurantInfo implements Parcelable {
    private RestaurantInfo restaurantDetails;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.restaurantDetails, flags);
    }

    public RootRestaurantInfo() {
    }

    protected RootRestaurantInfo(Parcel in) {
        this.restaurantDetails = in.readParcelable(RestaurantInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<RootRestaurantInfo> CREATOR = new Parcelable.Creator<RootRestaurantInfo>() {
        @Override
        public RootRestaurantInfo createFromParcel(Parcel source) {
            return new RootRestaurantInfo(source);
        }

        @Override
        public RootRestaurantInfo[] newArray(int size) {
            return new RootRestaurantInfo[size];
        }
    };
}
