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

    public RestaurantInfo restaurantDetails;

    protected RootRestaurantInfo(Parcel in) {
        restaurantDetails = in.readParcelable(RestaurantInfo.class.getClassLoader());
    }

    public static final Creator<RootRestaurantInfo> CREATOR = new Creator<RootRestaurantInfo>() {
        @Override
        public RootRestaurantInfo createFromParcel(Parcel in) {
            return new RootRestaurantInfo(in);
        }

        @Override
        public RootRestaurantInfo[] newArray(int size) {
            return new RootRestaurantInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(restaurantDetails, flags);
    }
}
