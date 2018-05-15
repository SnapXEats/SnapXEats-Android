package com.snapxeats.ui.review;

import android.content.Context;

import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.draft.RestaurantAminities;
import com.snapxeats.common.model.draft.SnapXDraftPhoto;
import com.snapxeats.common.utilities.AppUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 14/5/18.
 */

public class ReviewDbHelper {
    private Context mContext;

    @Inject
    ReviewDbHelper() {
    }


    @Inject
    AppUtility utility;

    @Inject
    DbHelper dbHelper;

    public void setContext(Context context) {
        this.mContext = context;
        utility.setContext(mContext);
        dbHelper.setContext(mContext);
    }

    void saveSnapDataInDb(String restaurant_name,
                          String image_path,
                          String audio_path,
                          String textReview,
                          int rating,
                          List<String> restaurantAminities) {

        String smartPhoto_Draft_Stored_id =
                new SimpleDateFormat(mContext.getString(R.string.date_time_pattern)).format(new Date());

        SnapXDraftPhoto snapXDraftPhoto = new SnapXDraftPhoto();
        snapXDraftPhoto.setSmartPhoto_Draft_Stored_id(smartPhoto_Draft_Stored_id);
        snapXDraftPhoto.setRestaurantName(restaurant_name);
        snapXDraftPhoto.setImageURL(image_path);
        snapXDraftPhoto.setAudioURL(audio_path);
        snapXDraftPhoto.setTextReview(textReview);
        snapXDraftPhoto.setRating(rating);

        if (null != restaurantAminities && 0 != restaurantAminities.size()) {
            RestaurantAminities restaurantAminity = new RestaurantAminities();

            List<RestaurantAminities> restaurant_aminities_list = new ArrayList<>();
            for (int index = 0; index < restaurantAminities.size(); index++) {
                restaurantAminity.setAminity(restaurantAminities.get(index));
                restaurant_aminities_list.add(restaurantAminity);
            }
            snapXDraftPhoto.setRestaurant_aminities(restaurant_aminities_list);
        }

        dbHelper.getDraftPhotoDao().insertOrReplace(snapXDraftPhoto);

    }
}
