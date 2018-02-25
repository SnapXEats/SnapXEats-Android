package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 8/2/18.
 */
@Getter
@Setter

//TODO SnapXUser class for saving user related information
public class SnapXUser implements Parcelable {
    private RootInstagram rootInstagram;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.rootInstagram, flags);
    }

    public SnapXUser() {
    }

    protected SnapXUser(Parcel in) {
        this.rootInstagram = in.readParcelable(RootInstagram.class.getClassLoader());
    }

    public static final Parcelable.Creator<SnapXUser> CREATOR = new Parcelable.Creator<SnapXUser>() {
        @Override
        public SnapXUser createFromParcel(Parcel source) {
            return new SnapXUser(source);
        }

        @Override
        public SnapXUser[] newArray(int size) {
            return new SnapXUser[size];
        }
    };
}
