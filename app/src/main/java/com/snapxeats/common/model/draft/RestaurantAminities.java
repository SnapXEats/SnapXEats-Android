package com.snapxeats.common.model.draft;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Snehal Tembare on 12/5/18.
 */


@Entity
public class RestaurantAminities {
    @Id(autoincrement = true)
    private Long id;
    private String aminity;
    private String photoIdFk;
    @Generated(hash = 373263892)
    public RestaurantAminities(Long id, String aminity, String photoIdFk) {
        this.id = id;
        this.aminity = aminity;
        this.photoIdFk = photoIdFk;
    }
    @Generated(hash = 1927534300)
    public RestaurantAminities() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAminity() {
        return this.aminity;
    }
    public void setAminity(String aminity) {
        this.aminity = aminity;
    }
    public String getPhotoIdFk() {
        return this.photoIdFk;
    }
    public void setPhotoIdFk(String photoIdFk) {
        this.photoIdFk = photoIdFk;
    }
}
