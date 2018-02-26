package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 16/2/18.
 */
@Getter
@Setter
public class InstagramMeta implements Parcelable {
    private String code;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
    }

    public InstagramMeta() {
    }

    protected InstagramMeta(Parcel in) {
        code = in.readString();
    }

    public static final Parcelable.Creator<InstagramMeta> CREATOR = new Parcelable.Creator<InstagramMeta>() {
        @Override
        public InstagramMeta createFromParcel(Parcel source) {
            return new InstagramMeta(source);
        }

        @Override
        public InstagramMeta[] newArray(int size) {
            return new InstagramMeta[size];
        }
    };
}
