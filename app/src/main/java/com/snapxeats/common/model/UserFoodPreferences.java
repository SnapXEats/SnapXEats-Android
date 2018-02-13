package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Snehal Tembare on 11/2/18.
 */

public class UserFoodPreferences implements Parcelable {

    private String food_type_info_id;
    private boolean is_food_like;
    private boolean is_food_favourite;

    protected UserFoodPreferences(Parcel in) {
        food_type_info_id = in.readString();
        is_food_like = in.readByte() != 0;
        is_food_favourite = in.readByte() != 0;
    }

    public static final Creator<UserFoodPreferences> CREATOR = new Creator<UserFoodPreferences>() {
        @Override
        public UserFoodPreferences createFromParcel(Parcel in) {
            return new UserFoodPreferences(in);
        }

        @Override
        public UserFoodPreferences[] newArray(int size) {
            return new UserFoodPreferences[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(food_type_info_id);
        dest.writeByte((byte) (is_food_like ? 1 : 0));
        dest.writeByte((byte) (is_food_favourite ? 1 : 0));
    }
}
