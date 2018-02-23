package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 11/2/18.
 */

@Getter
@Setter

public class UserCuisinePreferences extends RealmObject {
    private String cuisine_info_id;
    private boolean is_cuisine_like;
    private boolean is_cuisine_favourite;

    public UserCuisinePreferences() {
    }

    public UserCuisinePreferences(String cuisine_info_id,
                                  boolean is_cuisine_like,
                                  boolean is_cuisine_favourite) {
        this.cuisine_info_id = cuisine_info_id;
        this.is_cuisine_like = is_cuisine_like;
        this.is_cuisine_favourite = is_cuisine_favourite;
    }

   /* protected UserCuisinePreferences(Parcel in) {
        cuisine_info_id = in.readString();
        is_cuisine_like = in.readString();
        is_cuisine_favourite = in.readString();
    }

    public static final Creator<UserCuisinePreferences> CREATOR = new Creator<UserCuisinePreferences>() {
        @Override
        public UserCuisinePreferences createFromParcel(Parcel in) {
            return new UserCuisinePreferences(in);
        }

        @Override
        public UserCuisinePreferences[] newArray(int size) {
            return new UserCuisinePreferences[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cuisine_info_id);
        dest.writeString(is_cuisine_like);
        dest.writeString(is_cuisine_favourite);
    }*/
}
