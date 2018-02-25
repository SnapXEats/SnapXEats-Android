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

    private List<String> restaurant_timings;

    private String isOpenNow;

    private String restaurant_info_id;

    private List<Restaurant_speciality> restaurant_speciality;

    private List<Restaurant_pics> restaurant_pics;

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
        dest.writeString(this.location_lat);
        dest.writeStringList(this.restaurant_timings);
        dest.writeString(this.isOpenNow);
        dest.writeString(this.restaurant_info_id);
        dest.writeList(this.restaurant_speciality);
        dest.writeList(this.restaurant_pics);
        dest.writeString(this.restaurant_address);
        dest.writeString(this.restaurant_name);
        dest.writeString(this.restaurant_contact_no);
        dest.writeString(this.location_long);
    }

    public RestaurantDetails() {
    }

    protected RestaurantDetails(Parcel in) {
        this.location_lat = in.readString();
        this.restaurant_timings = in.createStringArrayList();
        this.isOpenNow = in.readString();
        this.restaurant_info_id = in.readString();
        this.restaurant_speciality = new ArrayList<Restaurant_speciality>();
        in.readList(this.restaurant_speciality, Restaurant_speciality.class.getClassLoader());
        this.restaurant_pics = new ArrayList<Restaurant_pics>();
        in.readList(this.restaurant_pics, Restaurant_pics.class.getClassLoader());
        this.restaurant_address = in.readString();
        this.restaurant_name = in.readString();
        this.restaurant_contact_no = in.readString();
        this.location_long = in.readString();
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
