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
public class RootInstagram implements Parcelable {
    private InstagramData data;
    private String instagramToken;
    private InstagramMeta meta;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeString(instagramToken);
        dest.writeParcelable(meta, flags);
    }

    public RootInstagram() {
    }

    protected RootInstagram(Parcel in) {
        data = in.readParcelable(InstagramData.class.getClassLoader());
        instagramToken = in.readString();
        meta = in.readParcelable(InstagramMeta.class.getClassLoader());
    }

    public static final Parcelable.Creator<RootInstagram> CREATOR = new Parcelable.Creator<RootInstagram>() {
        @Override
        public RootInstagram createFromParcel(Parcel source) {
            return new RootInstagram(source);
        }

        @Override
        public RootInstagram[] newArray(int size) {
            return new RootInstagram[size];
        }
    };
}
