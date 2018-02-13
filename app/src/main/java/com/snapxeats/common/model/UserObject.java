package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Snehal Tembare on 8/2/18.
 */

public class UserObject implements Parcelable {

    private Location userLocation;
    private String restaurant_rating;
    private String restaurant_price;
    private String restaurant_distance;
    private boolean sort_by_distance;
    private boolean sort_by_rating;
    private UserCuisinePreferences user_cuisine_preferences;
    private UserFoodPreferences user_food_preferences;


    protected UserObject(Parcel in) {
        userLocation = in.readParcelable(Location.class.getClassLoader());
        restaurant_rating = in.readString();
        restaurant_price = in.readString();
        restaurant_distance = in.readString();
        sort_by_distance = in.readByte() != 0;
        sort_by_rating = in.readByte() != 0;
        user_cuisine_preferences = in.readParcelable(UserCuisinePreferences.class.getClassLoader());
        user_food_preferences = in.readParcelable(UserFoodPreferences.class.getClassLoader());
    }

    public static final Creator<UserObject> CREATOR = new Creator<UserObject>() {
        @Override
        public UserObject createFromParcel(Parcel in) {
            return new UserObject(in);
        }

        @Override
        public UserObject[] newArray(int size) {
            return new UserObject[size];
        }
    };

    public UserObject(Location userLocation,
                      String restaurant_rating,
                      String restaurant_price,
                      String restaurant_distance,
                      boolean sort_by_distance,
                      boolean sort_by_rating,
                      UserCuisinePreferences user_cuisine_preferences,
                      UserFoodPreferences user_food_preferences) {
        this.userLocation = userLocation;
        this.restaurant_rating = restaurant_rating;
        this.restaurant_price = restaurant_price;
        this.restaurant_distance = restaurant_distance;
        this.sort_by_distance = sort_by_distance;
        this.sort_by_rating = sort_by_rating;
        this.user_cuisine_preferences = user_cuisine_preferences;
        this.user_food_preferences = user_food_preferences;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(userLocation, flags);
        dest.writeString(restaurant_rating);
        dest.writeString(restaurant_price);
        dest.writeString(restaurant_distance);
        dest.writeByte((byte) (sort_by_distance ? 1 : 0));
        dest.writeByte((byte) (sort_by_rating ? 1 : 0));
        dest.writeParcelable(user_cuisine_preferences, flags);
        dest.writeParcelable(user_food_preferences, flags);
    }
}
