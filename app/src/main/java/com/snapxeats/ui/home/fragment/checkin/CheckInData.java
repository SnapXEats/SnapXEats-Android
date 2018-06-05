package com.snapxeats.ui.home.fragment.checkin;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Prajakta Patil on 1/6/18.
 */
@Entity
public class CheckInData {
    @Id
    private String restId;
    private String userId;
    private String checkInTime;
    private boolean isCheckedIn=false;
    @Generated(hash = 241250191)
    public CheckInData(String restId, String userId, String checkInTime,
            boolean isCheckedIn) {
        this.restId = restId;
        this.userId = userId;
        this.checkInTime = checkInTime;
        this.isCheckedIn = isCheckedIn;
    }
    @Generated(hash = 1703976767)
    public CheckInData() {
    }
    public String getRestId() {
        return this.restId;
    }
    public void setRestId(String restId) {
        this.restId = restId;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getCheckInTime() {
        return this.checkInTime;
    }
    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }
    public boolean getIsCheckedIn() {
        return this.isCheckedIn;
    }
    public void setIsCheckedIn(boolean isCheckedIn) {
        this.isCheckedIn = isCheckedIn;
    }
   
}
