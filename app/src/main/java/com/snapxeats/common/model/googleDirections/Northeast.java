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
public class Northeast implements Parcelable {
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

    public Northeast() {
    }

    protected Northeast(Parcel in) {
        lng = in.readString();
        lat = in.readString();
    }

    public static final Parcelable.Creator<Northeast> CREATOR = new Parcelable.Creator<Northeast>() {
        @Override
        public Northeast createFromParcel(Parcel source) {
            return new Northeast(source);
        }

        @Override
        public Northeast[] newArray(int size) {
            return new Northeast[size];
        }
    };
}
