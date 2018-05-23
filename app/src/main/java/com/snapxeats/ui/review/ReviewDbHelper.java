package com.snapxeats.ui.review;

import android.content.Context;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.smartphotos.RestaurantAminities;
import com.snapxeats.common.model.smartphotos.RestaurantAminitiesDao;
import com.snapxeats.common.model.smartphotos.SnapXDraftPhoto;
import com.snapxeats.common.model.smartphotos.SnapXDraftPhotoDao;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.ui.home.fragment.smartphotos.draft.DraftAdapter;
import org.greenrobot.greendao.query.DeleteQuery;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import static com.snapxeats.common.constants.UIConstants.ZERO;

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

    void saveSnapDataInDb(String restId,
                          String restaurant_name,
                          String restAddress,
                          String image_path,
                          String audio_path,
                          String textReview,
                          int rating,
                          List<String> restaurantAminities) {

        String smartPhoto_Draft_Stored_id =
                new SimpleDateFormat(mContext.getString(R.string.date_time_pattern)).format(new Date());

        SnapXDraftPhoto snapXDraftPhoto = new SnapXDraftPhoto();
        snapXDraftPhoto.setRestId(restId);
        snapXDraftPhoto.setSmartPhoto_Draft_Stored_id(smartPhoto_Draft_Stored_id);
        snapXDraftPhoto.setRestaurantName(restaurant_name);
        snapXDraftPhoto.setRestaurantAddress(restAddress);
        snapXDraftPhoto.setImageURL(image_path);
        snapXDraftPhoto.setAudioURL(audio_path);
        snapXDraftPhoto.setTextReview(textReview);
        snapXDraftPhoto.setRating(rating);

        if (null != restaurantAminities && ZERO != restaurantAminities.size()) {
            RestaurantAminities restaurantAminity = new RestaurantAminities();

            List<RestaurantAminities> restaurant_aminities_list = new ArrayList<>();
            for (int index = ZERO; index < restaurantAminities.size(); index++) {
                restaurantAminity.setPhotoIdFk(snapXDraftPhoto.getSmartPhoto_Draft_Stored_id());
                restaurantAminity.setAminity(restaurantAminities.get(index));
                dbHelper.getRestaurantAminitiesDao().insert(restaurantAminity);
                restaurant_aminities_list.add(restaurantAminity);
            }
            snapXDraftPhoto.setRestaurant_aminities(restaurant_aminities_list);
        }

        new DraftAdapter().notifyDataSetChanged();
        dbHelper.getDraftPhotoDao().insert(snapXDraftPhoto);
    }

    public List<SnapXDraftPhoto> getDraftData() {
        if (null != dbHelper.getDraftPhotoDao().loadAll()
                && ZERO != dbHelper.getDraftPhotoDao().loadAll().size())
            return dbHelper.getDraftPhotoDao().loadAll();
        return null;
    }

    public void deleteDraftData(String photoId){
        DeleteQuery<RestaurantAminities> deleteRestAminities = dbHelper.getDaoSesion()
                .queryBuilder(RestaurantAminities.class)
                .where(RestaurantAminitiesDao.Properties.PhotoIdFk.eq(photoId)).buildDelete();
        deleteRestAminities.executeDeleteWithoutDetachingEntities();

        DeleteQuery<SnapXDraftPhoto> deleteQuery = dbHelper.getDaoSesion()
                .queryBuilder(SnapXDraftPhoto.class)
                .where(SnapXDraftPhotoDao.Properties.SmartPhoto_Draft_Stored_id.eq(photoId))
                .buildDelete();

        deleteQuery.executeDeleteWithoutDetachingEntities();

        dbHelper.getDaoSesion().clear();
    }
}
