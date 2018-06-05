package com.snapxeats.ui.home.fragment.checkin;

import android.content.Context;

import com.snapxeats.common.DbHelper;
import com.snapxeats.common.utilities.AppUtility;

import javax.inject.Inject;

import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Prajakta Patil on 1/6/18.
 */
public class CheckInDbHelper {
    private Context mContext;

    @Inject
    CheckInDbHelper() {
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

    public void saveCheckInDataInDb(String restId,
                                    String userId,
                                    String checkInTime,
                                    boolean isCheckedIn) {
        CheckInData checkInData = new CheckInData();
        checkInData.setRestId(restId);
        checkInData.setUserId(userId);
        checkInData.setCheckInTime(checkInTime);
        checkInData.setIsCheckedIn(isCheckedIn);
        if (ZERO == dbHelper.getCheckInDataDao().loadAll().size()) {
            dbHelper.getCheckInDataDao().insert(checkInData);
        } else {
            dbHelper.getCheckInDataDao().update(checkInData);

        }
    }

    public void clearCheckInData() {
        CheckInDataDao checkInDataDao = dbHelper.getDaoSesion().getCheckInDataDao();
        checkInDataDao.queryBuilder().buildDelete().executeDeleteWithoutDetachingEntities();
        CheckInData checkInData = new CheckInData();
        checkInData.setRestId(null);
        checkInData.setUserId(null);
        checkInData.setCheckInTime(null);
        checkInData.setIsCheckedIn(false);
        dbHelper.getDaoSesion().clear();
    }
}
