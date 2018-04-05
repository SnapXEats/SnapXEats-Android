package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.snapxeats.common.model.foodGestures.FoodWishlists;
import com.snapxeats.common.model.foodGestures.UserWishlist;
import com.snapxeats.common.model.foodGestures.Wishlist;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 7/2/18.
 */

@Getter
@Setter

public class SnapXUser implements Parcelable{
    private String token;
    private String user_id;
    private String social_platform;
    private boolean first_time_login;
    private List<UserWishlist> userWishList;
    private String userRewardPoint;

    protected SnapXUser(Parcel in) {
        token = in.readString();
        user_id = in.readString();
        social_platform = in.readString();
        first_time_login = in.readByte() != 0;
        userRewardPoint = in.readString();
    }

    public static final Creator<SnapXUser> CREATOR = new Creator<SnapXUser>() {
        @Override
        public SnapXUser createFromParcel(Parcel in) {
            return new SnapXUser(in);
        }

        @Override
        public SnapXUser[] newArray(int size) {
            return new SnapXUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeString(user_id);
        dest.writeString(social_platform);
        dest.writeByte((byte) (first_time_login ? 1 : 0));
        dest.writeString(userRewardPoint);
    }
}
