package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Snehal Tembare on 8/2/18.
 */

public class UserObject implements Parcelable {

    Location userLocation;

    protected UserObject(Parcel in) {
        userLocation = in.readParcelable(Location.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(userLocation, flags);
    }
}
