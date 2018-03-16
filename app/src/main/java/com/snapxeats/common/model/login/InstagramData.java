package com.snapxeats.common.model.login;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 16/2/18.
 */
@Getter
@Setter
public class InstagramData implements Parcelable {

    private String id;

    private String profile_picture;

    private String username;

    private String bio;

    private String website;

    private String is_business;

    private String full_name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(profile_picture);
        dest.writeString(username);
        dest.writeString(bio);
        dest.writeString(website);
        dest.writeString(is_business);
        dest.writeString(full_name);
    }

    public InstagramData() {
    }

    protected InstagramData(Parcel in) {
        id = in.readString();
        profile_picture = in.readString();
        username = in.readString();
        bio = in.readString();
        website = in.readString();
        is_business = in.readString();
        full_name = in.readString();
    }

    public static final Parcelable.Creator<InstagramData> CREATOR = new Parcelable.Creator<InstagramData>() {
        @Override
        public InstagramData createFromParcel(Parcel source) {
            return new InstagramData(source);
        }

        @Override
        public InstagramData[] newArray(int size) {
            return new InstagramData[size];
        }
    };
}
