package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 15/2/18.
 */

@Getter
@Setter
public class FoodPref implements Parcelable {

    private String food_type_info_id;
    private String food_name;
    private String food_image_url;
    private boolean is_food_like;
    private boolean is_food_favourite;
    private String user_food_preferences_id;

    protected FoodPref(Parcel in) {
        food_type_info_id = in.readString();
        food_name = in.readString();
        food_image_url = in.readString();
        is_food_like = in.readByte() != 0;
        is_food_favourite = in.readByte() != 0;
        user_food_preferences_id = in.readString();
    }

    public static final Creator<FoodPref> CREATOR = new Creator<FoodPref>() {
        @Override
        public FoodPref createFromParcel(Parcel in) {
            return new FoodPref(in);
        }

        @Override
        public FoodPref[] newArray(int size) {
            return new FoodPref[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(food_type_info_id);
        dest.writeString(food_name);
        dest.writeString(food_image_url);
        dest.writeByte((byte) (is_food_like ? 1 : 0));
        dest.writeByte((byte) (is_food_favourite ? 1 : 0));
        dest.writeString(user_food_preferences_id);
    }
}
