package com.snapxeats.ui.home.fragment.snapnshare;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
import com.snapxeats.R;

import java.util.concurrent.TimeUnit;

import static com.snapxeats.common.constants.UIConstants.NOTIFICATION_ID;
import static com.snapxeats.common.constants.UIConstants.PHOTO_NOTIFICATION_REQUEST_CODE;
import static com.snapxeats.common.constants.UIConstants.PHOTO_NOTIFICATION_TIME;

/**
 * Created by Snehal Tembare on 26/4/18.
 */

public class RemindMeLaterService extends IntentService {


    public RemindMeLaterService() {
        super("RemindMeLaterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String restaurantId = intent.getStringExtra(getString(R.string.intent_restaurant_id));

        Intent notifyIntent = new Intent(this, SnapNotificationReceiver.class);
        notifyIntent.putExtra(getString(R.string.intent_restaurant_id), restaurantId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, PHOTO_NOTIFICATION_REQUEST_CODE, notifyIntent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (null != notificationManager) {
            notificationManager.cancel(NOTIFICATION_ID);
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (null != alarmManager) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    TimeUnit.SECONDS.toMillis(PHOTO_NOTIFICATION_TIME), pendingIntent);
        }
    }
}
