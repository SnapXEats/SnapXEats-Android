package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 2/2/18.
 */
@Getter
@Setter
public class LocationCuisine implements Parcelable {
    double latitude;
    double longitude;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    public LocationCuisine() {
    }

    protected LocationCuisine(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Parcelable.Creator<LocationCuisine> CREATOR = new Parcelable.Creator<LocationCuisine>() {
        @Override
        public LocationCuisine createFromParcel(Parcel source) {
            return new LocationCuisine(source);
        }

        @Override
        public LocationCuisine[] newArray(int size) {
            return new LocationCuisine[size];
        }
    };
}
