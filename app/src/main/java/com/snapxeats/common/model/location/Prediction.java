package com.snapxeats.common.model.location;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 31/1/18.
 */

@Getter
@Setter

public class Prediction implements Parcelable {

    private String description;
    private String place_id;

    public Prediction(String description, String place_id) {
        this.description = description;
        this.place_id = place_id;
    }

    public Prediction(Parcel in) {
        description = in.readString();
        place_id = in.readString();
    }

    public static final Creator<Prediction> CREATOR = new Creator<Prediction>() {
        @Override
        public Prediction createFromParcel(Parcel in) {
            return new Prediction(in);
        }

        @Override
        public Prediction[] newArray(int size) {
            return new Prediction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(place_id);
    }

    @Override
    public String toString() {
        return "Prediction{" +
                "description='" + description + '\'' +
                ", place_id='" + place_id + '\'' +
                '}';
    }
}
