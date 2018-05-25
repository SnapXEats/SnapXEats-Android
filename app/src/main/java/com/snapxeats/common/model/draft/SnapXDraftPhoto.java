package com.snapxeats.common.model.draft;

import com.snapxeats.common.model.preference.DaoSession;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by Snehal Tembare on 11/5/18.
 */

@Entity
public class SnapXDraftPhoto {
    private String restId;

    @Id
    private String smartPhoto_Draft_Stored_id;
    private String restaurantName;
    private String restaurantAddress;
    private String imageURL;
    private String audioURL;
    private String textReview;
    private int rating;

    @ToMany(referencedJoinProperty = "photoIdFk")
    private List<RestaurantAminities> restaurant_aminities;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1239239247)
    private transient SnapXDraftPhotoDao myDao;

    @Generated(hash = 280186707)
    public SnapXDraftPhoto(String restId, String smartPhoto_Draft_Stored_id,
                           String restaurantName, String restaurantAddress, String imageURL, String audioURL,
                           String textReview, int rating) {
        this.restId = restId;
        this.smartPhoto_Draft_Stored_id = smartPhoto_Draft_Stored_id;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.imageURL = imageURL;
        this.audioURL = audioURL;
        this.textReview = textReview;
        this.rating = rating;
    }

    @Generated(hash = 1191274478)
    public SnapXDraftPhoto() {
    }

    public void setRestaurant_aminities(List<RestaurantAminities> restaurant_aminities) {
        this.restaurant_aminities = restaurant_aminities;
    }

    public String getRestId() {
        return this.restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getSmartPhoto_Draft_Stored_id() {
        return this.smartPhoto_Draft_Stored_id;
    }

    public void setSmartPhoto_Draft_Stored_id(String smartPhoto_Draft_Stored_id) {
        this.smartPhoto_Draft_Stored_id = smartPhoto_Draft_Stored_id;
    }

    public String getRestaurantName() {
        return this.restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return this.restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAudioURL() {
        return this.audioURL;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public String getTextReview() {
        return this.textReview;
    }

    public void setTextReview(String textReview) {
        this.textReview = textReview;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1698529719)
    public List<RestaurantAminities> getRestaurant_aminities() {
        if (restaurant_aminities == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RestaurantAminitiesDao targetDao = daoSession.getRestaurantAminitiesDao();
            List<RestaurantAminities> restaurant_aminitiesNew = targetDao
                    ._querySnapXDraftPhoto_Restaurant_aminities(smartPhoto_Draft_Stored_id);
            synchronized (this) {
                if (restaurant_aminities == null) {
                    restaurant_aminities = restaurant_aminitiesNew;
                }
            }
        }
        return restaurant_aminities;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1372917299)
    public synchronized void resetRestaurant_aminities() {
        restaurant_aminities = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1415628995)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSnapXDraftPhotoDao() : null;
    }


}
