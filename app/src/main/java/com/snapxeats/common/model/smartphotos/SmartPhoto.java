package com.snapxeats.common.model.smartphotos;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by Snehal Tembare on 27/5/18.
 */

@Entity
public class SmartPhoto {
    private String smartPhoto_Draft_Stored_id;

    @Id
    private String restaurantDishId;
    private String restaurantName;
    private String restaurantAddress;
    private String dishImageURL;
    private String audioURL;
    private String picTakenDate;
    private String textReview;
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
    @Generated(hash = 669921415)
    private transient SmartPhotoDao myDao;

    @Generated(hash = 1596104740)
    public SmartPhoto(String smartPhoto_Draft_Stored_id, String restaurantDishId,
                      String restaurantName, String restaurantAddress, String dishImageURL,
                      String audioURL, String picTakenDate, String textReview) {
        this.smartPhoto_Draft_Stored_id = smartPhoto_Draft_Stored_id;
        this.restaurantDishId = restaurantDishId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.dishImageURL = dishImageURL;
        this.audioURL = audioURL;
        this.picTakenDate = picTakenDate;
        this.textReview = textReview;
    }

    @Generated(hash = 1795797096)
    public SmartPhoto() {
    }

    public void setRestaurant_aminities(List<RestaurantAminities> restaurant_aminities) {
        this.restaurant_aminities = restaurant_aminities;
    }

    public String getSmartPhoto_Draft_Stored_id() {
        return this.smartPhoto_Draft_Stored_id;
    }

    public void setSmartPhoto_Draft_Stored_id(String smartPhoto_Draft_Stored_id) {
        this.smartPhoto_Draft_Stored_id = smartPhoto_Draft_Stored_id;
    }

    public String getRestaurantDishId() {
        return this.restaurantDishId;
    }

    public void setRestaurantDishId(String restaurantDishId) {
        this.restaurantDishId = restaurantDishId;
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

    public String getDishImageURL() {
        return this.dishImageURL;
    }

    public void setDishImageURL(String dishImageURL) {
        this.dishImageURL = dishImageURL;
    }

    public String getAudioURL() {
        return this.audioURL;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public String getPicTakenDate() {
        return this.picTakenDate;
    }

    public void setPicTakenDate(String picTakenDate) {
        this.picTakenDate = picTakenDate;
    }

    public String getTextReview() {
        return this.textReview;
    }

    public void setTextReview(String textReview) {
        this.textReview = textReview;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1645515339)
    public List<RestaurantAminities> getRestaurant_aminities() {
        if (restaurant_aminities == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RestaurantAminitiesDao targetDao = daoSession
                    .getRestaurantAminitiesDao();
            List<RestaurantAminities> restaurant_aminitiesNew = targetDao
                    ._querySmartPhoto_Restaurant_aminities(restaurantDishId);
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 262597808)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSmartPhotoDao() : null;
    }

}
