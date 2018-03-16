package com.snapxeats.common.model.location;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 31/1/18.
 */

@Getter
@Setter

public class PlacesAutoCompleteData implements Parcelable{
    List<Prediction> predictions;

    protected PlacesAutoCompleteData(Parcel in) {
        predictions = in.createTypedArrayList(Prediction.CREATOR);
    }

    public static final Creator<PlacesAutoCompleteData> CREATOR = new Creator<PlacesAutoCompleteData>() {
        @Override
        public PlacesAutoCompleteData createFromParcel(Parcel in) {
            return new PlacesAutoCompleteData(in);
        }

        @Override
        public PlacesAutoCompleteData[] newArray(int size) {
            return new PlacesAutoCompleteData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(predictions);
    }
}
