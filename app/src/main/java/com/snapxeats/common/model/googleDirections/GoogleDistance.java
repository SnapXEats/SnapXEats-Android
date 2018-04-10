package com.snapxeats.common.model.googleDirections;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 22/2/18.
 */
@Setter
@Getter
public class GoogleDistance implements Parcelable {

    private String text;

    private String value;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(value);
    }

    public GoogleDistance() {
    }

    protected GoogleDistance(Parcel in) {
        text = in.readString();
        value = in.readString();
    }

    public static final Parcelable.Creator<GoogleDistance> CREATOR = new Parcelable.Creator<GoogleDistance>() {
        @Override
        public GoogleDistance createFromParcel(Parcel source) {
            return new GoogleDistance(source);
        }

        @Override
        public GoogleDistance[] newArray(int size) {
            return new GoogleDistance[size];
        }
    };
}
