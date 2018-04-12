package com.snapxeats.common.model.googleDirections;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 2/4/18.
 */
@Getter
@Setter
public class Southwest implements Parcelable {
    private String lng;
    private String lat;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lng);
        dest.writeString(lat);
    }

    public Southwest() {
    }

    protected Southwest(Parcel in) {
        lng = in.readString();
        lat = in.readString();
    }

    public static final Parcelable.Creator<Southwest> CREATOR = new Parcelable.Creator<Southwest>() {
        @Override
        public Southwest createFromParcel(Parcel source) {
            return new Southwest(source);
        }

        @Override
        public Southwest[] newArray(int size) {
            return new Southwest[size];
        }
    };
}
