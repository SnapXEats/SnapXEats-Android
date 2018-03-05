package com.snapxeats.common.model.restaurantInfo;

import android.os.Parcel;
import android.os.Parcelable;

import com.snapxeats.common.model.RestaurantPics;
import com.snapxeats.common.model.RestaurantTimings;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 26/2/18.
 */
@Getter
@Setter
public class RestaurantInfo implements Parcelable {
    private List<String> restaurant_aminities;

    private List<RestaurantTimings> restaurant_timings;

    private String isOpenNow;

    private String restaurant_info_id;

    private List<RestaurantPics> restaurant_pics;

    private String restaurant_address;

    private String restaurant_name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(restaurant_aminities);
        dest.writeTypedList(restaurant_timings);
        dest.writeString(isOpenNow);
        dest.writeString(restaurant_info_id);
        dest.writeTypedList(restaurant_pics);
        dest.writeString(restaurant_address);
        dest.writeString(restaurant_name);
    }

    public RestaurantInfo() {
    }

    protected RestaurantInfo(Parcel in) {
        restaurant_aminities = in.createStringArrayList();
        restaurant_timings = in.createTypedArrayList(RestaurantTimings.CREATOR);
        isOpenNow = in.readString();
        restaurant_info_id = in.readString();
        restaurant_pics = in.createTypedArrayList(RestaurantPics.CREATOR);
        restaurant_address = in.readString();
        restaurant_name = in.readString();
    }

    public static final Parcelable.Creator<RestaurantInfo> CREATOR = new Parcelable.Creator<RestaurantInfo>() {
        @Override
        public RestaurantInfo createFromParcel(Parcel source) {
            return new RestaurantInfo(source);
        }

        @Override
        public RestaurantInfo[] newArray(int size) {
            return new RestaurantInfo[size];
        }
    };
}
