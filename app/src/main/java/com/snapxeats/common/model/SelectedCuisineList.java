package com.snapxeats.common.model;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 2/2/18.
 */
@Getter
@Setter
public class SelectedCuisineList implements Parcelable {
    LocationCuisine location;
    List<String> selectedCuisineList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(location, flags);
        dest.writeStringList(selectedCuisineList);
    }

    public SelectedCuisineList() {
    }

    protected SelectedCuisineList(Parcel in) {
        location = in.readParcelable(LocationCuisine.class.getClassLoader());
        selectedCuisineList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<SelectedCuisineList> CREATOR = new Parcelable.Creator<SelectedCuisineList>() {
        @Override
        public SelectedCuisineList createFromParcel(Parcel source) {
            return new SelectedCuisineList(source);
        }

        @Override
        public SelectedCuisineList[] newArray(int size) {
            return new SelectedCuisineList[size];
        }
    };
}
