package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 22/1/18.
 */
@Getter
@Setter
public class Cuisines implements Parcelable {

    private String cuisine_info_id;
    private String cuisine_image_url;
    private String cuisine_name;
    private boolean isSelected;
    private boolean is_cuisine_like;
    private boolean is_cuisine_favourite;
    private String user_cuisine_preferences_id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cuisine_info_id);
        dest.writeString(this.cuisine_image_url);
        dest.writeString(this.cuisine_name);
        dest.writeString(this.user_cuisine_preferences_id);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_cuisine_like ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_cuisine_favourite ? (byte) 1 : (byte) 0);
    }

    public Cuisines() {
    }

    protected Cuisines(Parcel in) {
        this.cuisine_info_id = in.readString();
        this.cuisine_image_url = in.readString();
        this.cuisine_name = in.readString();
        this.user_cuisine_preferences_id = in.readString();
        this.isSelected = in.readByte() != 0;
        this.is_cuisine_like = in.readByte() != 0;
        this.is_cuisine_favourite = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Cuisines> CREATOR = new Parcelable.Creator<Cuisines>() {
        @Override
        public Cuisines createFromParcel(Parcel source) {
            return new Cuisines(source);
        }

        @Override
        public Cuisines[] newArray(int size) {
            return new Cuisines[size];
        }
    };
}
