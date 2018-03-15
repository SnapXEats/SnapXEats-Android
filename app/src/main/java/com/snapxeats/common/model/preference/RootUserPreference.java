package com.snapxeats.common.model.preference;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 7/3/18.
 */

@Getter
@Setter

@Singleton
public class RootUserPreference implements Parcelable {

    private String user_Id;
    private String restaurant_rating;
    private String restaurant_price;
    private String restaurant_distance;
    private boolean sort_by_distance;
    private boolean sort_by_rating;
    private List<UserCuisinePreferences> userCuisinePreferences;
    private List<UserFoodPreferences> userFoodPreferences;

    protected RootUserPreference(Parcel in) {
        user_Id = in.readString();
        restaurant_rating = in.readString();
        restaurant_price = in.readString();
        restaurant_distance = in.readString();
        sort_by_distance = in.readByte() != 0;
        sort_by_rating = in.readByte() != 0;
    }

    public static final Creator<RootUserPreference> CREATOR = new Creator<RootUserPreference>() {
        @Override
        public RootUserPreference createFromParcel(Parcel in) {
            return new RootUserPreference(in);
        }

        @Override
        public RootUserPreference[] newArray(int size) {
            return new RootUserPreference[size];
        }
    };

    @Inject
    public RootUserPreference() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_Id);
        dest.writeString(restaurant_rating);
        dest.writeString(restaurant_price);
        dest.writeString(restaurant_distance);
        dest.writeByte((byte) (sort_by_distance ? 1 : 0));
        dest.writeByte((byte) (sort_by_rating ? 1 : 0));
    }
}