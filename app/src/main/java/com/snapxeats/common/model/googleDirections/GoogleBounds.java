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
public class GoogleBounds implements Parcelable {
    private Southwest southwest;

    private Northeast northeast;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(southwest, flags);
        dest.writeParcelable(northeast, flags);
    }

    public GoogleBounds() {
    }

    protected GoogleBounds(Parcel in) {
        southwest = in.readParcelable(Southwest.class.getClassLoader());
        northeast = in.readParcelable(Northeast.class.getClassLoader());
    }

    public static final Parcelable.Creator<GoogleBounds> CREATOR = new Parcelable.Creator<GoogleBounds>() {
        @Override
        public GoogleBounds createFromParcel(Parcel source) {
            return new GoogleBounds(source);
        }

        @Override
        public GoogleBounds[] newArray(int size) {
            return new GoogleBounds[size];
        }
    };
}
