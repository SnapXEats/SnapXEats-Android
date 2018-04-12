package com.snapxeats.common.model.googleDirections;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 22/2/18.
 */
@Getter
@Setter
public class GoogleDuration implements Parcelable {
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

    public GoogleDuration() {
    }

    protected GoogleDuration(Parcel in) {
        text = in.readString();
        value = in.readString();
    }

    public static final Parcelable.Creator<GoogleDuration> CREATOR = new Parcelable.Creator<GoogleDuration>() {
        @Override
        public GoogleDuration createFromParcel(Parcel source) {
            return new GoogleDuration(source);
        }

        @Override
        public GoogleDuration[] newArray(int size) {
            return new GoogleDuration[size];
        }
    };
}
