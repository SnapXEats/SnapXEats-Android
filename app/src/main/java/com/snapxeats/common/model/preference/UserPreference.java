package com.snapxeats.common.model.preference;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.DaoException;
import com.snapxeats.common.model.DaoSession;

/**
 * Created by Snehal Tembare on 8/2/18.
 */


@Entity
public class UserPreference {

    @Id
    private String id;

    @Expose
    private String restaurant_rating;
    @Expose
    private String restaurant_price;
    @Expose
    private String restaurant_distance;
    @Expose
    private boolean sort_by_distance;
    @Expose
    private boolean sort_by_rating;

    @Expose
    @ToMany(referencedJoinProperty = "userPreferenceId")
    private List<UserCuisinePreferences> user_cuisine_preferences;

    @SerializedName("user_food_preferences")
    @Expose
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "userPreferenceId")})
    private List<UserFoodPreferences> user_food_preferences;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 2081933574)
    private transient UserPreferenceDao myDao;

    @Generated(hash = 1348862546)
    public UserPreference(String id, String restaurant_rating, String restaurant_price,
            String restaurant_distance, boolean sort_by_distance, boolean sort_by_rating) {
        this.id = id;
        this.restaurant_rating = restaurant_rating;
        this.restaurant_price = restaurant_price;
        this.restaurant_distance = restaurant_distance;
        this.sort_by_distance = sort_by_distance;
        this.sort_by_rating = sort_by_rating;
    }

    @Generated(hash = 1390964)
    public UserPreference() {
    }
    public UserPreference(String id,
                          String restaurant_rating,
                          String restaurant_price,
                          String restaurant_distance,
                          boolean sort_by_distance,
                          boolean sort_by_rating,
                          List<UserCuisinePreferences> user_cuisine_preferences,
                          List<UserFoodPreferences> user_food_preferences) {
        this.id = id;
        this.restaurant_rating = restaurant_rating;
        this.restaurant_price = restaurant_price;
        this.restaurant_distance = restaurant_distance;
        this.sort_by_distance = sort_by_distance;
        this.sort_by_rating = sort_by_rating;
        this.user_cuisine_preferences = user_cuisine_preferences;
        this.user_food_preferences = user_food_preferences;
    }
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurant_rating() {
        return this.restaurant_rating;
    }

    public void setRestaurant_rating(String restaurant_rating) {
        this.restaurant_rating = restaurant_rating;
    }

    public String getRestaurant_price() {
        return this.restaurant_price;
    }

    public void setRestaurant_price(String restaurant_price) {
        this.restaurant_price = restaurant_price;
    }

    public String getRestaurant_distance() {
        return this.restaurant_distance;
    }

    public void setRestaurant_distance(String restaurant_distance) {
        this.restaurant_distance = restaurant_distance;
    }

    public boolean getSort_by_distance() {
        return this.sort_by_distance;
    }

    public void setSort_by_distance(boolean sort_by_distance) {
        this.sort_by_distance = sort_by_distance;
    }

    public boolean getSort_by_rating() {
        return this.sort_by_rating;
    }

    public void setSort_by_rating(boolean sort_by_rating) {
        this.sort_by_rating = sort_by_rating;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1331219827)
    public List<UserCuisinePreferences> getUser_cuisine_preferences() {
        if (user_cuisine_preferences == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserCuisinePreferencesDao targetDao = daoSession.getUserCuisinePreferencesDao();
            List<UserCuisinePreferences> user_cuisine_preferencesNew = targetDao
                    ._queryUserPreference_User_cuisine_preferences(id);
            synchronized (this) {
                if (user_cuisine_preferences == null) {
                    user_cuisine_preferences = user_cuisine_preferencesNew;
                }
            }
        }
        return user_cuisine_preferences;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1132281012)
    public synchronized void resetUser_cuisine_preferences() {
        user_cuisine_preferences = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1238150642)
    public List<UserFoodPreferences> getUser_food_preferences() {
        if (user_food_preferences == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserFoodPreferencesDao targetDao = daoSession.getUserFoodPreferencesDao();
            List<UserFoodPreferences> user_food_preferencesNew = targetDao
                    ._queryUserPreference_User_food_preferences(id);
            synchronized (this) {
                if (user_food_preferences == null) {
                    user_food_preferences = user_food_preferencesNew;
                }
            }
        }
        return user_food_preferences;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 2119780956)
    public synchronized void resetUser_food_preferences() {
        user_food_preferences = null;
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
    @Generated(hash = 1374549841)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserPreferenceDao() : null;
    }


}
