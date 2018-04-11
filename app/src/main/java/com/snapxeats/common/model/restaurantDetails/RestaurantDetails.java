package com.snapxeats.common.model.restaurantDetails;

import android.os.Parcel;
import android.os.Parcelable;

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

    private String restaurant_rating;

    private String restaurant_name;

    private String restaurant_contact_no;

    private String location_long;

    private String restaurant_price;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.location_lat);
        dest.writeTypedList(this.restaurant_timings);
        dest.writeString(this.isOpenNow);
        dest.writeString(this.restaurant_info_id);
        dest.writeTypedList(this.restaurant_speciality);
        dest.writeTypedList(this.restaurant_pics);
        dest.writeString(this.restaurant_address);
        dest.writeString(this.restaurant_rating);
        dest.writeString(this.restaurant_name);
        dest.writeString(this.restaurant_contact_no);
        dest.writeString(this.location_long);
        dest.writeString(this.restaurant_price);
    }

    public RestaurantDetails() {
    }

    protected RestaurantDetails(Parcel in) {
        this.location_lat = in.readString();
        this.restaurant_timings = in.createTypedArrayList(RestaurantTimings.CREATOR);
        this.isOpenNow = in.readString();
        this.restaurant_info_id = in.readString();
        this.restaurant_speciality = in.createTypedArrayList(RestaurantSpeciality.CREATOR);
        this.restaurant_pics = in.createTypedArrayList(RestaurantPics.CREATOR);
        this.restaurant_address = in.readString();
        this.restaurant_rating = in.readString();
        this.restaurant_name = in.readString();
        this.restaurant_contact_no = in.readString();
        this.location_long = in.readString();
        this.restaurant_price = in.readString();
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
