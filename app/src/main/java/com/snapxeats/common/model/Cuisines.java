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
public class Cuisines  implements Parcelable {

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
        dest.writeString(cuisine_info_id);
        dest.writeString(cuisine_image_url);
        dest.writeString(cuisine_name);
        dest.writeString(user_cuisine_preferences_id);
        dest.writeByte(isSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(is_cuisine_like ? (byte) 1 : (byte) 0);
        dest.writeByte(is_cuisine_favourite ? (byte) 1 : (byte) 0);
    }

    public Cuisines() {
    }

    protected Cuisines(Parcel in) {
        cuisine_info_id = in.readString();
        cuisine_image_url = in.readString();
        cuisine_name = in.readString();
        user_cuisine_preferences_id = in.readString();
        isSelected = in.readByte() != 0;
        is_cuisine_like = in.readByte() != 0;
        is_cuisine_favourite = in.readByte() != 0;
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
