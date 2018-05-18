package com.snapxeats.common.model.restaurantInfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 26/2/18.
 */
@Getter
@Setter
public class RestaurantInfo implements Parcelable {
    private List<String> restaurant_amenities;

    private List<RestaurantTimings> restaurant_timings;

    private String isOpenNow;

    private String restaurant_info_id;

    private List<RestaurantPics> restaurant_pics;

    private String restaurant_address;

    private String restaurant_name;

    private String location_lat;

    private List<RestaurantSpeciality> restaurant_speciality;

    private String restaurant_rating;

    private String restaurant_contact_no;

    private String location_long;

    private String restaurant_price;

    protected RestaurantInfo(Parcel in) {
        restaurant_amenities = in.createStringArrayList();
        restaurant_timings = in.createTypedArrayList(RestaurantTimings.CREATOR);
        isOpenNow = in.readString();
        restaurant_info_id = in.readString();
        restaurant_pics = in.createTypedArrayList(RestaurantPics.CREATOR);
        restaurant_address = in.readString();
        restaurant_name = in.readString();
        location_lat = in.readString();
        restaurant_speciality = in.createTypedArrayList(RestaurantSpeciality.CREATOR);
        restaurant_rating = in.readString();
        restaurant_contact_no = in.readString();
        location_long = in.readString();
        restaurant_price = in.readString();
    }

    public static final Creator<RestaurantInfo> CREATOR = new Creator<RestaurantInfo>() {
        @Override
        public RestaurantInfo createFromParcel(Parcel in) {
            return new RestaurantInfo(in);
        }

        @Override
        public RestaurantInfo[] newArray(int size) {
            return new RestaurantInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(restaurant_amenities);
        dest.writeTypedList(restaurant_timings);
        dest.writeString(isOpenNow);
        dest.writeString(restaurant_info_id);
        dest.writeTypedList(restaurant_pics);
        dest.writeString(restaurant_address);
        dest.writeString(restaurant_name);
        dest.writeString(location_lat);
        dest.writeTypedList(restaurant_speciality);
        dest.writeString(restaurant_rating);
        dest.writeString(restaurant_contact_no);
        dest.writeString(location_long);
        dest.writeString(restaurant_price);
    }
}
