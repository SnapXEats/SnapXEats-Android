package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Snehal Tembare on 8/2/18.
 */

public class UserPreference implements Parcelable {
    private String token;
    private String user_preferences_id;
    private String restaurant_rating;
    private String restaurant_price;
    private String restaurant_distance;
    private boolean sort_by_distance;
    private boolean sort_by_rating;
    private List<UserCuisinePreferences> user_cuisine_preferences;
    private List<UserFoodPreferences> user_food_preferences;

    protected UserPreference(Parcel in) {
        token = in.readString();
        user_preferences_id = in.readString();
        restaurant_rating = in.readString();
        restaurant_price = in.readString();
        restaurant_distance = in.readString();
        sort_by_distance = in.readByte() != 0;
        sort_by_rating = in.readByte() != 0;
//        user_cuisine_preferences = in.createTypedArrayList(UserCuisinePreferences.CREATOR);
        user_food_preferences = in.createTypedArrayList(UserFoodPreferences.CREATOR);
    }

    public UserPreference(String token,
                          String user_preferences_id,
                          String restaurant_rating,
                          String restaurant_price,
                          String restaurant_distance,
                          boolean sort_by_distance,
                          boolean sort_by_rating,
                          List<UserCuisinePreferences> user_cuisine_preferences,
                          List<UserFoodPreferences> user_food_preferences) {
        this.token = token;
        this.user_preferences_id = user_preferences_id;
        this.restaurant_rating = restaurant_rating;
        this.restaurant_price = restaurant_price;
        this.restaurant_distance = restaurant_distance;
        this.sort_by_distance = sort_by_distance;
        this.sort_by_rating = sort_by_rating;
        this.user_cuisine_preferences = user_cuisine_preferences;
        this.user_food_preferences = user_food_preferences;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeString(user_preferences_id);
        dest.writeString(restaurant_rating);
        dest.writeString(restaurant_price);
        dest.writeString(restaurant_distance);
        dest.writeByte((byte) (sort_by_distance ? 1 : 0));
        dest.writeByte((byte) (sort_by_rating ? 1 : 0));
//        dest.writeTypedList(user_cuisine_preferences);
        dest.writeTypedList(user_food_preferences);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserPreference> CREATOR = new Creator<UserPreference>() {
        @Override
        public UserPreference createFromParcel(Parcel in) {
            return new UserPreference(in);
        }

        @Override
        public UserPreference[] newArray(int size) {
            return new UserPreference[size];
        }
    };
}
