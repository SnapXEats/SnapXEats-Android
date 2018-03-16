package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 7/2/18.
 */
@Getter
@Setter
public class SnapXUserResponse implements Parcelable{
    private SnapXUser userInfo;

    protected SnapXUserResponse(Parcel in) {
        userInfo = in.readParcelable(SnapXUser.class.getClassLoader());
    }

    public static final Creator<SnapXUserResponse> CREATOR = new Creator<SnapXUserResponse>() {
        @Override
        public SnapXUserResponse createFromParcel(Parcel in) {
            return new SnapXUserResponse(in);
        }

        @Override
        public SnapXUserResponse[] newArray(int size) {
            return new SnapXUserResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(userInfo, flags);
    }
}
