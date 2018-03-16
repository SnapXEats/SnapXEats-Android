package com.snapxeats.common.model.location;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 3/2/18.
 */

@Getter
@Setter

public class Result implements Parcelable {
    private Geometry geometry;
    private String name;
    private String vicinity;

    protected Result(Parcel in) {
        geometry = in.readParcelable(Geometry.class.getClassLoader());
        name = in.readString();
        vicinity = in.readString();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public Result(Geometry geometry, String name) {
        this.geometry = geometry;
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(geometry, flags);
        dest.writeString(name);
        dest.writeString(vicinity);
    }
}
