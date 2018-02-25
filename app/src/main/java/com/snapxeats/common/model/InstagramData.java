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
        dest.writeString(this.id);
        dest.writeString(this.profile_picture);
        dest.writeString(this.username);
        dest.writeString(this.bio);
        dest.writeString(this.website);
        dest.writeString(this.is_business);
        dest.writeString(this.full_name);
    }

    public InstagramData() {
    }

    protected InstagramData(Parcel in) {
        this.id = in.readString();
        this.profile_picture = in.readString();
        this.username = in.readString();
        this.bio = in.readString();
        this.website = in.readString();
        this.is_business = in.readString();
        this.full_name = in.readString();
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
