package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 14/2/18.
 */
@Getter
@Setter
public class RestaurantDetails implements Parcelable {
    private String location_lat;

    private List<RestaurantTimings> restaurant_timings;

    private String isOpenNow;

    private String restaurant_info_id;

    private List<RestaurantSpeciality> restaurant_speciality;

    private List<RestaurantPics> restaurant_pics;

    private String restaurant_address;

    private String restaurant_name;

    private String restaurant_contact_no;

    private String location_long;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location_lat);
        dest.writeList(restaurant_timings);
        dest.writeString(isOpenNow);
        dest.writeString(restaurant_info_id);
        dest.writeTypedList(restaurant_speciality);
        dest.writeTypedList(restaurant_pics);
        dest.writeString(restaurant_address);
        dest.writeString(restaurant_name);
        dest.writeString(restaurant_contact_no);
        dest.writeString(location_long);
    }

    public RestaurantDetails() {
    }

    protected RestaurantDetails(Parcel in) {
        location_lat = in.readString();
        restaurant_timings = new ArrayList<RestaurantTimings>();
        in.readList(restaurant_timings, RestaurantTimings.class.getClassLoader());
        isOpenNow = in.readString();
        restaurant_info_id = in.readString();
        restaurant_speciality = in.createTypedArrayList(RestaurantSpeciality.CREATOR);
        restaurant_pics = in.createTypedArrayList(RestaurantPics.CREATOR);
        restaurant_address = in.readString();
        restaurant_name = in.readString();
        restaurant_contact_no = in.readString();
        location_long = in.readString();
    }

    public static final Parcelable.Creator<RestaurantDetails> CREATOR = new Parcelable.Creator<RestaurantDetails>() {
        @Override
        public RestaurantDetails createFromParcel(Parcel source) {
            return new RestaurantDetails(source);
        }

        @Override
        public RestaurantDetails[] newArray(int size) {
            return new RestaurantDetails[size];
        }
    };
}
